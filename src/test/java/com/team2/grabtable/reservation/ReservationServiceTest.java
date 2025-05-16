package com.team2.grabtable.reservation;

import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.owner.repository.OwnerRepository;
import com.team2.grabtable.domain.reservation.dto.ReservationResultDto;
import com.team2.grabtable.domain.reservation.entity.Reservation;
import com.team2.grabtable.domain.reservation.entity.ReservationSlot;
import com.team2.grabtable.domain.reservation.repository.ReservationRepository;
import com.team2.grabtable.domain.reservation.repository.ReservationSlotRepository;
import com.team2.grabtable.domain.reservation.service.ReservationServiceImpl;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationSlotRepository reservationSlotRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void listReservationByStoreId_success() {
        Long ownerId = 1L;
        Long storeId = 2L;
        Owner owner = Owner.builder().ownerId(ownerId).build();
        Store store = Store.builder().storeId(storeId).owner(owner).build();

        ReservationSlot slot = ReservationSlot.builder()
                .slotId(3L)
                .startTime("09:00")
                .build();
        Reservation reservation = Reservation.builder()
                .reservationId(4L)
                .store(store)
                .visitDate(new Date())
                .reservationSlot(slot)
                .createdAt(new Date())
                .status("before")
                .build();

        given(ownerRepository.findById(ownerId)).willReturn(Optional.of(owner));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(reservationRepository.findWithSlotAndStoreByStoreId(storeId))
                .willReturn(List.of(reservation));

        ReservationResultDto result = reservationService.listReservationByStoreId(ownerId, storeId);

        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getReservationDtoList()).hasSize(1);
        assertThat(result.getReservationDtoList().get(0).getReservationId()).isEqualTo(4L);
        assertThat(result.getReservationDtoList().get(0).getStoreId()).isEqualTo(storeId);
        assertThat(result.getReservationDtoList().get(0).getSlotId()).isEqualTo(3L);
    }

    @Test
    void listReservationByStoreId_ownerNotFound() {
        Long ownerId = 1L, storeId = 2L;
        given(ownerRepository.findById(ownerId)).willReturn(Optional.empty());

        ReservationResultDto result = reservationService.listReservationByStoreId(ownerId, storeId);

        assertThat(result.getResult()).isEqualTo("Owner not found");
        assertThat(result.getReservationDtoList()).isNull();
    }

    @Test
    void listReservationByStoreId_noReservation() {
        Long ownerId = 1L, storeId = 2L;
        Owner owner = Owner.builder().ownerId(ownerId).build();
        Store store = Store.builder().storeId(storeId).owner(owner).build();

        given(ownerRepository.findById(ownerId)).willReturn(Optional.of(owner));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(reservationRepository.findWithSlotAndStoreByStoreId(storeId))
                .willReturn(Collections.emptyList());

        ReservationResultDto result = reservationService.listReservationByStoreId(ownerId, storeId);

        assertThat(result.getResult()).isEqualTo("no reservation");
        assertThat(result.getReservationDtoList()).isNull();
    }

    @Test
    void changeStatus_success() {
        Long ownerId = 1L, storeId = 2L, reservationId = 5L;
        Owner owner = Owner.builder().ownerId(ownerId).build();
        Store store = Store.builder().storeId(storeId).owner(owner).build();
        Reservation reservation = Reservation.builder()
                .reservationId(reservationId)
                .store(store)
                .status("before")
                .build();

        given(ownerRepository.findById(ownerId)).willReturn(Optional.of(owner));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(reservationRepository.findWithStoreByReservationId(reservationId))
                .willReturn(Optional.of(reservation));

        ReservationResultDto result = reservationService.changeStatus(ownerId, storeId, reservationId);

        assertThat(result.getResult()).isEqualTo("success");
        assertThat(reservation.getStatus()).isEqualTo("after");
        verify(reservationRepository).save(reservation);
    }

    @Test
    void changeStatus_ownerNotFound() {
        Long ownerId = 1L, storeId = 2L, reservationId = 5L;
        given(ownerRepository.findById(ownerId)).willReturn(Optional.empty());

        ReservationResultDto result = reservationService.changeStatus(ownerId, storeId, reservationId);

        assertThat(result.getResult()).isEqualTo("Owner not found");
    }

    @Test
    void deleteReservation_success() {
        Long ownerId = 1L, reservationId = 6L;
        Owner owner = Owner.builder().ownerId(ownerId).build();
        Store store = Store.builder().storeId(2L).owner(owner).build();
        Reservation reservation = Reservation.builder()
                .reservationId(reservationId)
                .store(store)
                .build();

        given(reservationRepository.findById(reservationId))
                .willReturn(Optional.of(reservation));

        ReservationResultDto result = reservationService.deleteReservation(ownerId, reservationId);

        assertThat(result.getResult()).isEqualTo("success");
        verify(reservationRepository).delete(reservation);
    }

    @Test
    void deleteReservation_notFound() {
        Long ownerId = 1L, reservationId = 6L;
        given(reservationRepository.findById(reservationId)).willReturn(Optional.empty());

        ReservationResultDto result = reservationService.deleteReservation(ownerId, reservationId);

        assertThat(result.getResult()).isEqualTo("fail");
    }

    @Test
    void deleteReservation_ownerMismatch() {
        Long ownerId = 1L, reservationId = 6L;
        Owner other = Owner.builder().ownerId(9L).build();
        Store store = Store.builder().storeId(2L).owner(other).build();
        Reservation reservation = Reservation.builder()
                .reservationId(reservationId)
                .store(store)
                .build();

        given(reservationRepository.findById(reservationId))
                .willReturn(Optional.of(reservation));

        ReservationResultDto result = reservationService.deleteReservation(ownerId, reservationId);

        assertThat(result.getResult()).isEqualTo("fail");
    }
}
