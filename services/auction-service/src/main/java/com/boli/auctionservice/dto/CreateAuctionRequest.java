package com.boli.auctionservice.dto;

import com.boli.auctionservice.enums.AuctionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Data
@Builder
public class CreateAuctionRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private Double startingPrice;
    private Double minIncrement;
    @Future
    private Timestamp startTime;
    private Timestamp endTime;
}
