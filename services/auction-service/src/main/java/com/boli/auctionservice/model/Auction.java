package com.boli.auctionservice.model;

import com.boli.auctionservice.enums.AuctionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Entity
@Table(schema = "auction")
@Getter
@Setter
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;
    @Column(name = "starting_price", nullable = false)
    private Double startingPrice;
    @Column(name = "min_increment", nullable = false)
    private Double minIncrement;
    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;
    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;
    @Column(nullable = false)
    private AuctionStatus status;
    @Column(name = "winner_id")
    private Long winnerId;
    @Column(name = "winning_amount")
    private Double winningAmount;
    @Column(name = "created_at")
    @CreatedDate
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
