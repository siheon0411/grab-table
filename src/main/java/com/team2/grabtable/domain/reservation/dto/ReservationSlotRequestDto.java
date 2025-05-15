package com.team2.grabtable.domain.reservation.dto;

import com.team2.grabtable.domain.reservation.entity.ReservationSlot.MembershipLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSlotRequestDto {
    private Long storeId;
    private String startTime;
    private String endTime;
    private MembershipLevel allowedMembership;
}
