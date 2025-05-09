package com.team2.grabtable.domain.store.service;

import com.team2.grabtable.domain.owner.dto.OwnerDto;
import com.team2.grabtable.domain.store.dto.StoreDto;
import com.team2.grabtable.domain.store.dto.StoreImageDto;
import com.team2.grabtable.domain.store.dto.StoreResultDto;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public StoreResultDto findStoresByOwnerId(int ownerId) {
        StoreResultDto storeResultDto = new StoreResultDto();

        try {
            List<Store> storeList = storeRepository.findByOwnerId(ownerId);
            List<StoreDto> storeDtoList = new ArrayList<>();

            for (Store store : storeList) {

                StoreDto storeDto = StoreDto.builder()
                        .storeId(store.getStoreId())
                        .ownerId(store.getOwner().getOwnerId())
                        .name(store.getName())
                        .location(store.getLocation())
                        .type(store.getType())
                        .build();
                storeDtoList.add(storeDto);
            }

            storeResultDto.setStoreDtoList(storeDtoList);
            storeResultDto.setResult("success");

        } catch (Exception e) {
            e.printStackTrace();
            storeResultDto.setResult("fail");
        }

        return storeResultDto;
    }

    @Override
    public StoreResultDto getStoreDetail(int storeId) {
        StoreResultDto storeResultDto = new StoreResultDto();

        try {
            Optional<Store> storeList = storeRepository.findById(storeId);

            if (storeList.isPresent()) {
                Store store = storeList.get();

                StoreDto storeDto = StoreDto.builder()
                        .storeId(store.getStoreId())
                        .ownerId(store.getOwner().getOwnerId())
                        .name(store.getName())
                        .location(store.getLocation())
                        .type(store.getType())
                        .build();

                storeResultDto.setStoreDto(storeDto);
                storeResultDto.setResult("success");
            } else {
                storeResultDto.setResult("notfound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            storeResultDto.setResult("fail");
        }

        return storeResultDto;
    }

    @Override
    public StoreImageDto getStoreImage(int storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));
        return new StoreImageDto(store.getImage());
    }

    @Override
    public StoreResultDto insertStore(StoreDto storeDto, MultipartFile imageFile) throws IOException {
        StoreResultDto storeResultDto = new StoreResultDto();

        // todo: owner 정보 넣기

        Store store = Store.builder()
//                .owner(owner)
                .name(storeDto.getName())
                .location(storeDto.getLocation())
                .type(storeDto.getType())
                .image(imageFile.getBytes())
                .build();

        try {
            storeRepository.save(store);
            storeResultDto.setResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            storeResultDto.setResult("fail");
        }

        return storeResultDto;
    }

    @Override
    public StoreResultDto updateStore(StoreDto storeDto) {
        StoreResultDto storeResultDto = new StoreResultDto();

        // todo: owner 정보 넣기

        Store store = Store.builder()
                .storeId(storeDto.getStoreId())
//                .owner(owner)
                .name(storeDto.getName())
                .location(storeDto.getLocation())
                .type(storeDto.getType())
                .build();

        try {
            storeRepository.save(store);
            storeResultDto.setResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            storeResultDto.setResult("fail");
        }

        return storeResultDto;
    }

    @Override
    public StoreResultDto deleteStore(int storeId) {
        StoreResultDto storeResultDto = new StoreResultDto();

        try {
            storeRepository.deleteById(storeId);
            storeResultDto.setResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            storeResultDto.setResult("fail");
        }

        return storeResultDto;
    }

    @Override
    public StoreResultDto countStoresByOwnerId(int ownerId) {
        StoreResultDto storeResultDto = new StoreResultDto();

        try {
            Long count = storeRepository.countByOwnerId(ownerId);
            storeResultDto.setCount(count);
            storeResultDto.setResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            storeResultDto.setResult("fail");
        }

        return storeResultDto;
    }
}
