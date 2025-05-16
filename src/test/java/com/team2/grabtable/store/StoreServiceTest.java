package com.team2.grabtable.store;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.Menu.repository.MenuRepository;
import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.store.dto.StoreRegisterDto;
import com.team2.grabtable.domain.store.dto.StoreResultDto;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import com.team2.grabtable.domain.review.repository.ReviewRepository;
import com.team2.grabtable.domain.reservation.repository.ReservationRepository;
import com.team2.grabtable.domain.store.service.StoreServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    void findStoresByOwnerId_emptyList() {
        // Given
        OwnerDetails ownerDetails = mock(OwnerDetails.class);
        Owner owner = new Owner();
        owner.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(owner);
        when(storeRepository.findByOwnerId(owner.getOwnerId()))
                .thenReturn(Collections.emptyList());

        // When
        StoreResultDto result = storeService.findStoresByOwnerId(ownerDetails);

        // Then
        assertThat(result.getStoreDtoList()).isEmpty();
        assertThat(result.getResult()).isEqualTo("success");
        verify(storeRepository).findByOwnerId(owner.getOwnerId());
    }

    @Test
    void getStoreDetail_found() {
        // Given
        OwnerDetails ownerDetails = mock(OwnerDetails.class);
        Owner owner = new Owner();
        owner.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(owner);

        Long id = 1L;
        Store store = new Store();
        store.setStoreId(id);
        store.setOwner(owner);
        store.setName("Detail");
        when(storeRepository.findById(id)).thenReturn(Optional.of(store));

        // When
        StoreResultDto result = storeService.getStoreDetail(ownerDetails, id);

        // Then
        assertThat(result.getStoreDto()).isNotNull();
        assertThat(result.getStoreDto().getStoreId()).isEqualTo(id);
        assertThat(result.getStoreDto().getName()).isEqualTo("Detail");
        assertThat(result.getResult()).isEqualTo("success");
        verify(storeRepository).findById(id);
    }

    @Test
    void insertStore_success() throws IOException {
        // Given
        OwnerDetails ownerDetails = mock(OwnerDetails.class);
        Owner owner = new Owner();
        owner.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(owner);

        StoreRegisterDto registerDto = new StoreRegisterDto();
        registerDto.setName("TestStore");
        registerDto.setLocation("TestLocation");
        MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/png", "dummy-data".getBytes());
        registerDto.setImageFile(imageFile);

        // Stub saved entity with generated ID
        Store saved = new Store();
        saved.setStoreId(42L);
        saved.setOwner(owner);
        saved.setName("TestStore");
        saved.setLocation("TestLocation");
        when(storeRepository.save(any(Store.class))).thenReturn(saved);

        // When
        StoreResultDto result = storeService.insertStore(ownerDetails, registerDto);

        // Then
        assertThat(result.getResult()).isEqualTo("success");
        verify(storeRepository).save(argThat(s ->
                s.getName().equals("TestStore") && s.getOwner().equals(owner)
        ));
    }

    @Test
    void updateStore_success() throws IOException {
        // Given
        OwnerDetails ownerDetails = mock(OwnerDetails.class);
        Owner owner = new Owner();
        owner.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(owner);

        Long id = 2L;
        Store existing = new Store();
        existing.setStoreId(id);
        existing.setOwner(owner);
        existing.setName("OldName");
        when(storeRepository.findById(id)).thenReturn(Optional.of(existing));

        StoreRegisterDto updateDto = new StoreRegisterDto();
        updateDto.setName("NewName");
        updateDto.setLocation("NewLocation");
        MockMultipartFile updateFile = new MockMultipartFile("image", "update.png", "image/png", "dummy-data".getBytes());
        updateDto.setImageFile(updateFile);

        // When
        StoreResultDto result = storeService.updateStore(ownerDetails, id, updateDto);

        // Then
        assertThat(result.getResult()).isEqualTo("success");
        verify(storeRepository).findById(id);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    void deleteStore_success() {
        // Given
        OwnerDetails ownerDetails = mock(OwnerDetails.class);
        Owner owner = new Owner();
        owner.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(owner);

        Long id = 3L;
        Store existing = new Store();
        existing.setStoreId(id);
        existing.setOwner(owner);
        when(storeRepository.findById(id)).thenReturn(Optional.of(existing));

        when(menuRepository.findByStoreId(id)).thenReturn(Collections.emptyList());
        doNothing().when(menuRepository).deleteAll(any());
        when(reviewRepository.findByStoreId(id)).thenReturn(Collections.emptyList());
        doNothing().when(reviewRepository).deleteAll(any());
        when(reservationRepository.findByStoreStoreId(id)).thenReturn(Collections.emptyList());
        doNothing().when(reservationRepository).deleteAll(any());

        // When
        StoreResultDto result = storeService.deleteStore(ownerDetails, id);

        // Then
        assertThat(result.getResult()).isEqualTo("success");
        verify(menuRepository).findByStoreId(id);
        verify(menuRepository).deleteAll(Collections.emptyList());
        verify(reviewRepository).findByStoreId(id);
        verify(reviewRepository).deleteAll(Collections.emptyList());
        verify(reservationRepository).findByStoreStoreId(id);
        verify(reservationRepository).deleteAll(Collections.emptyList());
        verify(storeRepository).deleteById(eq(id));
    }
}