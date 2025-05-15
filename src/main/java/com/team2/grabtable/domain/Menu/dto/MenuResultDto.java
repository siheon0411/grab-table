package com.team2.grabtable.domain.Menu.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuResultDto {
    private String result;
    private MenuDto menuDto;
    private List<MenuDto> menuDtoList;
    private Long count;
}
