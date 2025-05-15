package com.team2.grabtable.domain.review.service;

import com.team2.grabtable.domain.review.dto.ReviewResultDto;

public interface ReviewService {
    ReviewResultDto findByStoreId(Long storeId, Integer pageNumber, Integer pageSize);

    ReviewResultDto countByStoreStoreId(Long storeId);

    ReviewResultDto findByMenuId(Long menuId);

    ReviewResultDto findByReviewId(Long reviewId);

}
