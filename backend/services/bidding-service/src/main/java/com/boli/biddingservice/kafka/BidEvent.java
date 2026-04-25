package com.boli.biddingservice.kafka;

import com.boli.common.enums.BidEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidEvent {
    private BidEventType eventType;
    private Instant timestamp;
    private BidEventPayload data;
}
