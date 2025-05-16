package com.team2.grabtable.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.grabtable.config.SecurityConfig;
import com.team2.grabtable.config.OwnerDetailsService;
import com.team2.grabtable.config.MyAuthenticationSuccessHandler;
import com.team2.grabtable.config.MyAuthenticationFailureHandler;
import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.reservation.controller.ReservationSlotController;
import com.team2.grabtable.domain.reservation.dto.ReservationSlotRequestDto;
import com.team2.grabtable.domain.reservation.dto.ReservationSlotResponseDto;
import com.team2.grabtable.domain.reservation.entity.ReservationSlot.MembershipLevel;
import com.team2.grabtable.domain.reservation.service.ReservationSlotService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservationSlotController.class)
@Import({SecurityConfig.class, MyAuthenticationSuccessHandler.class, MyAuthenticationFailureHandler.class})
class ReservationSlotControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean OwnerDetailsService ownerDetailsService; // 시큐리티용
    @MockBean ReservationSlotService slotService;

    // 공통으로 사용할 가짜 인증주체
    private final OwnerDetails mockOwner = new OwnerDetails(
            com.team2.grabtable.domain.owner.entity.Owner.builder()
                    .ownerId(10L).build()
    );

    @Test
    @DisplayName("POST /owner/slots - 성공")
    void createSlot_success() throws Exception {
        // given
        ReservationSlotRequestDto req = ReservationSlotRequestDto.builder()
                .storeId(5L)
                .startTime("09:00")
                .endTime("10:00")
                .allowedMembership(MembershipLevel.GOLD)
                .build();

        ReservationSlotResponseDto resp = ReservationSlotResponseDto.builder()
                .slotId(1L)
                .storeId(5L)
                .startTime("09:00")
                .endTime("10:00")
                .allowedMembership(MembershipLevel.GOLD)
                .build();

        given(slotService.createSlot(eq(10L), any(ReservationSlotRequestDto.class)))
                .willReturn(resp);

        // when / then
        mockMvc.perform(post("/owner/slots")
                        .with(user(mockOwner))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slotId").value(1))
                .andExpect(jsonPath("$.storeId").value(5))
                .andExpect(jsonPath("$.allowedMembership").value("GOLD"));
    }

    @Test
    @DisplayName("GET /owner/slots?storeId= - 성공")
    void getSlots_success() throws Exception {
        // given
        ReservationSlotResponseDto slot1 = ReservationSlotResponseDto.builder()
                .slotId(1L).storeId(5L)
                .startTime("09:00").endTime("10:00")
                .allowedMembership(MembershipLevel.SILVER)
                .build();
        ReservationSlotResponseDto slot2 = ReservationSlotResponseDto.builder()
                .slotId(2L).storeId(5L)
                .startTime("10:00").endTime("11:00")
                .allowedMembership(MembershipLevel.BRONZE)
                .build();
        given(slotService.getSlotsByStore(10L, 5L))
                .willReturn(List.of(slot1, slot2));

        // when / then
        mockMvc.perform(get("/owner/slots")
                        .with(user(mockOwner))
                        .param("storeId", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slotId").value(1))
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("DELETE /owner/slots/{slotId} - 성공")
    void deleteSlot_success() throws Exception {
        // given
        doNothing().when(slotService).deleteSlot(10L, 7L);

        // when / then
        mockMvc.perform(delete("/owner/slots/{slotId}", 7L)
                        .with(user(mockOwner))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
