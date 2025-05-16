package com.team2.grabtable.menu;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.Menu.dto.MenuDto;
import com.team2.grabtable.domain.Menu.dto.MenuRegisterDto;
import com.team2.grabtable.domain.Menu.dto.MenuResultDto;
import com.team2.grabtable.domain.Menu.entity.Menu;
import com.team2.grabtable.domain.Menu.repository.MenuRepository;
import com.team2.grabtable.domain.Menu.service.MenuServiceImpl;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MenuServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OwnerDetails ownerDetails;

    @InjectMocks
    private MenuServiceImpl menuService;

    @Test
    void findByStoreId_success() {
        long storeId = 1L;
        // given owner
        com.team2.grabtable.domain.owner.entity.Owner user = new com.team2.grabtable.domain.owner.entity.Owner();
        user.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(user);
        // given store
        Store store = new Store(); store.setStoreId(storeId); store.setOwner(user);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        // given menus
        Menu menu = Menu.builder()
                .menuId(10L)
                .store(store)
                .name("Test")
                .price(1000)
                .image(new byte[0])
                .imageContentType("img/png")
                .build();
        when(menuRepository.findByStoreId(storeId)).thenReturn(Collections.singletonList(menu));

        MenuResultDto result = menuService.findByStoreId(ownerDetails, storeId);

        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getMenuDtoList()).hasSize(1);
        assertThat(result.getMenuDtoList().get(0).getMenuId()).isEqualTo(10L);
    }

    @Test
    void findByStoreId_noPermission() {
        long storeId = 2L;
        com.team2.grabtable.domain.owner.entity.Owner user = new com.team2.grabtable.domain.owner.entity.Owner(); user.setOwnerId(99L);
        when(ownerDetails.getOwner()).thenReturn(user);
        Store store = new Store(); store.setStoreId(storeId);
        com.team2.grabtable.domain.owner.entity.Owner owner = new com.team2.grabtable.domain.owner.entity.Owner(); owner.setOwnerId(1L);
        store.setOwner(owner);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        MenuResultDto result = menuService.findByStoreId(ownerDetails, storeId);
        assertThat(result.getResult()).startsWith("no permission at " + storeId + " store");
    }

    @Test
    void findByStoreId_notFound() {
        when(storeRepository.findById(3L)).thenReturn(Optional.empty());
        MenuResultDto result = menuService.findByStoreId(ownerDetails, 3L);
        assertThat(result.getResult()).isEqualTo("notfound");
    }

    @Test
    void findByMenuId_success() {
        long menuId = 5L;
        com.team2.grabtable.domain.owner.entity.Owner user = new com.team2.grabtable.domain.owner.entity.Owner(); user.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(user);
        Store store = new Store(); store.setOwner(user);
        Menu menu = Menu.builder().menuId(menuId).store(store)
                .name("M").price(500)
                .image(new byte[0]).imageContentType("img/png").build();
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        MenuResultDto result = menuService.findByMenuId(ownerDetails, menuId);
        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getMenuDto().getMenuId()).isEqualTo(menuId);
    }

    @Test
    void findByMenuId_notFound() {
        when(menuRepository.findById(6L)).thenReturn(Optional.empty());
        MenuResultDto result = menuService.findByMenuId(ownerDetails, 6L);
        assertThat(result.getResult()).isEqualTo("notfound");
    }

    @Test
    void insertMenu_success() throws IOException {
        long storeId = 7L;
        com.team2.grabtable.domain.owner.entity.Owner user = new com.team2.grabtable.domain.owner.entity.Owner(); user.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(user);
        Store store = new Store(); store.setStoreId(storeId); store.setOwner(user);
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        MenuRegisterDto dto = new MenuRegisterDto("X", 100, new MockMultipartFile("f","fn","img/png","b".getBytes()));
        when(menuRepository.save(any(Menu.class))).thenAnswer(i -> i.getArgument(0));

        MenuResultDto result = menuService.insertMenu(ownerDetails, storeId, dto);
        assertThat(result.getResult()).isEqualTo("success");
    }

    @Test
    void insertMenu_notFound() throws IOException {
        when(storeRepository.findById(8L)).thenReturn(Optional.empty());
        MenuResultDto result = menuService.insertMenu(ownerDetails, 8L, new MenuRegisterDto("A",1,new MockMultipartFile("f","","","".getBytes())));
        assertThat(result.getResult()).isEqualTo("notfound");
    }

    @Test
    void updateMenu_success() throws IOException {
        long menuId = 9L;
        com.team2.grabtable.domain.owner.entity.Owner user = new com.team2.grabtable.domain.owner.entity.Owner(); user.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(user);
        Store store = new Store(); store.setOwner(user);
        Menu menu = Menu.builder().menuId(menuId).store(store).build();
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(Menu.class))).thenAnswer(i -> i.getArgument(0));

        MenuResultDto result = menuService.updateMenu(ownerDetails, menuId, new MenuRegisterDto("B",2,new MockMultipartFile("f","","","".getBytes())));
        assertThat(result.getResult()).isEqualTo("success");
    }

    @Test
    void updateMenu_notFound() throws IOException {
        when(menuRepository.findById(10L)).thenReturn(Optional.empty());
        MenuResultDto result = menuService.updateMenu(ownerDetails, 10L, new MenuRegisterDto("C",3,new MockMultipartFile("f","","","".getBytes())));
        assertThat(result.getResult()).isEqualTo("notfound");
    }

    @Test
    void deleteMenu_success() {
        long menuId = 11L;
        com.team2.grabtable.domain.owner.entity.Owner user = new com.team2.grabtable.domain.owner.entity.Owner(); user.setOwnerId(1L);
        when(ownerDetails.getOwner()).thenReturn(user);
        Store store = new Store(); store.setOwner(user);
        Menu menu = Menu.builder().menuId(menuId).store(store).build();
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        MenuResultDto result = menuService.deleteMenu(ownerDetails, menuId);
        assertThat(result.getResult()).isEqualTo("success");
    }

    @Test
    void deleteMenu_notFound() {
        when(menuRepository.findById(12L)).thenReturn(Optional.empty());
        MenuResultDto result = menuService.deleteMenu(ownerDetails, 12L);
        assertThat(result.getResult()).isEqualTo("notfound");
    }
}
