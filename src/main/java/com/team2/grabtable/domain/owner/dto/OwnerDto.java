package com.team2.grabtable.domain.owner.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerDto {
    private Long ownerId;
    private String email;
    private String name;
    private LocalDate birthdate;
    private LocalDateTime createdAt;
}

