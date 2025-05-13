package com.team2.grabtable.domain.Menu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuImageDto {
    private byte[] data;
    private String contentType;
}
