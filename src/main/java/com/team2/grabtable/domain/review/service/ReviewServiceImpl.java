package com.team2.grabtable.domain.review.service;

import com.team2.grabtable.domain.Menu.repository.MenuRepository;
import com.team2.grabtable.domain.review.dto.ReviewDto;
import com.team2.grabtable.domain.review.dto.ReviewResultDto;
import com.team2.grabtable.domain.review.entity.Review;
import com.team2.grabtable.domain.review.repository.ReviewRepository;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResultDto findByStoreId(Long storeId, Integer pageNumber, Integer pageSize) {
        ReviewResultDto reviewResultDto = new ReviewResultDto();

        try {
            storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("Store not found : " + storeId));

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Review> page = reviewRepository.findByStoreId(storeId, pageable);
            List<Review> reviewList = page.toList();
//            List<Review> reviewList = reviewRepository.findByStoreId(storeId);

            List<ReviewDto> reveiwDtoList = new ArrayList<>();
            for (Review review : reviewList) {
                ReviewDto reviewDto = ReviewDto.builder()
                        .reviewId(review.getReviewId())
                        .username(review.getUser().getName())
                        .storeId(review.getStore().getStoreId())
                        .menuName(review.getMenu().getName())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .image(review.getImage())
                        .imageContentType(review.getImageContentType())
                        .build();
                reveiwDtoList.add(reviewDto);
            }
            reviewResultDto.setReviewDtoList(reveiwDtoList);
            reviewResultDto.setResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            reviewResultDto.setResult("fail");
        }

        return reviewResultDto;
    }

    @Override
    public ReviewResultDto countByStoreStoreId(Long storeId) {
        ReviewResultDto reviewResultDto = new ReviewResultDto();

        try {
            storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("Store not found : " + storeId));

            Long count = reviewRepository.countByStoreStoreId(storeId);

            reviewResultDto.setCount(count);
            reviewResultDto.setResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            reviewResultDto.setResult("fail");
        }

        return reviewResultDto;
    }

    @Override
    public ReviewResultDto findByMenuId(Long menuId) {
        ReviewResultDto reviewResultDto = new ReviewResultDto();

        try {
            menuRepository.findById(menuId)
                    .orElseThrow(() -> new RuntimeException("Menu not found : " + menuId));

            List<Review> reviewList = reviewRepository.findByMenuId(menuId);
            List<ReviewDto> reveiwDtoList = new ArrayList<>();
            for (Review review : reviewList) {
                ReviewDto reviewDto = ReviewDto.builder()
                        .reviewId(review.getReviewId())
                        .username(review.getUser().getName())
                        .storeId(review.getStore().getStoreId())
                        .menuName(review.getMenu().getName())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .image(review.getImage())
                        .imageContentType(review.getImageContentType())
                        .build();
                reveiwDtoList.add(reviewDto);
            }
            reviewResultDto.setReviewDtoList(reveiwDtoList);
            reviewResultDto.setResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            reviewResultDto.setResult("fail");
        }

        return reviewResultDto;
    }

    @Override
    public ReviewResultDto findByReviewId(Long reviewId) {
        ReviewResultDto reviewResultDto = new ReviewResultDto();

        try {
            Optional<Review> optionalReview = reviewRepository.findById(reviewId);

            if (optionalReview.isPresent()) {
                Review review = optionalReview.get();
                ReviewDto reviewDto = ReviewDto.builder()
                        .reviewId(review.getReviewId())
                        .username(review.getUser().getName())
                        .storeId(review.getStore().getStoreId())
                        .menuName(review.getMenu().getName())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .image(review.getImage())
                        .imageContentType(review.getImageContentType())
                        .build();
                reviewResultDto.setReviewDto(reviewDto);
                reviewResultDto.setResult("success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            reviewResultDto.setResult("fail");
        }

        return reviewResultDto;
    }

}
