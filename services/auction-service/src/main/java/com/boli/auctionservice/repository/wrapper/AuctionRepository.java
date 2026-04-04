package com.boli.auctionservice.repository.wrapper;

import com.boli.auctionservice.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
