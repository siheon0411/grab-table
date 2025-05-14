package com.team2.grabtable.domain.reservation.controller;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.reservation.dto.ReservationResultDto;
import com.team2.grabtable.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation/{storeId}")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ReservationResultDto> listReservationByStoreId(
            @AuthenticationPrincipal OwnerDetails ownerDetails,
            @PathVariable Long storeId
    ) {
        Long ownerId = ownerDetails.getOwner().getOwnerId();

        ReservationResultDto reservationResultDto = reservationService.listReservationByStoreId(ownerId, storeId);

        return ResponseEntity.ok(reservationResultDto);
    }


    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ReservationResultDto> deleteReservation(
            @AuthenticationPrincipal OwnerDetails ownerDetails,
            @PathVariable Long storeId,
            @PathVariable Long reservationId
    ) {
        Long ownerId = ownerDetails.getOwner().getOwnerId();

        ReservationResultDto reservationResultDto = reservationService.deleteReservation(ownerId, reservationId);

        return ResponseEntity.ok(reservationResultDto);
    }
    
}
