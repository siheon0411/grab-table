package com.team2.grabtable.domain.reservation.service;

import com.team2.grabtable.domain.reservation.dto.ReservationSlotRequestDto;
import com.team2.grabtable.domain.reservation.dto.ReservationSlotResponseDto;

import java.util.List;

public interface ReservationSlotService {
    ReservationSlotResponseDto createSlot(Long ownerId, ReservationSlotRequestDto dto);
    List<ReservationSlotResponseDto> getSlotsByStore(Long ownerId, Long storeId);
    void deleteSlot(Long ownerId, Long slotId);

}