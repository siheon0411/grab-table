package com.team2.grabtable.store;

import com.team2.grabtable.config.MyAuthenticationFailureHandler;
import com.team2.grabtable.config.MyAuthenticationSuccessHandler;
import com.team2.grabtable.config.SecurityConfig;
import com.team2.grabtable.config.OwnerDetailsService;
import com.team2.grabtable.domain.store.controller.StoreController;
import com.team2.grabtable.domain.store.dto.StoreResultDto;
import com.team2.grabtable.domain.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StoreController.class)
@Import({SecurityConfig.class, MyAuthenticationSuccessHandler.class, MyAuthenticationFailureHandler.class})
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @MockBean
    private OwnerDetailsService ownerDetailsService;

    @Test
    void testFindStoresByOwner() throws Exception {
        // Prepare DTO with empty list and count
        StoreResultDto resultDto = new StoreResultDto();
        resultDto.setStoreDtoList(Collections.emptyList());
        resultDto.setCount(0L);
        when(storeService.findStoresByOwnerId(any())).thenReturn(resultDto);

        mockMvc.perform(get("/api/admin/stores")
                        .with(user("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.storeDtoList").isArray())
                .andExpect(jsonPath("$.storeDtoList").isEmpty())
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
    void testGetStoreDetail() throws Exception {
        Long storeId = 1L;
        StoreResultDto detailDto = new StoreResultDto();
        // Set up detailDto fields as needed
        when(storeService.getStoreDetail(any(), eq(storeId))).thenReturn(detailDto);

        mockMvc.perform(get("/api/admin/stores/{id}", storeId)
                        .with(user("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // 추가 검증: .andExpect(jsonPath(...))
    }

    @Test
    void testCreateStore() throws Exception {
        StoreResultDto createdDto = new StoreResultDto();
        // Populate createdDto as needed
        when(storeService.insertStore(any(), any())).thenReturn(createdDto);

        mockMvc.perform(multipart("/api/admin/stores")
                        .file(new MockMultipartFile("image", "test.png", "image/png", "dummy-data".getBytes()))
                        .param("name", "Test Store")
                        .param("location", "Test Location")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateStore() throws Exception {
        Long storeId = 2L;
        StoreResultDto updatedDto = new StoreResultDto();
        // Populate updatedDto as needed
        when(storeService.updateStore(any(), eq(storeId), any())).thenReturn(updatedDto);

        mockMvc.perform(multipart("/api/admin/stores/{id}", storeId)
                        .file(new MockMultipartFile("image", "update.png", "image/png", "dummy-data".getBytes()))
                        .param("name", "Updated Store")
                        .param("location", "Updated Location")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteStore() throws Exception {
        Long storeId = 3L;
        StoreResultDto deletedDto = new StoreResultDto();
        // Populate deletedDto as needed
        when(storeService.deleteStore(any(), eq(storeId))).thenReturn(deletedDto);

        mockMvc.perform(delete("/api/admin/stores/{id}", storeId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}