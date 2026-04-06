package com.boli.biddingservice.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedActionException extends AuctionServiceException{
    public UnauthorizedActionException(String action) {
        super("You are not authorized to perform this action: " + action, HttpStatus.FORBIDDEN);
    }
}
