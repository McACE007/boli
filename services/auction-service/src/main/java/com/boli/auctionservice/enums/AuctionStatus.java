package com.boli.auctionservice.enums;

public enum AuctionStatus {
    CREATED, SCHEDULED, LIVE, ENDED, CANCELLED;

    public boolean canTransitionTo(AuctionStatus newStatus) {
        return switch (this) {
            case CREATED -> newStatus == SCHEDULED || newStatus == CANCELLED;
            case SCHEDULED -> newStatus == LIVE || newStatus == CANCELLED;
            case LIVE -> newStatus == ENDED;
            case ENDED, CANCELLED -> false;
        };
    }

    public boolean isEditable() {
        return this == CREATED || this == SCHEDULED;
    }

    public boolean isBiddable() {
        return this == LIVE;
    }
}
