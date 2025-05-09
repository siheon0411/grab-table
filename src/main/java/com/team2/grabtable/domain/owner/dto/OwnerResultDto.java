package com.team2.grabtable.domain.owner.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResultDto {
    private String result;
    private OwnerDto data;
}
