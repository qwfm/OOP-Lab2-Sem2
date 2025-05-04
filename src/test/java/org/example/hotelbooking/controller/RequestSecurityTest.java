package org.example.hotelbooking.controller;

import org.example.hotelbooking.controller.RequestController;
import org.example.hotelbooking.dto.RequestDTO;
import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.service.RequestService;
import org.example.hotelbooking.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
class RequestControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private RequestService requestService;

    @MockitoBean
    private UserService userService;

    private final RequestDTO sample = new RequestDTO(
            1L, 42L, "Standard", 2, "PENDING",
            LocalDate.now(), LocalDate.now().plusDays(1)
    );

    @Test
    @DisplayName("GET /api/requests/all — без JWT => 401")
    void listAll_noJwt_unauthorized() throws Exception {
        mvc.perform(get("/api/requests/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/requests/all — звичайний юзер => 403")
    void listAll_userForbidden() throws Exception {
        given(userService.findOrCreate(any()))
                .willReturn(new UserDTO(100L, "Bob", "bob@example.com", "client"));

        mvc.perform(get("/api/requests/all")
                        .with(jwt().authorities(() -> "ROLE_USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/requests/all — admin => 200 + JSON")
    void listAll_adminAllowed() throws Exception {
        UserDTO admin = new UserDTO(42L, "Admin", "admin@ex.com", "admin");
        given(userService.findOrCreate(any())).willReturn(admin);
        given(requestService.getAll()).willReturn(List.of(sample));

        mvc.perform(get("/api/requests/all")
                        .with(jwt().authorities(() -> "ROLE_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].roomType").value("Standard"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    @DisplayName("PUT /api/requests/1/confirm — admin => 200")
    void confirm_adminAllowed() throws Exception {
        UserDTO admin = new UserDTO(42L, "Admin", "admin@ex.com", "admin");
        given(userService.findOrCreate(any())).willReturn(admin);

        RequestDTO confirmed = new RequestDTO(
                sample.getId(),
                sample.getClientId(),
                sample.getRoomType(),
                sample.getGuests(),
                "CONFIRMED",
                sample.getCheckIn(),
                sample.getCheckOut()
        );
        given(requestService.confirm(1L)).willReturn(confirmed);

        mvc.perform(put("/api/requests/1/confirm")
                        .with(jwt().authorities(() -> "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("PUT /api/requests/1/reject — admin => 200")
    void reject_adminAllowed() throws Exception {
        UserDTO admin = new UserDTO(42L, "Admin", "admin@ex.com", "admin");
        given(userService.findOrCreate(any())).willReturn(admin);

        RequestDTO rejected = new RequestDTO(
                sample.getId(),
                sample.getClientId(),
                sample.getRoomType(),
                sample.getGuests(),
                "REJECTED",
                sample.getCheckIn(),
                sample.getCheckOut()
        );
        given(requestService.reject(1L)).willReturn(rejected);

        mvc.perform(put("/api/requests/1/reject")
                        .with(jwt().authorities(() -> "ROLE_ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    @DisplayName("PUT /api/requests/1/confirm — client => 403")
    void confirm_userForbidden() throws Exception {
        given(userService.findOrCreate(any()))
                .willReturn(new UserDTO(99L, "User", "u@ex.com", "client"));

        mvc.perform(put("/api/requests/1/confirm")
                        .with(jwt().authorities(() -> "ROLE_USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /api/requests/1/reject — client => 403")
    void reject_userForbidden() throws Exception {
        given(userService.findOrCreate(any()))
                .willReturn(new UserDTO(99L, "User", "u@ex.com", "client"));

        mvc.perform(put("/api/requests/1/reject")
                        .with(jwt().authorities(() -> "ROLE_USER")))
                .andExpect(status().isForbidden());
    }
}
