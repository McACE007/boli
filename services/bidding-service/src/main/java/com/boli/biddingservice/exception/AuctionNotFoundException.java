package com.boli.biddingservice.exception;

import org.springframework.http.HttpStatus;

public class AuctionNotFoundException extends AuctionServiceException{
    public AuctionNotFoundException(Long auctionId) {
        super("Auction not found: " + auctionId, HttpStatus.NOT_FOUND);
    }
}
