package com.boli.biddingservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BidEventProducer {
    private final KafkaTemplate<String, BidEvent> kafkaTemplate;

    private static final String TOPIC = "bid-events";

    public void publish(BidEvent event) {
        kafkaTemplate.send(TOPIC, event.getData().getAuctionId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("kafka_publish_failed | eventType={} | auctionId={} | bidId={}",
                                event.getEventType(),
                                event.getData().getAuctionId(),
                                event.getData().getBidId(),
                                ex);
                    } else {
                        log.debug("kafka_publish_success | eventType={} | auctionId={} | bidId={} | partition={} | offset={}",
                                event.getEventType(),
                                event.getData().getAuctionId(),
                                event.getData().getBidId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
