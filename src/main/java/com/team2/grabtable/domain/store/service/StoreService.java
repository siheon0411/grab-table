package com.team2.grabtable.domain.store.service;

import com.team2.grabtable.domain.store.dto.StoreDto;
import com.team2.grabtable.domain.store.dto.StoreImageDto;
import com.team2.grabtable.domain.store.dto.StoreResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoreService {

    StoreResultDto findStoresByOwnerId(int ownerId);

    StoreResultDto getStoreDetail(int storeId);

    StoreImageDto getStoreImage(int storeId);

    StoreResultDto insertStore(StoreDto storeDto, MultipartFile imageFile) throws IOException;

    StoreResultDto updateStore(StoreDto storeDto);

    StoreResultDto deleteStore(int storeId);

    StoreResultDto countStoresByOwnerId(int ownerId);

}
