package com.team2.grabtable.domain.review.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewDto {
    private Long reviewId;
    private String username;
    private Long storeId;
    private String menuName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private byte[] image;
    private String imageContentType;
}

