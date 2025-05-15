package com.team2.grabtable.domain.reservation.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReservationResultDto {

    private String result;
    private ReservationDto reservationDto;
    private List<ReservationDto> reservationDtoList;
}
