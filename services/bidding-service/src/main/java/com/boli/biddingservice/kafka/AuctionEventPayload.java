package com.boli.biddingservice.kafka;

import com.boli.common.enums.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionEventPayload {
    private Long auctionId;
    private Long sellerId;
    private AuctionStatus status;
    private Instant startTime;
    private Instant endTime;
    private Double startingPrice;
    private Double minIncrement;
}
