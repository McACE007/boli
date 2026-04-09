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
public class BidEventPayload {
    private Long bidId;
    private Long bidderId;
    private Long auctionId;
    private Double amount;
}
