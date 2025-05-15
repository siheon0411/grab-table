package com.team2.grabtable.domain.reservation.controller;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.reservation.dto.ReservationSlotRequestDto;
import com.team2.grabtable.domain.reservation.dto.ReservationSlotResponseDto;
import com.team2.grabtable.domain.reservation.service.ReservationSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner/slots")
@RequiredArgsConstructor
public class ReservationSlotController {

    private final ReservationSlotService slotService;

    @PostMapping
    public ResponseEntity<ReservationSlotResponseDto> createSlot(
            @RequestBody ReservationSlotRequestDto dto,
            @AuthenticationPrincipal OwnerDetails ownerDetails) {
        Long ownerId = ownerDetails.getOwner().getOwnerId();
        return ResponseEntity.ok(slotService.createSlot(ownerId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ReservationSlotResponseDto>> getSlots(
            @RequestParam Long storeId,
            @AuthenticationPrincipal OwnerDetails ownerDetails) {
        Long ownerId = ownerDetails.getOwner().getOwnerId();
        return ResponseEntity.ok(slotService.getSlotsByStore(ownerId, storeId));
    }

    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> deleteSlot(
            @PathVariable Long slotId,
            @AuthenticationPrincipal OwnerDetails ownerDetails) {
        Long ownerId = ownerDetails.getOwner().getOwnerId();
        slotService.deleteSlot(ownerId, slotId);
        return ResponseEntity.noContent().build();
    }
}
