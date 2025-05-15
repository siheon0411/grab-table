package com.team2.grabtable.domain.Menu.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class MenuRegisterDto {
    private String name;
    private int price;
    private MultipartFile imageFile;
}
