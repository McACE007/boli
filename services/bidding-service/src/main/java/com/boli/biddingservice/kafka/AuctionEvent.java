package com.boli.biddingservice.kafka;

import com.boli.common.enums.AuctionEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionEvent {
    private AuctionEventType eventType;
    private LocalDateTime timestamp;
    private AuctionEventPayload data;
}
