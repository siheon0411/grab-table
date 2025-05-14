package com.team2.grabtable.domain.reservation.service;

import com.team2.grabtable.domain.reservation.dto.ReservationSlotRequestDto;
import com.team2.grabtable.domain.reservation.dto.ReservationSlotResponseDto;
import com.team2.grabtable.domain.reservation.entity.ReservationSlot;
import com.team2.grabtable.domain.reservation.repository.ReservationSlotRepository;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationSlotServiceImpl implements ReservationSlotService {

    private final ReservationSlotRepository slotRepository;
    private final StoreRepository storeRepository;

    @Override
    public ReservationSlotResponseDto createSlot(Long ownerId, ReservationSlotRequestDto dto) {
        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getOwnerId().equals(ownerId)) {
            throw new IllegalStateException("해당 가게에 접근 권한이 없습니다.");
        }

        ReservationSlot slot = ReservationSlot.builder()
                .store(store)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .allowedMembership(dto.getAllowedMembership())
                .build();

        ReservationSlot saved = slotRepository.save(slot);

        return ReservationSlotResponseDto.builder()
                .slotId(saved.getSlotId())
                .storeId(saved.getStore().getStoreId())
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .allowedMembership(saved.getAllowedMembership())
                .build();
    }

    @Override
    public List<ReservationSlotResponseDto> getSlotsByStore(Long ownerId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getOwnerId().equals(ownerId)) {
            throw new IllegalStateException("해당 가게에 접근 권한이 없습니다.");
        }

        return slotRepository.findByStore_StoreId(storeId).stream()
                .map(slot -> ReservationSlotResponseDto.builder()
                        .slotId(slot.getSlotId())
                        .storeId(slot.getStore().getStoreId())
                        .startTime(slot.getStartTime())
                        .endTime(slot.getEndTime())
                        .allowedMembership(slot.getAllowedMembership())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSlot(Long ownerId, Long slotId) {
        ReservationSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 슬롯이 존재하지 않습니다."));

        if (!slot.getStore().getOwner().getOwnerId().equals(ownerId)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        slotRepository.delete(slot);
    }
}
