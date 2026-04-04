package com.boli.auctionservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateAuctionRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @Min(message = "Starting Price must be at least 1", value = 1)
    private Double startingPrice;
    @Min(message = "Minimum Increment must be at least 1", value = 1)
    private Double minIncrement;
    @Future(message = "Start Time must be in future")
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
