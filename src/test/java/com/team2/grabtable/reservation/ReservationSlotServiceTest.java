package com.team2.grabtable.reservation;

import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.reservation.dto.ReservationSlotRequestDto;
import com.team2.grabtable.domain.reservation.dto.ReservationSlotResponseDto;
import com.team2.grabtable.domain.reservation.entity.ReservationSlot;
import com.team2.grabtable.domain.reservation.entity.ReservationSlot.MembershipLevel;
import com.team2.grabtable.domain.reservation.repository.ReservationSlotRepository;
import com.team2.grabtable.domain.reservation.service.ReservationSlotServiceImpl;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationSlotServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ReservationSlotRepository slotRepository;

    @InjectMocks
    private ReservationSlotServiceImpl service;

    @Test
    @DisplayName("createSlot 성공")
    void createSlot_success() {
        Long ownerId = 10L;
        Long storeId = 20L;

        Owner owner = Owner.builder().ownerId(ownerId).build();
        Store store = Store.builder()
                .storeId(storeId)
                .owner(owner)
                .build();

        ReservationSlotRequestDto req = ReservationSlotRequestDto.builder()
                .storeId(storeId)
                .startTime("08:00")
                .endTime("09:00")
                .allowedMembership(MembershipLevel.SILVER)
                .build();

        ReservationSlot savedEntity = ReservationSlot.builder()
                .slotId(5L)
                .store(store)
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .allowedMembership(req.getAllowedMembership())
                .build();

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(slotRepository.save(any(ReservationSlot.class))).willReturn(savedEntity);

        ReservationSlotResponseDto resp = service.createSlot(ownerId, req);

        assertThat(resp.getSlotId()).isEqualTo(5L);
        assertThat(resp.getStoreId()).isEqualTo(storeId);
        assertThat(resp.getStartTime()).isEqualTo("08:00");
        assertThat(resp.getEndTime()).isEqualTo("09:00");
        assertThat(resp.getAllowedMembership()).isEqualTo(MembershipLevel.SILVER);

        then(storeRepository).should().findById(storeId);
        then(slotRepository).should().save(any(ReservationSlot.class));
    }

    @Test
    @DisplayName("createSlot - 가게 없음 -> IllegalArgumentException")
    void createSlot_storeNotFound() {
        Long ownerId = 10L;
        Long storeId = 20L;

        ReservationSlotRequestDto req = ReservationSlotRequestDto.builder()
                .storeId(storeId)
                .startTime("08:00")
                .endTime("09:00")
                .allowedMembership(MembershipLevel.GOLD)
                .build();

        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.createSlot(ownerId, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가게를 찾을 수 없습니다.");

        then(storeRepository).should().findById(storeId);
        then(slotRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("createSlot - 소유주 불일치 -> IllegalStateException")
    void createSlot_ownerMismatch() {
        Long ownerId = 10L;
        Long otherOwnerId = 99L;
        Long storeId = 20L;

        Owner owner = Owner.builder().ownerId(otherOwnerId).build();
        Store store = Store.builder()
                .storeId(storeId)
                .owner(owner)
                .build();

        ReservationSlotRequestDto req = ReservationSlotRequestDto.builder()
                .storeId(storeId)
                .startTime("08:00")
                .endTime("09:00")
                .allowedMembership(MembershipLevel.GOLD)
                .build();

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        assertThatThrownBy(() -> service.createSlot(ownerId, req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 가게에 접근 권한이 없습니다.");

        then(storeRepository).should().findById(storeId);
        then(slotRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("getSlotsByStore 성공")
    void getSlotsByStore_success() {
        Long ownerId = 10L;
        Long storeId = 20L;

        Owner owner = Owner.builder().ownerId(ownerId).build();
        Store store = Store.builder().storeId(storeId).owner(owner).build();

        ReservationSlot slot1 = ReservationSlot.builder()
                .slotId(1L)
                .store(store)
                .startTime("07:00")
                .endTime("08:00")
                .allowedMembership(MembershipLevel.BRONZE)
                .build();
        ReservationSlot slot2 = ReservationSlot.builder()
                .slotId(2L)
                .store(store)
                .startTime("08:00")
                .endTime("09:00")
                .allowedMembership(MembershipLevel.GOLD)
                .build();

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(slotRepository.findByStore_StoreId(storeId))
                .willReturn(List.of(slot1, slot2));

        List<ReservationSlotResponseDto> list = service.getSlotsByStore(ownerId, storeId);

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getSlotId()).isEqualTo(1L);
        assertThat(list.get(1).getAllowedMembership()).isEqualTo(MembershipLevel.GOLD);

        then(storeRepository).should().findById(storeId);
        then(slotRepository).should().findByStore_StoreId(storeId);
    }

    @Test
    @DisplayName("getSlotsByStore - 가게 없음 -> IllegalArgumentException")
    void getSlotsByStore_storeNotFound() {
        Long ownerId = 10L;
        Long storeId = 20L;

        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSlotsByStore(ownerId, storeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가게를 찾을 수 없습니다.");

        then(storeRepository).should().findById(storeId);
        then(slotRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("getSlotsByStore - 소유주 불일치 -> IllegalStateException")
    void getSlotsByStore_ownerMismatch() {
        Long ownerId = 10L;
        Long otherOwnerId = 99L;
        Long storeId = 20L;

        Store store = Store.builder()
                .storeId(storeId)
                .owner(Owner.builder().ownerId(otherOwnerId).build())
                .build();

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        assertThatThrownBy(() -> service.getSlotsByStore(ownerId, storeId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 가게에 접근 권한이 없습니다.");

        then(storeRepository).should().findById(storeId);
        then(slotRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("deleteSlot 성공")
    void deleteSlot_success() {
        Long ownerId = 10L;
        Long slotId = 30L;

        Owner owner = Owner.builder().ownerId(ownerId).build();
        Store store = Store.builder().storeId(20L).owner(owner).build();
        ReservationSlot slot = ReservationSlot.builder()
                .slotId(slotId)
                .store(store)
                .build();

        given(slotRepository.findById(slotId)).willReturn(Optional.of(slot));

        service.deleteSlot(ownerId, slotId);

        then(slotRepository).should().findById(slotId);
        then(slotRepository).should().delete(slot);
    }

    @Test
    @DisplayName("deleteSlot - 슬롯 없음 -> IllegalArgumentException")
    void deleteSlot_notFound() {
        Long ownerId = 10L;
        Long slotId = 30L;

        given(slotRepository.findById(slotId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteSlot(ownerId, slotId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 슬롯이 존재하지 않습니다.");

        then(slotRepository).should().findById(slotId);
        then(slotRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("deleteSlot - 소유주 불일치 -> IllegalStateException")
    void deleteSlot_ownerMismatch() {
        Long ownerId = 10L;
        Long otherOwnerId = 99L;
        Long slotId = 30L;

        Store store = Store.builder().storeId(20L).owner(Owner.builder().ownerId(otherOwnerId).build()).build();
        ReservationSlot slot = ReservationSlot.builder()
                .slotId(slotId)
                .store(store)
                .build();

        given(slotRepository.findById(slotId)).willReturn(Optional.of(slot));

        assertThatThrownBy(() -> service.deleteSlot(ownerId, slotId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("삭제 권한이 없습니다.");

        then(slotRepository).should().findById(slotId);
        then(slotRepository).shouldHaveNoMoreInteractions();
    }
}
