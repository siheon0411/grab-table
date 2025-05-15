package com.team2.grabtable.domain.review.controller;

import com.team2.grabtable.domain.review.dto.ReviewResultDto;
import com.team2.grabtable.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class ReviewController {
    
    private final ReviewService reviewService;

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ReviewResultDto> findByStoreId(@PathVariable("storeId") Long storeId,
                                                         @RequestParam("pageNumber") Integer pageNumber,
                                                         @RequestParam("pageSize") Integer pageSize) {

        return ResponseEntity.ok(reviewService.findByStoreId(storeId, pageNumber, pageSize));
    }

    @GetMapping("/stores/{storeId}/reviews/count")
    public ResponseEntity<ReviewResultDto> countByStoreStoreId(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(reviewService.countByStoreStoreId(storeId));
    }

    @GetMapping("/menus/{menuId}/reviews")
    public ResponseEntity<ReviewResultDto> findByMenuId(@PathVariable("menuId") Long menuId) {
        return ResponseEntity.ok(reviewService.findByMenuId(menuId));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResultDto> findByReviewId(@PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(reviewService.findByReviewId(reviewId));
    }

}
