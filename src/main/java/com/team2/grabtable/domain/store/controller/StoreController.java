package com.team2.grabtable.domain.store.controller;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.store.dto.StoreImageDto;
import com.team2.grabtable.domain.store.dto.StoreRegisterDto;
import com.team2.grabtable.domain.store.dto.StoreResultDto;
import com.team2.grabtable.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<StoreResultDto> findStoresByOwnerId(@AuthenticationPrincipal OwnerDetails ownerDetails) {
        return ResponseEntity.status(200).body(storeService.findStoresByOwnerId(ownerDetails));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResultDto> getStoreDetail(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable("storeId") Long storeId) {
        return ResponseEntity.status(200).body(storeService.getStoreDetail(ownerDetails, storeId));
    }

    @GetMapping("/{storeId}/image")
    public ResponseEntity<byte[]> getStoreImage(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable Long storeId) {
        StoreImageDto img = storeService.getStoreImage(ownerDetails, storeId);
        return ResponseEntity.status(200).contentType(MediaType.parseMediaType(img.getContentType())).body(img.getData());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoreResultDto> insertStore(@AuthenticationPrincipal OwnerDetails ownerDetails, @ModelAttribute StoreRegisterDto storeRegisterDto) throws IOException {
        return ResponseEntity.status(200).body(storeService.insertStore(ownerDetails, storeRegisterDto));
    }

    @PutMapping(value = "/{storeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoreResultDto> updateStore(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable("storeId") Long storeId, @ModelAttribute StoreRegisterDto storeRegisterDto) throws IOException {
        return ResponseEntity.status(200).body(storeService.updateStore(ownerDetails, storeId, storeRegisterDto));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<StoreResultDto> deleteStore(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable("storeId") Long storeId) {
        return ResponseEntity.status(200).body(storeService.deleteStore(ownerDetails, storeId));
    }
}
