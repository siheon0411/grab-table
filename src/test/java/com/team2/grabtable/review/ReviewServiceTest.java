package com.team2.grabtable.review;

import com.team2.grabtable.domain.Menu.entity.Menu;
import com.team2.grabtable.domain.Menu.repository.MenuRepository;
import com.team2.grabtable.domain.review.dto.ReviewDto;
import com.team2.grabtable.domain.review.dto.ReviewResultDto;
import com.team2.grabtable.domain.review.entity.Review;
import com.team2.grabtable.domain.review.repository.ReviewRepository;
import com.team2.grabtable.domain.review.service.ReviewServiceImpl;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import com.team2.grabtable.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Store store;
    private User user;
    private Menu menu;

    @BeforeEach
    void setUp() {
        // common domain setup
        user = new User();
        user.setUserId(1L);
        user.setName("tester");

        store = new Store();
        store.setStoreId(2L);

        menu = new Menu();
        menu.setMenuId(3L);
        menu.setName("Pizza");
    }

    @Test
    void findByStoreId_success() {
        // Given
        Long storeId = store.getStoreId();
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        Review review = Review.builder()
                .reviewId(10L)
                .user(user)
                .store(store)
                .menu(menu)
                .content("Great")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Page<Review> page = new PageImpl<>(Collections.singletonList(review), PageRequest.of(0, 1), 1);
        when(reviewRepository.findByStoreId(eq(storeId), any(PageRequest.class))).thenReturn(page);

        // When
        ReviewResultDto result = reviewService.findByStoreId(storeId, 0, 1);

        // Then
        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getReviewDtoList()).hasSize(1);
        ReviewDto dto = result.getReviewDtoList().get(0);
        assertThat(dto.getReviewId()).isEqualTo(10L);
        assertThat(dto.getUsername()).isEqualTo("tester");
        verify(storeRepository).findById(storeId);
        verify(reviewRepository).findByStoreId(eq(storeId), any(PageRequest.class));
    }

    @Test
    void findByStoreId_storeNotFound() {
        // Given
        Long storeId = 5L;
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // When
        ReviewResultDto result = reviewService.findByStoreId(storeId, 0, 10);

        // Then
        assertThat(result.getResult()).isEqualTo("fail");
        assertThat(result.getReviewDtoList()).isNullOrEmpty();
    }

    @Test
    void countByStoreStoreId_success() {
        // Given
        Long storeId = store.getStoreId();
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(reviewRepository.countByStoreStoreId(storeId)).thenReturn(42L);

        // When
        ReviewResultDto result = reviewService.countByStoreStoreId(storeId);

        // Then
        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getCount()).isEqualTo(42L);
        verify(reviewRepository).countByStoreStoreId(storeId);
    }

    @Test
    void countByStoreStoreId_storeNotFound() {
        // Given
        Long storeId = 6L;
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // When
        ReviewResultDto result = reviewService.countByStoreStoreId(storeId);

        // Then
        assertThat(result.getResult()).isEqualTo("fail");
    }

    @Test
    void findByMenuId_success() {
        // Given
        Long menuId = menu.getMenuId();
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        Review review = Review.builder()
                .reviewId(20L)
                .user(user)
                .store(store)
                .menu(menu)
                .content("OK")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(reviewRepository.findByMenuId(menuId)).thenReturn(Collections.singletonList(review));

        // When
        ReviewResultDto result = reviewService.findByMenuId(menuId);

        // Then
        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getReviewDtoList()).hasSize(1);
        verify(reviewRepository).findByMenuId(menuId);
    }

    @Test
    void findByMenuId_menuNotFound() {
        // Given
        Long menuId = 7L;
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        // When
        ReviewResultDto result = reviewService.findByMenuId(menuId);

        // Then
        assertThat(result.getResult()).isEqualTo("fail");
    }

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private ReviewServiceImpl reviewServiceWithMenu;

    @Test
    void findByReviewId_success() {
        // Given
        Long reviewId = 30L;
        Review review = Review.builder()
                .reviewId(reviewId)
                .user(user)
                .store(store)
                .menu(menu)
                .content("Nice")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // When
        ReviewResultDto result = reviewService.findByReviewId(reviewId);

        // Then
        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getReviewDto()).isNotNull();
        assertThat(result.getReviewDto().getReviewId()).isEqualTo(reviewId);
    }

    @Test
    void findByReviewId_notFound() {
        // Given
        Long reviewId = 31L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When
        ReviewResultDto result = reviewService.findByReviewId(reviewId);

        // Then
        assertThat(result.getResult()).isNull();
        assertThat(result.getReviewDto()).isNull();
    }
}
