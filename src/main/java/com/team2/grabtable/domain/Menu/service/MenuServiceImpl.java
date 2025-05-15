package com.team2.grabtable.domain.Menu.service;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.Menu.dto.MenuDto;
import com.team2.grabtable.domain.Menu.dto.MenuImageDto;
import com.team2.grabtable.domain.Menu.dto.MenuRegisterDto;
import com.team2.grabtable.domain.Menu.dto.MenuResultDto;
import com.team2.grabtable.domain.Menu.entity.Menu;
import com.team2.grabtable.domain.Menu.repository.MenuRepository;
import com.team2.grabtable.domain.store.entity.Store;
import com.team2.grabtable.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Override
    public MenuResultDto findByStoreId(OwnerDetails ownerDetails, Long storeId) {
        MenuResultDto menuResultDto = new MenuResultDto();

        try {

            Optional<Store> optionalStore = storeRepository.findById(storeId);

            if (optionalStore.isPresent()) {
                Store store = optionalStore.get();
                if (store.getOwner().getOwnerId().equals(ownerDetails.getOwner().getOwnerId())) {
                    List<Menu> menuList = menuRepository.findByStoreId(storeId);
                    List<MenuDto> menuDtoList = new ArrayList<>();

                    for (Menu menu : menuList) {
                        MenuDto menuDto = MenuDto.builder()
                                .menuId(menu.getMenuId())
                                .storeId(menu.getStore().getStoreId())
                                .name(menu.getName())
                                .price(menu.getPrice())
                                .image(menu.getImage())
                                .imageContentType(menu.getImageContentType())
                                .build();
                        menuDtoList.add(menuDto);
                    }
                    menuResultDto.setMenuDtoList(menuDtoList);
                    menuResultDto.setResult("success");
                } else {
                    menuResultDto.setResult("no permission at " + storeId + " store");
                    return menuResultDto;
                }

            } else {
                menuResultDto.setResult("notfound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            menuResultDto.setResult("fail");
        }

        return menuResultDto;
    }

    @Override
    public MenuResultDto findByMenuId(OwnerDetails ownerDetails, Long menuId) {
        MenuResultDto menuResultDto = new MenuResultDto();

        try {

            Optional<Menu> optionalMenu = menuRepository.findById(menuId);

            if (optionalMenu.isPresent()) {
                Menu menu = optionalMenu.get();
                if (menu.getStore().getOwner().getOwnerId().equals(ownerDetails.getOwner().getOwnerId())) {
                    MenuDto menuDto = MenuDto.builder()
                            .menuId(menu.getMenuId())
                            .storeId(menu.getStore().getStoreId())
                            .name(menu.getName())
                            .price(menu.getPrice())
                            .image(menu.getImage())
                            .imageContentType(menu.getImageContentType())
                            .build();
                    menuResultDto.setMenuDto(menuDto);
                    menuResultDto.setResult("success");
                } else {
                    menuResultDto.setResult("no permission at " + menuId + " store");
                    return menuResultDto;
                }
            } else {
                menuResultDto.setResult("notfound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            menuResultDto.setResult("fail");
        }

        return menuResultDto;
    }

    @Override
    public MenuImageDto getMenuImage(OwnerDetails ownerDetails, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("menu not found"));
        return new MenuImageDto(menu.getImage(), menu.getImageContentType());
    }

    @Override
    public MenuResultDto insertMenu(OwnerDetails ownerDetails, Long storeId, MenuRegisterDto menuRegisterDto) throws IOException {
        MenuResultDto menuResultDto = new MenuResultDto();

        try {
            Optional<Store> optionalStore = storeRepository.findById(storeId);

            if (optionalStore.isPresent()) {
                Store store = optionalStore.get();
                if (store.getOwner().getOwnerId().equals(ownerDetails.getOwner().getOwnerId())) {
                    Menu menu = Menu.builder()
                            .store(store)
                            .name(menuRegisterDto.getName())
                            .price(menuRegisterDto.getPrice())
                            .image(menuRegisterDto.getImageFile().getBytes())
                            .imageContentType(menuRegisterDto.getImageFile().getContentType())
                            .build();

                    menuRepository.save(menu);
                    menuResultDto.setResult("success");
                } else {
                    menuResultDto.setResult("no permission at " + storeId + " store");
                }
            } else {
                menuResultDto.setResult("notfound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            menuResultDto.setResult("fail");
        }

        return menuResultDto;
    }

    @Override
    public MenuResultDto updateMenu(OwnerDetails ownerDetails, Long menuId, MenuRegisterDto menuRegisterDto) throws IOException {
        MenuResultDto menuResultDto = new MenuResultDto();

        try {
            Optional<Menu> optionalMenu = menuRepository.findById(menuId);

            if (optionalMenu.isPresent()) {
                Menu menu = optionalMenu.get();
                if (menu.getStore().getOwner().getOwnerId().equals(ownerDetails.getOwner().getOwnerId())) {
                    menu.setName(menuRegisterDto.getName());
                    menu.setPrice(menuRegisterDto.getPrice());
                    if (menu.getImage() != null) {
                        menu.setImage(menuRegisterDto.getImageFile().getBytes());
                        menu.setImageContentType(menuRegisterDto.getImageFile().getContentType());
                    }
                    menuRepository.save(menu);
                    menuResultDto.setResult("success");
                } else {
                    menuResultDto.setResult("no permission at " + menuId + " store");
                }
            } else {
                menuResultDto.setResult("notfound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            menuResultDto.setResult("fail");
        }

        return menuResultDto;
    }

    @Override
    public MenuResultDto deleteMenu(OwnerDetails ownerDetails, Long menuId) {
        MenuResultDto menuResultDto = new MenuResultDto();

        try {
            Optional<Menu> optionalMenu = menuRepository.findById(menuId);

            if (optionalMenu.isPresent()) {
                Menu menu = optionalMenu.get();
                if (menu.getStore().getOwner().getOwnerId().equals(ownerDetails.getOwner().getOwnerId())) {
                    menuRepository.delete(menu);
                    menuResultDto.setResult("success");
                } else {
                    menuResultDto.setResult("no permission at " + menuId + " store");
                }
            } else {
                menuResultDto.setResult("notfound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            menuResultDto.setResult("fail");
        }

        return menuResultDto;
    }
}
