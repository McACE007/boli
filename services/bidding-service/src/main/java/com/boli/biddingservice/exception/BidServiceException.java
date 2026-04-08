package com.boli.biddingservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BidServiceException extends RuntimeException {
    private final HttpStatus status;

    public BidServiceException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

}
