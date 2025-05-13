package com.team2.grabtable.domain.Menu.controller;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.Menu.dto.MenuImageDto;
import com.team2.grabtable.domain.Menu.dto.MenuRegisterDto;
import com.team2.grabtable.domain.Menu.dto.MenuResultDto;
import com.team2.grabtable.domain.Menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/stores/{storeId}/menus")
    public ResponseEntity<MenuResultDto> findByStoreId(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable("storeId") Long storeId) {
        return ResponseEntity.status(200).body(menuService.findByStoreId(ownerDetails, storeId));
    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<MenuResultDto> findByMenuId(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable Long menuId) {
        return ResponseEntity.status(200).body(menuService.findByMenuId(ownerDetails, menuId));
    }

    @GetMapping("/menus/{menuId}/image")
    public ResponseEntity<MenuImageDto> getMenuImage(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.getMenuImage(ownerDetails, menuId));
    }

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<MenuResultDto> insertMenu(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable Long storeId, @ModelAttribute MenuRegisterDto menuRegisterDto) throws IOException {
        return ResponseEntity.ok(menuService.insertMenu(ownerDetails, storeId, menuRegisterDto));
    }

    @PutMapping("/menus/{menuId}")
    public ResponseEntity<MenuResultDto> updateMenu(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable Long menuId, @ModelAttribute MenuRegisterDto menuRegisterDto) throws IOException {
        return ResponseEntity.ok(menuService.updateMenu(ownerDetails, menuId, menuRegisterDto));
    }

    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<MenuResultDto> deleteMenu(@AuthenticationPrincipal OwnerDetails ownerDetails, @PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.deleteMenu(ownerDetails, menuId));
    }
}
