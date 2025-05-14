package com.team2.grabtable.domain.store.service;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.store.dto.StoreDto;
import com.team2.grabtable.domain.store.dto.StoreImageDto;
import com.team2.grabtable.domain.store.dto.StoreRegisterDto;
import com.team2.grabtable.domain.store.dto.StoreResultDto;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public StoreResultDto findStoresByOwnerId(OwnerDetails ownerDetails) {
        StoreResultDto storeResultDto = new StoreResultDto();

        Long ownerId = ownerDetails.getOwner().getOwnerId();

        try {
            List<Store> storeList = storeRepository.findByOwnerId(ownerId);
            List<StoreDto> storeDtoList = new ArrayList<>();

            for (Store store : storeList) {

                StoreDto storeDto = StoreDto.builder()
                        .storeId(store.getStoreId())
                        .ownerId(ownerId)
                        .name(store.getName())
                        .location(store.getLocation())
                        .type(store.getType())
                        .image(store.getImage())
                        .imageContentType(store.getImageContentType())
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
    public StoreResultDto getStoreDetail(OwnerDetails ownerDetails, Long storeId) {
        StoreResultDto storeResultDto = new StoreResultDto();

        try {
            Optional<Store> optionalStore = storeRepository.findById(storeId);

            if (optionalStore.isPresent()) {
                Store store = optionalStore.get();
                if (store.getOwner().getOwnerId().equals(ownerDetails.getOwner().getOwnerId())) {
                    StoreDto storeDto = StoreDto.builder()
                            .storeId(store.getStoreId())
                            .ownerId(store.getOwner().getOwnerId())
                            .name(store.getName())
                            .location(store.getLocation())
                            .type(store.getType())
                            .image(store.getImage())
                            .imageContentType(store.getImageContentType())
                            .build();

                    storeResultDto.setStoreDto(storeDto);
                    storeResultDto.setResult("success");
                } else {
                    storeResultDto.setResult("no permission at " + storeId + " store");
                }
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
    public StoreImageDto getStoreImage(OwnerDetails ownerDetails, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));
        return new StoreImageDto(store.getImage(), store.getImageContentType());
    }

    @Override
    public StoreResultDto insertStore(OwnerDetails ownerDetails, StoreRegisterDto storeRegisterDto) throws IOException {
        StoreResultDto storeResultDto = new StoreResultDto();

        Store store = Store.builder()
                .owner(ownerDetails.getOwner())
                .name(storeRegisterDto.getName())
                .location(storeRegisterDto.getLocation())
                .type(storeRegisterDto.getType())
                .image(storeRegisterDto.getImageFile().getBytes())
                .imageContentType(storeRegisterDto.getImageFile().getContentType())
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
    public StoreResultDto updateStore(OwnerDetails ownerDetails, Long storeId, StoreRegisterDto storeRegisterDto) throws IOException {
        StoreResultDto storeResultDto = new StoreResultDto();

        Store updateStore = Store.builder()
                .storeId(storeId)
                .owner(ownerDetails.getOwner())
                .name(storeRegisterDto.getName())
                .location(storeRegisterDto.getLocation())
                .type(storeRegisterDto.getType())
                .image(storeRegisterDto.getImageFile().getBytes())
                .imageContentType(storeRegisterDto.getImageFile().getContentType())
                .build();

        try {

            Optional<Store> store = storeRepository.findById(storeId);

            if (store.isPresent()) {
                Store storeToDelete = store.get();
                if (storeToDelete.getOwner().getOwnerId().equals(ownerDetails.getOwner().getOwnerId())) {
                    storeRepository.save(updateStore);
                    storeResultDto.setResult("success");
                } else {
                    storeResultDto.setResult("no permission at " + storeId + "store");
                }
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
    public StoreResultDto deleteStore(OwnerDetails ownerDetails, Long storeId) {
        StoreResultDto storeResultDto = new StoreResultDto();

        try {

            Optional<Store> store = storeRepository.findById(storeId);

            if (store.isPresent()) {
                Store storeToDelete = store.get();
                if (storeToDelete.getOwner().getOwnerId().equals(ownerDetails.getOwner().getOwnerId())) {
                    storeRepository.deleteById(storeId);
                    storeResultDto.setResult("success");
                } else {
                    storeResultDto.setResult("no permission at " + storeId + "store");
                }
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
    public StoreResultDto countStoresByOwnerId(Long ownerId) {
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
