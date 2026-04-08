package com.boli.biddingservice.exception;


import org.springframework.http.HttpStatus;

public class InvalidBidException extends BidServiceException {
    public InvalidBidException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
