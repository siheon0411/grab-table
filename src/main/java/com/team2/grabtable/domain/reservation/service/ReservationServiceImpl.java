package com.team2.grabtable.domain.reservation.service;

import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.owner.repository.OwnerRepository;
import com.team2.grabtable.domain.reservation.dto.ReservationDto;
import com.team2.grabtable.domain.reservation.dto.ReservationResultDto;
import com.team2.grabtable.domain.reservation.entity.Reservation;
import com.team2.grabtable.domain.reservation.entity.ReservationSlot;
import com.team2.grabtable.domain.reservation.repository.ReservationRepository;
import com.team2.grabtable.domain.reservation.repository.ReservationSlotRepository;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final OwnerRepository ownerRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationSlotRepository reservationSlotRepository;
    private final StoreRepository storeRepository;

    @Override
    public ReservationResultDto listReservationByStoreId(Long ownerId, Long storeId) {
        ReservationResultDto reservationResultDto = new ReservationResultDto();
        Optional<Owner> owner = ownerRepository.findById(ownerId);

        try {
            if (owner.isEmpty()) {
                reservationResultDto.setResult("Owner not found");
                return reservationResultDto;
            }

            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 매장입니다."));

            if (!store.getOwner().getOwnerId().equals(ownerId)) {
                throw new RuntimeException("당신의 매장이 아니어유.");
            }

            List<Reservation> reservationList = reservationRepository.findWithSlotAndStoreByStoreId(storeId);

            if (reservationList.isEmpty()) {
                reservationResultDto.setResult("no reservation");
                return reservationResultDto;
            }

            List<ReservationDto> reservationDtoList = reservationList.stream()
                    .map(res -> ReservationDto.builder()
                            .reservationId(res.getReservationId())
                            .storeId(res.getStore().getStoreId())
                            .visitDate(res.getVisitDate().toString())
                            .slotId(res.getReservationSlot().getSlotId())
                            .slotStartTime(res.getReservationSlot().getStartTime())
                            .createdAt(res.getCreatedAt())
                            .status(res.getStatus())
                            .build()
                    )
                    .toList();

            reservationResultDto.setResult("success");
            reservationResultDto.setReservationDtoList(reservationDtoList);

        } catch (Exception e) {
            e.printStackTrace();
            reservationResultDto.setResult("fail");
        }

        return reservationResultDto;
    }

    @Override
    public ReservationResultDto changeStatus(Long ownerId, Long storeId, Long reservationId) {
        ReservationResultDto reservationResultDto = new ReservationResultDto();
        Optional<Owner> owner = ownerRepository.findById(ownerId);

        try {
            if (owner.isEmpty()) {
                reservationResultDto.setResult("Owner not found");
                return reservationResultDto;
            }

            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 매장입니다."));

            if (!store.getOwner().getOwnerId().equals(ownerId)) {
                throw new RuntimeException("당신의 매장이 아니어유.");
            }

            Reservation reservation = reservationRepository.findWithStoreByReservationId(reservationId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 예약입니다."));

            if (!reservation.getStore().getStoreId().equals(storeId)) {
                throw new RuntimeException("예약이 해당 매장에 속하지 않습니다.");
            }

            if (!reservation.getStore().getOwner().getOwnerId().equals(ownerId)) {
                throw new AccessDeniedException("예약 접근 권한 없음! 당신네 가게 예약이 아니어유.");
            }

            // status 변경 "before" <-> "after"
            if (reservation.getStatus().equals("before")) {
                reservation.setStatus("after");     // before면 after로 변경
            } else {
                reservation.setStatus("before");    // after면 before로 변경
            }

            reservationRepository.save(reservation);
            reservationResultDto.setResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            reservationResultDto.setResult("fail");
        }

        return reservationResultDto;
    }

    @Override
    public ReservationResultDto deleteReservation(Long ownerId, Long reservationId) {
        ReservationResultDto reservationResultDto = new ReservationResultDto();

        try {
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 예약입니다."));

            if (!reservation.getStore().getOwner().getOwnerId().equals(ownerId)) {
                throw new AccessDeniedException("예약 접근 권한 없음! 당신네 가게 예약이 아니어유.");
            }

            reservationRepository.delete(reservation);
            reservationResultDto.setResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            reservationResultDto.setResult("fail");
        }

        return reservationResultDto;
    }
}
