package com.team2.grabtable.domain.Menu.service;

import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.Menu.dto.MenuImageDto;
import com.team2.grabtable.domain.Menu.dto.MenuRegisterDto;
import com.team2.grabtable.domain.Menu.dto.MenuResultDto;

import java.io.IOException;


public interface MenuService {
    MenuResultDto findByStoreId(OwnerDetails ownerDetails, Long storeId);
    MenuResultDto findByMenuId(OwnerDetails ownerDetails, Long menuId);
    MenuImageDto getMenuImage(OwnerDetails ownerDetails, Long menuId);
    MenuResultDto insertMenu(OwnerDetails ownerDetails, Long storeId, MenuRegisterDto menuRegisterDto) throws IOException;
    MenuResultDto updateMenu(OwnerDetails ownerDetails, Long menuId, MenuRegisterDto menuRegisterDto) throws IOException;
    MenuResultDto deleteMenu(OwnerDetails ownerDetails, Long menuId);
}
