package com.boli.auctionservice.repository.wrapper;

import com.boli.common.enums.AuctionStatus;
import com.boli.auctionservice.model.Auction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction> {
    @Query("SELECT a FROM Auction a WHERE a.status = :status AND a.startTime <= :now")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Auction> findAndLockAuctionsToStart(
            @Param("status") AuctionStatus status,
            @Param("now") Instant now
    );

    @Query("SELECT a FROM Auction a WHERE a.status = :status AND a.endTime <= :now")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Auction> findAndLockAuctionsToEnd(
            @Param("status") AuctionStatus status,
            @Param("now") Instant now
    );
}
