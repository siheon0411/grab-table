package com.team2.grabtable.domain.reservation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReservationDto {

    private Long reservationId;
    private Long storeId;
    private String visitDate;   // TODO: (임시) String으로 받아서 ServiceImpl에서 parse 해놨음

    private Long slotId;
    private String slotStartTime;

    private Date createdAt;

    private String status;
}
