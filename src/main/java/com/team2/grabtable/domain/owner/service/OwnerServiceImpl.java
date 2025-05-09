package com.team2.grabtable.domain.owner.service;

import com.team2.grabtable.domain.owner.dto.OwnerDto;
import com.team2.grabtable.domain.owner.dto.OwnerRegisterDto;
import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.owner.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OwnerDto register(OwnerRegisterDto dto) {

        if (ownerRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }

        Owner owner = Owner.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .birthdate(dto.getBirthdate())
                .build();

        Owner saved = ownerRepository.save(owner);

        return OwnerDto.builder()
                .ownerId(saved.getOwnerId())
                .email(saved.getEmail())
                .name(saved.getName())
                .birthdate(saved.getBirthdate())
                .createdAt(saved.getCreatedAt())
                .build();

    }

    @Override
    public Owner findByEmail(String email) {
        return ownerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
    }
}
