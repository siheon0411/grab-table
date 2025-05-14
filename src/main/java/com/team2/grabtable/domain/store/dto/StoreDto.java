package com.team2.grabtable.domain.store.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDto {
    private Long storeId;
    private Long ownerId;
    private String name;
    private String location;
    private String type;

    private byte[] image;
    private String imageContentType;
}
