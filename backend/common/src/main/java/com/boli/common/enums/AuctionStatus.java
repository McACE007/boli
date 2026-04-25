package com.boli.common.enums;

public enum AuctionStatus {
    SCHEDULED, LIVE, ENDED, CANCELLED;

    public boolean canTransitionTo(AuctionStatus newStatus) {
        return switch (this) {
            case SCHEDULED -> newStatus == LIVE || newStatus == CANCELLED;
            case LIVE -> newStatus == ENDED;
            case ENDED, CANCELLED -> false;
        };
    }

    public boolean isEditable() {
        return this == SCHEDULED;
    }

    public boolean isBiddable() {
        return this == LIVE;
    }
}
