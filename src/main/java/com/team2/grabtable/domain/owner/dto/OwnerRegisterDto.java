package com.team2.grabtable.domain.owner.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerRegisterDto {
    private String email;
    private String password;
    private String name;
    private LocalDate birthdate;
}
