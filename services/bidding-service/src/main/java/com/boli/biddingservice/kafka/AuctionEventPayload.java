package com.boli.biddingservice.kafka;

import com.boli.common.enums.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionEventPayload {
    private Long auctionId;
    private Long sellerId;
    private AuctionStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
