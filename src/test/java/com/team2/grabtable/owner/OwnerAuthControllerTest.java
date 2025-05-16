package com.team2.grabtable.owner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.grabtable.config.MyAuthenticationFailureHandler;
import com.team2.grabtable.config.MyAuthenticationSuccessHandler;
import com.team2.grabtable.config.OwnerDetailsService;
import com.team2.grabtable.config.SecurityConfig;
import com.team2.grabtable.config.OwnerDetails;
import com.team2.grabtable.domain.owner.controller.OwnerAuthController;
import com.team2.grabtable.domain.owner.dto.OwnerDto;
import com.team2.grabtable.domain.owner.dto.OwnerRegisterDto;
import com.team2.grabtable.domain.owner.dto.OwnerResultDto;
import com.team2.grabtable.domain.owner.entity.Owner;
import com.team2.grabtable.domain.owner.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OwnerAuthController.class)
@Import({SecurityConfig.class, MyAuthenticationSuccessHandler.class, MyAuthenticationFailureHandler.class})
public class OwnerAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @MockBean
    private OwnerDetailsService ownerDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_success() throws Exception {
        OwnerRegisterDto registerDto = OwnerRegisterDto.builder()
                .email("test@example.com")
                .password("pass123")
                .name("Tester")
                .birthdate(LocalDate.of(1990, 1, 1))
                .build();
        OwnerDto ownerDto = OwnerDto.builder()
                .ownerId(5L)
                .email("test@example.com")
                .name("Tester")
                .birthdate(registerDto.getBirthdate())
                .createdAt(LocalDateTime.now())
                .build();
        when(ownerService.register(any(OwnerRegisterDto.class))).thenReturn(ownerDto);

        mockMvc.perform(post("/owner/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("success"))
                .andExpect(jsonPath("$.data.ownerId").value(5));
    }

    @Test
    void register_failure_duplicateEmail() throws Exception {
        OwnerRegisterDto registerDto = OwnerRegisterDto.builder()
                .email("dup@example.com")
                .password("pass")
                .name("Dup")
                .birthdate(LocalDate.of(2000, 2, 2))
                .build();
        doThrow(new IllegalArgumentException("Email already in use")).when(ownerService).register(any());

        mockMvc.perform(post("/owner/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("fail"));
    }

    @Test
    void getCurrentOwner_success() throws Exception {
        Owner owner = Owner.builder()
                .ownerId(7L)
                .email("cur@example.com")
                .name("Current")
                .birthdate(LocalDate.of(1985, 5, 5))
                .createdAt(LocalDateTime.now())
                .build();
        OwnerDetails ownerDetails = new OwnerDetails(owner);

        mockMvc.perform(get("/owner/about")
                        .with(user(ownerDetails))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"))
                .andExpect(jsonPath("$.data.ownerId").value(7));
    }

    @Test
    void getCurrentOwner_unauthenticated() throws Exception {
        mockMvc.perform(get("/owner/about")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login.html"));
    }
}
