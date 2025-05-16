package com.team2.grabtable.review;

import com.team2.grabtable.config.MyAuthenticationFailureHandler;
import com.team2.grabtable.config.MyAuthenticationSuccessHandler;
import com.team2.grabtable.config.OwnerDetailsService;
import com.team2.grabtable.config.SecurityConfig;
import com.team2.grabtable.domain.review.controller.ReviewController;
import com.team2.grabtable.domain.review.dto.ReviewResultDto;
import com.team2.grabtable.domain.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class)
@Import({SecurityConfig.class, MyAuthenticationSuccessHandler.class, MyAuthenticationFailureHandler.class})
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private OwnerDetailsService ownerDetailsService;

    @Test
    void testFindByStoreId() throws Exception {
        long storeId = 1L;
        ReviewResultDto dto = new ReviewResultDto();
        dto.setResult("success");
        dto.setReviewDtoList(Collections.emptyList());
        when(reviewService.findByStoreId(eq(storeId), eq(0), eq(10))).thenReturn(dto);

        mockMvc.perform(get("/api/admin/stores/{storeId}/reviews", storeId)
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .with(user("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("success"))
                .andExpect(jsonPath("$.reviewDtoList").isArray())
                .andExpect(jsonPath("$.reviewDtoList").isEmpty());
    }

    @Test
    void testCountByStoreStoreId() throws Exception {
        long storeId = 2L;
        ReviewResultDto dto = new ReviewResultDto();
        dto.setResult("success");
        dto.setCount(5L);
        when(reviewService.countByStoreStoreId(storeId)).thenReturn(dto);

        mockMvc.perform(get("/api/admin/stores/{storeId}/reviews/count", storeId)
                        .with(user("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("success"))
                .andExpect(jsonPath("$.count").value(5));
    }

    @Test
    void testFindByMenuId() throws Exception {
        long menuId = 3L;
        ReviewResultDto dto = new ReviewResultDto();
        dto.setResult("success");
        dto.setReviewDtoList(Collections.emptyList());
        when(reviewService.findByMenuId(menuId)).thenReturn(dto);

        mockMvc.perform(get("/api/admin/menus/{menuId}/reviews", menuId)
                        .with(user("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("success"))
                .andExpect(jsonPath("$.reviewDtoList").isEmpty());
    }

    @Test
    void testFindByReviewId() throws Exception {
        long reviewId = 4L;
        ReviewResultDto dto = new ReviewResultDto();
        dto.setResult("success");
        when(reviewService.findByReviewId(reviewId)).thenReturn(dto);

        mockMvc.perform(get("/api/admin/reviews/{reviewId}", reviewId)
                        .with(user("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("success"));
    }
}
