package com.boli.biddingservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BidResponse {
    private Long id;
    private Long auctionId;
    private Long bidderId;
    private Double amount;
    private Boolean isHighest;
}
