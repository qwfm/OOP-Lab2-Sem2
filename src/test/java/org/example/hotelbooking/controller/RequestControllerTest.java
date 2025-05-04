package org.example.hotelbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelbooking.dto.RequestDTO;
import org.example.hotelbooking.dto.UserDTO;
import org.example.hotelbooking.service.RequestService;
import org.example.hotelbooking.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RequestControllerTest {
    @Autowired
    private MockMvc mvc;

    // Replace @MockBean with @MockitoBean
    @MockitoBean
    private RequestService requestService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void listAll_forAdmin_returnsJsonArray() throws Exception {
        UserDTO admin = new UserDTO(42L, "new user", "admin@example.com", "admin");
        given(userService.findOrCreate(any())).willReturn(admin);

        RequestDTO dto = new RequestDTO(
                1L,
                42L,
                "Standard",
                2,
                "PENDING",
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );
        given(requestService.getAll()).willReturn(List.of(dto));

        mvc.perform(get("/api/requests/all")
                        .header("Authorization", "Bearer dummy-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].roomType").value("Standard"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}