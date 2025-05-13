package com.team2.grabtable.domain.store.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class StoreRegisterDto {
    private Long ownerId;
    private String name;
    private String location;
    private String type;
    private MultipartFile imageFile;
}
