// OwnerAuthController
package com.team2.grabtable.domain.owner.controller;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.owner.dto.OwnerDto;
import com.team2.grabtable.domain.owner.dto.OwnerRegisterDto;
import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerAuthController {

    private final OwnerService ownerService;

    @PostMapping("/register")
    public ResponseEntity<OwnerDto> register(@RequestBody OwnerRegisterDto dto) {
        OwnerDto result = ownerService.register(dto);
        return ResponseEntity.ok(result);
    }

    // 세션 기반 인증 동작 테스트!
    // 로그인된 사용자의 정보를 가져와 OwnerDto 형태로 반환
    // @AuthenticationPrincipal: Spring Security에서 현재 인증ㄷ된 사용자 정보를 컨트롤러 파라미터로 주입할 때 사용
    // 사용자가 로그인하면 SecurityContextHolder에 인증 객체가 저장됨
    // UserDetails 구현체인 OwnerDetails를 가져옴
    @GetMapping("/about")
    public OwnerDto getCurrentOwner(@AuthenticationPrincipal OwnerDetails ownerDetails) {
        Owner owner = ownerDetails.getOwner();

        return OwnerDto.builder()
                .ownerId(owner.getOwnerId())
                .email(owner.getEmail())
                .name(owner.getName())
                .birthdate(owner.getBirthdate())
                .createdAt(owner.getCreatedAt())
                .build();
    }

}