package com.boli.auctionservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuctionServiceException extends RuntimeException {
    private final HttpStatus status;

    public AuctionServiceException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

}
