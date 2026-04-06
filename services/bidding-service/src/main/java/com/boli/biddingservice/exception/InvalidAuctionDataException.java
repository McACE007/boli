package com.boli.biddingservice.exception;


import org.springframework.http.HttpStatus;

public class InvalidAuctionDataException extends AuctionServiceException {
    public InvalidAuctionDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
