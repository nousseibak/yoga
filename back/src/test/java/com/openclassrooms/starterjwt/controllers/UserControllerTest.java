package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User mockUser;
    private LocalDateTime fixedDate = LocalDate.now().atStartOfDay();


    @BeforeEach
    public void setup() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setFirstName("test");
        mockUser.setLastName("test");
        mockUser.setCreatedAt(fixedDate);
        mockUser.setUpdatedAt(fixedDate);
        mockUser.setPassword("test");
    }

    @Test
    @WithMockUser(username = "test@example.com") // Simulate logged in user
    public void testFindById_ExistingUser() throws Exception {
        when(userService.findById(1L)).thenReturn(mockUser);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "test@example.com") // Simulate logged in user
    public void testFindById_NonExistingUser() throws Exception {
        when(userService.findById(2L)).thenReturn(null);

        mockMvc.perform(get("/api/user/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@example.com") // Simulate logged in user
    public void testDeleteUser_Authorized() throws Exception {
        when(userService.findById(1L)).thenReturn(mockUser);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "other@example.com") // Simulate different logged in user
    public void testDeleteUser_Unauthorized() throws Exception {
        when(userService.findById(1L)).thenReturn(mockUser);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

}
