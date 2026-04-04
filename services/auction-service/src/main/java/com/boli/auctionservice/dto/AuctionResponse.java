package com.boli.auctionservice.dto;

import com.boli.auctionservice.enums.AuctionStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AuctionResponse {
    private Long id;
    private String title;
    private String description;
    private Double startingPrice;
    private Double minIncrement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AuctionStatus status;
    private Long sellerId;
    private LocalDateTime createdAt;
}
