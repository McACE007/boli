package com.boli.auctionservice.kafka;

import com.boli.auctionservice.enums.AuctionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuctionEventPayload {
    private Long auctionId;
    private Long sellerId;
    private AuctionStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
