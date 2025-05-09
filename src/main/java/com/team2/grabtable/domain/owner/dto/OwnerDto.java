package com.team2.grabtable.domain.owner.dto;

import com.team2.grabtable.domain.store.dto.StoreDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class OwnerDto {
    private int ownerId;
    private String email;
    private String password;
    private String name;
    private Date birthdate;
    private Date createdAt;
}
