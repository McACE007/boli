package com.boli.auctionservice.dto;

import com.boli.common.enums.AuctionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuctionRulesResponse {
    private AuctionStatus status;
    private Double startingPrice;
    private Double minIncrement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long sellerId;
}
