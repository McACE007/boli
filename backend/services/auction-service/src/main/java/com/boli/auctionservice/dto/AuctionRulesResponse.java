package com.boli.auctionservice.dto;

import com.boli.common.enums.AuctionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuctionRulesResponse {
    private AuctionStatus status;
    private Double startingPrice;
    private Double minIncrement;
    private Instant startTime;
    private Instant endTime;
    private Long sellerId;
}
