package com.team2.grabtable.menu;

import com.team2.grabtable.config.MyAuthenticationFailureHandler;
import com.team2.grabtable.config.MyAuthenticationSuccessHandler;
import com.team2.grabtable.config.SecurityConfig;
import com.team2.grabtable.config.OwnerDetailsService;
import com.team2.grabtable.domain.Menu.controller.MenuController;
import com.team2.grabtable.domain.Menu.dto.MenuRegisterDto;
import com.team2.grabtable.domain.Menu.dto.MenuResultDto;
import com.team2.grabtable.domain.Menu.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MenuController.class)
@Import({SecurityConfig.class, MyAuthenticationSuccessHandler.class, MyAuthenticationFailureHandler.class})
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @MockBean
    private OwnerDetailsService ownerDetailsService;

    @Test
    void testFindByStoreId() throws Exception {
        Long storeId = 1L;
        MenuResultDto dto = new MenuResultDto();
        dto.setMenuDtoList(Collections.emptyList());
        dto.setResult("success");
        when(menuService.findByStoreId(any(), eq(storeId))).thenReturn(dto);

        mockMvc.perform(get("/api/admin/stores/{storeId}/menus", storeId)
                        .with(user("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.menuDtoList").isArray())
                .andExpect(jsonPath("$.menuDtoList").isEmpty());
    }

    @Test
    void testFindByMenuId() throws Exception {
        Long menuId = 2L;
        MenuResultDto dto = new MenuResultDto();
        dto.setResult("success");
        when(menuService.findByMenuId(any(), eq(menuId))).thenReturn(dto);

        mockMvc.perform(get("/api/admin/menus/{menuId}", menuId)
                        .with(user("admin").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testInsertMenu() throws Exception {
        Long storeId = 3L;
        MenuResultDto dto = new MenuResultDto();
        dto.setResult("success");
        when(menuService.insertMenu(any(), eq(storeId), any(MenuRegisterDto.class))).thenReturn(dto);

        MockMultipartFile file = new MockMultipartFile("imageFile", "test.png", "image/png", "data".getBytes());

        mockMvc.perform(multipart("/api/admin/stores/{storeId}/menus", storeId)
                        .file(file)
                        .param("name", "Pizza")
                        .param("price", "15000")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateMenu() throws Exception {
        Long menuId = 4L;
        MenuResultDto dto = new MenuResultDto();
        dto.setResult("success");
        when(menuService.updateMenu(any(), eq(menuId), any(MenuRegisterDto.class))).thenReturn(dto);

        MockMultipartFile file = new MockMultipartFile("imageFile", "update.png", "image/png", "data".getBytes());

        mockMvc.perform(multipart("/api/admin/menus/{menuId}", menuId)
                        .file(file)
                        .param("name", "Burger")
                        .param("price", "12000")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteMenu() throws Exception {
        Long menuId = 5L;
        MenuResultDto dto = new MenuResultDto();
        dto.setResult("success");
        when(menuService.deleteMenu(any(), eq(menuId))).thenReturn(dto);

        mockMvc.perform(delete("/api/admin/menus/{menuId}", menuId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
