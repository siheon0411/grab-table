package com.team2.grabtable.domain.store.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDto {
    private int storeId;
    private int ownerId;
    private String name;
    private String location;
    private String type;
}
