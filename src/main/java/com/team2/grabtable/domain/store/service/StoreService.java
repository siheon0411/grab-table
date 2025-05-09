package com.team2.grabtable.domain.store.service;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.store.dto.StoreDto;
import com.team2.grabtable.domain.store.dto.StoreImageDto;
import com.team2.grabtable.domain.store.dto.StoreResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoreService {

    StoreResultDto findStoresByOwnerId(OwnerDetails ownerDetails);

    StoreResultDto getStoreDetail(Long storeId);

    StoreImageDto getStoreImage(Long storeId);

    StoreResultDto insertStore(OwnerDetails ownerDetails, StoreDto storeDto, MultipartFile imageFile) throws IOException;

    StoreResultDto updateStore(OwnerDetails ownerDetails, StoreDto storeDto);

    StoreResultDto deleteStore(Long storeId);

    StoreResultDto countStoresByOwnerId(Long ownerId);

}
