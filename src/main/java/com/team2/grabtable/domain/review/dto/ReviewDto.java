package com.team2.grabtable.domain.review.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewDto {
    private Long reviewId;
    // todo : 가게이름 으로??
    private String username;
    private Long storeId;
    private String menuName;
    // todo : 예약 연결
//    private Long reservationId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private byte[] image;
    private String imageContentType;
}

