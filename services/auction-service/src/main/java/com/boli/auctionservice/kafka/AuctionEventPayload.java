package com.boli.auctionservice.kafka;

import com.boli.common.enums.AuctionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuctionEventPayload {
    private Long auctionId;
    private Long sellerId;
    private AuctionStatus status;
    private Instant startTime;
    private Instant endTime;
    private Double startingPrice;
    private Double minIncrement;
}
