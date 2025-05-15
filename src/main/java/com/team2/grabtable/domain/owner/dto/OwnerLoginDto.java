package com.team2.grabtable.domain.owner.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerLoginDto {
    private String email;
    private String password;
}
