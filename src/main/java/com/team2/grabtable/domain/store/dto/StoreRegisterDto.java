package com.team2.grabtable.domain.store.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRegisterDto {
    private Long ownerId;
    private String name;
    private String location;
    private String type;
    private MultipartFile imageFile;
}
