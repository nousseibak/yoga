
package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.JsonUtil;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
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
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.openclassrooms.starterjwt.JsonUtil.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"ADMIN"}) // pour simuler une connection sinon erreur 401
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    private Date fixedDate = new Date(1234567890123L);
    private User user1 = new User();
    private User user2 = new User();
    private java.util.List<User> userList = new ArrayList<User>();
    private Session mockSession;
    private SessionDto mockSessionDto;

    @BeforeEach
    public void setup() {
        userList.add(user1);
        userList.add(user2);
        mockSession = new Session();
        mockSession.setId(1L);
        mockSession.setDate(fixedDate);
        mockSession.setDescription("description");
        mockSession.setName("name");
        mockSession.setTeacher(new Teacher());
        mockSession.setUsers(userList);
        mockSessionDto = new SessionDto();
        mockSessionDto.setId(1L);
        mockSessionDto.setDate(fixedDate);
        mockSessionDto.setDescription("description");
        mockSessionDto.setName("name");
        mockSessionDto.setTeacher_id(10L);
        mockSessionDto.setUsers(new ArrayList<Long>(Arrays.asList(1L, 2L)));
    }


    @Test
    public void testFindById() throws Exception {
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sessionDto.getId()));
    }

    @Test
    public void testFindAll() throws Exception {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());

        List<SessionDto> sessionsDto=new ArrayList<>();
        sessionsDto.add(new SessionDto());
        sessionsDto.add(new SessionDto());

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionsDto);


        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(sessions.size()));
    }

    @Test
    @WithMockUser
    public void testCreate() throws Exception {
        SessionDto sessionDto = mockSessionDto;
        Session session = mockSession;
        when(sessionService.create(any())).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/api/session")
                                .content(asJsonString(sessionDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(sessionDto.getId())
                );
        verify(sessionService, times(1)).create(any());
    }

    @Test
    public void testUpdate() throws Exception {
        SessionDto sessionDto = mockSessionDto;
        Session session = mockSession;

        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put("/api/session/1")
                                .content(asJsonString(sessionDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(sessionDto.getId())
                );
        verify(sessionService, times(1)).update(1L,session);
    }


    @Test
    public void testParticipate() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testNoLongerParticipate() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/1"))
                .andExpect(status().isOk());
    }
}

