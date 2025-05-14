package com.team2.grabtable.domain.reservation.service;


import com.team2.grabtable.domain.reservation.dto.ReservationDto;
import com.team2.grabtable.domain.reservation.dto.ReservationResultDto;

import java.util.List;

public interface ReservationService {
    ReservationResultDto listReservationByStoreId(Long ownerId, Long storeId);

    ReservationResultDto deleteReservation(Long ownerId, Long reservationId);
}
