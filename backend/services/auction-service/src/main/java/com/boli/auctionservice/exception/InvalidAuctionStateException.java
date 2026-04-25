package com.boli.auctionservice.exception;

import com.boli.common.enums.AuctionStatus;
import org.springframework.http.HttpStatus;

public class InvalidAuctionStateException extends AuctionServiceException{
    public InvalidAuctionStateException(AuctionStatus current, AuctionStatus attempted) {
        super("Cannot transition auction from " + current + " to " + attempted, HttpStatus.CONFLICT);
    }
}
