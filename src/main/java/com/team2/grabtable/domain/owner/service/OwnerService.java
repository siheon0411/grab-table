package com.team2.grabtable.domain.owner.service;

import com.team2.grabtable.domain.owner.dto.OwnerDto;
import com.team2.grabtable.domain.owner.dto.OwnerRegisterDto;
import com.team2.grabtable.domain.owner.entity.Owner;

public interface OwnerService {
    OwnerDto register(OwnerRegisterDto dto);
    Owner findByEmail(String email);
}
