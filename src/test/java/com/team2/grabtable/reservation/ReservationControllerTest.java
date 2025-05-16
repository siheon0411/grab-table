package com.team2.grabtable.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.grabtable.config.MyAuthenticationFailureHandler;
import com.team2.grabtable.config.MyAuthenticationSuccessHandler;
import com.team2.grabtable.config.OwnerDetailsService;
import com.team2.grabtable.config.SecurityConfig;
import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.reservation.controller.ReservationController;
import com.team2.grabtable.domain.reservation.dto.ReservationDto;
import com.team2.grabtable.domain.reservation.dto.ReservationResultDto;
import com.team2.grabtable.domain.reservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservationController.class)
@Import({SecurityConfig.class, MyAuthenticationSuccessHandler.class, MyAuthenticationFailureHandler.class})
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private OwnerDetailsService ownerDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listReservationByStoreId_success() throws Exception {
        // Given
        Long ownerId = 1L;
        Long storeId = 10L;
        // prepare principal
        Owner owner = Owner.builder().ownerId(ownerId).build();
        OwnerDetails ownerDetails = new OwnerDetails(owner);
        // prepare service response
        ReservationResultDto dto = new ReservationResultDto();
        dto.setResult("success");
        dto.setReservationDtoList(Collections.emptyList());
        when(reservationService.listReservationByStoreId(eq(ownerId), eq(storeId))).thenReturn(dto);

        // When / Then
        mockMvc.perform(get("/api/reservation/{storeId}", storeId)
                        .with(user(ownerDetails))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("success"))
                .andExpect(jsonPath("$.reservationDtoList").isArray())
                .andExpect(jsonPath("$.reservationDtoList").isEmpty());
    }

    @Test
    void changeStatus_success() throws Exception {
        // Given
        Long ownerId = 2L;
        Long storeId = 20L;
        Long reservationId = 200L;
        Owner owner = Owner.builder().ownerId(ownerId).build();
        OwnerDetails ownerDetails = new OwnerDetails(owner);
        ReservationResultDto dto = new ReservationResultDto();
        dto.setResult("success");
        when(reservationService.changeStatus(eq(ownerId), eq(storeId), eq(reservationId))).thenReturn(dto);

        // When / Then
        mockMvc.perform(patch("/api/reservation/{storeId}/{reservationId}", storeId, reservationId)
                        .with(user(ownerDetails))
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("success"));
    }

    @Test
    void deleteReservation_success() throws Exception {
        // Given
        Long ownerId = 3L;
        Long storeId = 30L;
        Long reservationId = 300L;
        Owner owner = Owner.builder().ownerId(ownerId).build();
        OwnerDetails ownerDetails = new OwnerDetails(owner);
        ReservationResultDto dto = new ReservationResultDto();
        dto.setResult("success");
        when(reservationService.deleteReservation(eq(ownerId), eq(reservationId))).thenReturn(dto);

        // When / Then
        mockMvc.perform(delete("/api/reservation/{storeId}/{reservationId}", storeId, reservationId)
                        .with(user(ownerDetails))
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("success"));
    }
}
