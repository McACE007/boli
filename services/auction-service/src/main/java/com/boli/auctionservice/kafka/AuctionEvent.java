package com.boli.auctionservice.kafka;

import com.boli.common.enums.AuctionEventType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuctionEvent {
    private AuctionEventType eventType;
    private Instant timestamp;
    private AuctionEventPayload data;
}
