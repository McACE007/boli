package com.boli.auctionservice.dto;

import com.boli.common.enums.AuctionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuctionResponse {
    private Long id;
    private String title;
    private String description;
    private Double startingPrice;
    private Double minIncrement;
    private Instant startTime;
    private Instant endTime;
    private AuctionStatus status;
    private Long sellerId;
    private Instant createdAt;
}
