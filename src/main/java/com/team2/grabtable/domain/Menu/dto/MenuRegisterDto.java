package com.team2.grabtable.domain.Menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuRegisterDto {
    private String name;
    private int price;
    private MultipartFile imageFile;
}
