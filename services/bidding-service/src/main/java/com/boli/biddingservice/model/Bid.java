package com.boli.biddingservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "bid")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "auction_id", nullable = false)
    private Long auctionId;
    @Column(name = "bidder_id", nullable = false)
    private Long bidderId;
    @Column(name = "amount", nullable = false)
    private Long amount;
    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;
}
