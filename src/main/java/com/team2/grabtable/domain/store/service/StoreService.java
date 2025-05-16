package com.team2.grabtable.domain.store.service;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.store.dto.StoreImageDto;
import com.team2.grabtable.domain.store.dto.StoreRegisterDto;
import com.team2.grabtable.domain.store.dto.StoreResultDto;

import java.io.IOException;

public interface StoreService {

    StoreResultDto findStoresByOwnerId(OwnerDetails ownerDetails);

    StoreResultDto getStoreDetail(OwnerDetails ownerDetails, Long storeId);

//    StoreImageDto getStoreImage(OwnerDetails ownerDetails, Long storeId);

    StoreResultDto insertStore(OwnerDetails ownerDetails, StoreRegisterDto storeRegisterDto) throws IOException;

    StoreResultDto updateStore(OwnerDetails ownerDetails, Long storeId, StoreRegisterDto storeRegisterDto) throws IOException;

    StoreResultDto deleteStore(OwnerDetails ownerDetails, Long storeId);

//    StoreResultDto countStoresByOwnerId(Long ownerId);

}
