package com.team2.grabtable.owner;

import com.team2.grabtable.domain.owner.dto.OwnerDto;
import com.team2.grabtable.domain.owner.dto.OwnerRegisterDto;
import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.owner.repository.OwnerRepository;
import com.team2.grabtable.domain.owner.service.OwnerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OwnerServiceImpl ownerService;

    private OwnerRegisterDto registerDto;
    private Owner savedOwner;

    @BeforeEach
    void setUp() {
        registerDto = OwnerRegisterDto.builder()
                .email("user@example.com")
                .password("plainpass")
                .name("User")
                .birthdate(LocalDate.of(1995, 5, 15))
                .build();

        savedOwner = Owner.builder()
                .ownerId(100L)
                .email(registerDto.getEmail())
                .password("encoded")
                .name(registerDto.getName())
                .birthdate(registerDto.getBirthdate())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void register_success() {
        // Given
        when(ownerRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encoded");
        when(ownerRepository.save(any(Owner.class))).thenReturn(savedOwner);

        // When
        OwnerDto result = ownerService.register(registerDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOwnerId()).isEqualTo(100L);
        assertThat(result.getEmail()).isEqualTo(registerDto.getEmail());
        assertThat(result.getName()).isEqualTo(registerDto.getName());
        verify(ownerRepository).existsByEmail(registerDto.getEmail());
        verify(passwordEncoder).encode(registerDto.getPassword());
        verify(ownerRepository).save(any(Owner.class));
    }

    @Test
    void register_duplicateEmail_throws() {
        // Given
        when(ownerRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        // When / Then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ownerService.register(registerDto));
        assertThat(ex.getMessage()).isEqualTo("Email already in use");
        verify(ownerRepository).existsByEmail(registerDto.getEmail());
        verify(ownerRepository, never()).save(any());
    }

    @Test
    void findByEmail_success() {
        // Given
        Owner owner = Owner.builder()
                .ownerId(200L)
                .email("find@example.com")
                .build();
        when(ownerRepository.findByEmail("find@example.com")).thenReturn(Optional.of(owner));

        // When
        Owner result = ownerService.findByEmail("find@example.com");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOwnerId()).isEqualTo(200L);
        verify(ownerRepository).findByEmail("find@example.com");
    }

    @Test
    void findByEmail_notFound_throws() {
        // Given
        when(ownerRepository.findByEmail("none@example.com")).thenReturn(Optional.empty());

        // When / Then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ownerService.findByEmail("none@example.com"));
        assertThat(ex.getMessage()).isEqualTo("Email not found");
        verify(ownerRepository).findByEmail("none@example.com");
    }
}
