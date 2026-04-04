package com.boli.auctionservice.mapper;

import com.boli.auctionservice.dto.AuctionResponse;
import com.boli.auctionservice.dto.CreateAuctionRequest;
import com.boli.auctionservice.model.Auction;
import org.springframework.stereotype.Component;

@Component
public class AuctionMapper {
    public Auction toAuction(CreateAuctionRequest request) {
        Auction auction = new Auction();
        auction.setTitle(request.getTitle());
        auction.setDescription(request.getDescription());
        auction.setStartingPrice(request.getStartingPrice());
        auction.setMinIncrement(request.getMinIncrement());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        return auction;
    }

    public AuctionResponse toAuctionResponse(Auction auction) {
        return AuctionResponse.builder()
                .id(auction.getId()).title(auction.getTitle()).description(auction.getDescription()).startingPrice(auction.getStartingPrice()).minIncrement(auction.getMinIncrement()).startTime(auction.getStartTime()).endTime(auction.getEndTime()).status(auction.getStatus()).sellerId(auction.getSellerId()).createdAt(auction.getCreatedAt()).build();
    }
}
