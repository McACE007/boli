package com.boli.auctionservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuctionWinnerResponse {
    private Long winnerId;
    private Double winningAmount;
}
