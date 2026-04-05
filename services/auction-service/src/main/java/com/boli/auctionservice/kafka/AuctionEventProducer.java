package com.boli.auctionservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionEventProducer {
    private final KafkaTemplate<String, AuctionEvent> kafkaTemplate;

    private static final String TOPIC = "auction-events";

    public void publish(AuctionEvent event) {
        kafkaTemplate.send(TOPIC, event.getData().getAuctionId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("kafka_publish_failed | eventType={} | auctionId={}",
                                event.getEventType(),
                                event.getData().getAuctionId(),
                                ex);
                    } else {
                        log.info("kafka_publish_success | eventType={} | auctionId={} | partition={} | offset={}",
                                event.getEventType(),
                                event.getData().getAuctionId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
