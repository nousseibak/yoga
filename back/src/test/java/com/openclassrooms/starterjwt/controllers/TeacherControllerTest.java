package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher mockTeacher1;
    private Teacher mockTeacher2;

    @BeforeEach
    public void setup() {
        mockTeacher1 = new Teacher();
        mockTeacher1.setId(1L);
        mockTeacher1.setLastName("Teacher One");
        mockTeacher1.setFirstName("Teacher One");

        mockTeacher2 = new Teacher();
        mockTeacher2.setId(2L);
        mockTeacher2.setLastName("Teacher Two");
        mockTeacher2.setFirstName("Teacher Two");

    }

    @Test
    @WithMockUser(username = "test@example.com") // Simulate logged in user
    public void testFindById_ExistingTeacher() throws Exception {
        when(teacherService.findById(1L)).thenReturn(mockTeacher1);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Teacher One"));
    }

    @Test
    @WithMockUser(username = "test@example.com") // Simulate logged in user
    public void testFindById_NonExistingTeacher() throws Exception {
        when(teacherService.findById(3L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@example.com") // Simulate logged in user
    public void testFindAll() throws Exception {
        List<Teacher> teachers = Arrays.asList(mockTeacher1, mockTeacher2);
        when(teacherService.findAll()).thenReturn(teachers);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Teacher One"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Teacher Two"));
    }
}
