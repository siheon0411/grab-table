package com.team2.grabtable.domain.Menu.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuRegisterDto {
    private String name;
    private int price;
    private MultipartFile imageFile;
}
