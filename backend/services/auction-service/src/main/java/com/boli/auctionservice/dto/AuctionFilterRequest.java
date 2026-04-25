package com.boli.auctionservice.dto;

import com.boli.common.enums.AuctionStatus;
import lombok.Data;

@Data
public class AuctionFilterRequest {
    private AuctionStatus status;
    private Double minPrice;
    private Double maxPrice;

    // pagination
    private int page = 0;      // default 0
    private int size = 10;     // default 10
}
