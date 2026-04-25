package com.boli.biddingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BidRequest {
    @NotNull(message = "Auction Id is required")
    private Long auctionId;
    @DecimalMin(value = "1.0", message = "Bid amount must be at least 1.0")
    private Double amount;
}
