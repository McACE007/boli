package com.boli.auctionservice.kafka;

import com.boli.common.enums.AuctionEventType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuctionEvent {
    private AuctionEventType eventType;
    private LocalDateTime timestamp;
    private AuctionEventPayload data;
}
