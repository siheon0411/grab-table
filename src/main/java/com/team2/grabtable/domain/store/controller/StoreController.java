package com.team2.grabtable.domain.store.controller;

import com.team2.grabtable.domain.store.dto.StoreDto;
import com.team2.grabtable.domain.store.dto.StoreImageDto;
import com.team2.grabtable.domain.store.dto.StoreResultDto;
import com.team2.grabtable.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<StoreResultDto> storeList(Integer ownerId) {
        return ResponseEntity.status(200).body(storeService.findStoresByOwnerId(ownerId));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResultDto> getStoreDetail(@PathVariable("storeId") int storeId) {
        return ResponseEntity.status(200).body(storeService.getStoreDetail(storeId));
    }

    @GetMapping("/{storeId}/image")
    public ResponseEntity<byte[]> getStoreImage(@PathVariable int storeId) {
        StoreImageDto img = storeService.getStoreImage(storeId);
        return ResponseEntity.status(200).body(img.getData());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoreResultDto> insertStore(@RequestPart StoreDto storeDto, @RequestPart MultipartFile imageFile) throws IOException {
        return ResponseEntity.status(200).body(storeService.insertStore(storeDto, imageFile));
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResultDto> updateStore(@PathVariable("storeId") int storeId, StoreDto storeDto) {
        return ResponseEntity.status(200).body(storeService.updateStore(storeDto));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<StoreResultDto> deleteStore(@PathVariable("storeId") int storeId) {
        return ResponseEntity.status(200).body(storeService.deleteStore(storeId));
    }
}
