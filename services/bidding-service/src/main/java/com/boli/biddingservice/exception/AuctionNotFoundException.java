package com.boli.biddingservice.exception;

import org.springframework.http.HttpStatus;

public class AuctionNotFoundException extends AuctionServiceException{
    public AuctionNotFoundException(Long auctionId) {
        super("Bid not found: " + auctionId, HttpStatus.NOT_FOUND);
    }
}
