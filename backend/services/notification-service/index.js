const express = require('express');
const { Kafka } = require('kafkajs');
const cors = require('cors');

const app = express();
app.use(cors()); // Allow frontend to connect across different ports

// This is our in-memory connection manager.
// It maps auctionId -> Array of active HTTP Response objects (clients)
const clients = new Map();

// --- 1. THE SSE ENDPOINT ---
app.get('/api/notifications/stream/:auctionId', (req, res) => {
    const auctionId = req.params.auctionId;

    // 1a. Set the mandatory headers for Server-Sent Events
    res.setHeader('Content-Type', 'text/event-stream');
    res.setHeader('Cache-Control', 'no-cache');
    res.setHeader('Connection', 'keep-alive');
    res.flushHeaders(); // Establish the connection immediately

    // Send an initial heartbeat/connection message
    res.write(`data: ${JSON.stringify({ type: 'CONNECTED', message: `Listening to Auction ${auctionId}` })}\n\n`);

    // 1b. Add this user's connection to our Map
    if (!clients.has(auctionId)) {
        clients.set(auctionId, []);
    }
    clients.get(auctionId).push(res);
    console.log(`[Client Connected] Auction ${auctionId} | Total listeners: ${clients.get(auctionId).length}`);

    // 1c. CLEANUP: If the user closes the browser tab, remove them from the array to prevent memory leaks!
    req.on('close', () => {
        const auctionClients = clients.get(auctionId);
        if (auctionClients) {
            const index = auctionClients.indexOf(res);
            if (index !== -1) {
                auctionClients.splice(index, 1);
            }
            // If no one is listening to this auction anymore, delete the array
            if (auctionClients.length === 0) {
                clients.delete(auctionId);
            }
        }
        console.log(`[Client Disconnected] Auction ${auctionId}`);
    });
});

// --- 2. THE KAFKA CONSUMER ---
const kafka = new Kafka({
    clientId: 'notification-service',
    brokers: ['localhost:9092'] // Point to your Kafka broker
});

const consumer = kafka.consumer({ groupId: 'notification-service-group' });

const runKafka = async () => {
    await consumer.connect();
    // Subscribe to the exact topic we created in the Bidding Service
    await consumer.subscribe({ topic: 'bid-events', fromBeginning: false });

    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            // Parse the JSON from the Bidding Service
            const event = JSON.parse(message.value.toString());
            console.log(`[Kafka Event] ${event.eventType} received for Auction ${event.data.auctionId}`);

            if (event.eventType === 'BID_PLACED') {
                const auctionId = event.data.auctionId.toString();
                const auctionClients = clients.get(auctionId);

                // If we have active browser tabs looking at this auction, push the update!
                if (auctionClients && auctionClients.length > 0) {
                    const payload = JSON.stringify(event.data);

                    // SSE strictly requires the format: "data: <string>\n\n"
                    auctionClients.forEach(client => client.write(`data: ${payload}\n\n`));
                    console.log(`[SSE Broadcast] Pushed new bid of ${event.data.amount} to ${auctionClients.length} users.`);
                }
            }
        },
    });
};

runKafka().catch(console.error);

// --- 3. START SERVER ---
const PORT = 8093; // Make sure this doesn't conflict with your Java ports
app.listen(PORT, () => {
    console.log(`🚀 Notification Service (Node.js/SSE) running on http://localhost:${PORT}`);
});