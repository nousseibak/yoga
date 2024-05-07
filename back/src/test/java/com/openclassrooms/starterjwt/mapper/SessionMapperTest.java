package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper; // Inject the implementation, not the interface

    @Test
    public void testToEntity() {
        // Given
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Sample description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Collections.singletonList(2L));

        User mockUser = new User();
        mockUser.setId(2L);

        when(teacherService.findById(1L)).thenReturn(new Teacher()); // Simulate finding a teacher
        when(userService.findById(2L)).thenReturn(mockUser); // Simulate finding a user

        // When
        Session session = sessionMapper.toEntity(sessionDto);

        // Then
        assertEquals(sessionDto.getDescription(), session.getDescription());
    }

    @Test
    public void testToDto() {
        // Given
        Session session = new Session();
        session.setDescription("Sample description");
        session.setTeacher(new Teacher());
        session.getTeacher().setId(1L);
        session.setUsers(Collections.singletonList(new User()));

        // When
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Then
        assertEquals(session.getDescription(), sessionDto.getDescription());
    }

    @Test
    public void testToEntityList() {
        // Given
        List<SessionDto> sessionDtoList = Arrays.asList(
                createSessionDto("Description 1", 1L, Collections.singletonList(2L)),
                createSessionDto("Description 2", 3L, Arrays.asList(4L, 5L))
        );

        User mockUser1 = new User();
        mockUser1.setId(2L);
        User mockUser2 = new User();
        mockUser2.setId(4L);
        User mockUser3 = new User();
        mockUser3.setId(5L);

        when(teacherService.findById(1L)).thenReturn(new Teacher()); // Simulate finding a teacher
        when(teacherService.findById(3L)).thenReturn(new Teacher()); // Simulate finding another teacher
        when(userService.findById(2L)).thenReturn(mockUser1); // Simulate finding a user
        when(userService.findById(4L)).thenReturn(mockUser2); // Simulate finding another user
        when(userService.findById(5L)).thenReturn(mockUser3); // Simulate finding another user

        // When
        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        // Then
        assertEquals(sessionDtoList.size(), sessionList.size());
        assertEquals(sessionDtoList.get(0).getDescription(), sessionList.get(0).getDescription());
        assertEquals(sessionDtoList.get(1).getDescription(), sessionList.get(1).getDescription());
    }

    @Test
    public void testToDtoList() {
        // Given
        List<Session> sessionList = Arrays.asList(
                createSession("Description 1", new Teacher(), Arrays.asList(new User())),
                createSession("Description 2", new Teacher(), Arrays.asList(new User(), new User()))
        );

        // When
        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        // Then
        assertEquals(sessionList.size(), sessionDtoList.size());
        assertEquals(sessionList.get(0).getDescription(), sessionDtoList.get(0).getDescription());
        assertEquals(sessionList.get(1).getDescription(), sessionDtoList.get(1).getDescription());
    }

    private SessionDto createSessionDto(String description, Long teacherId, List<Long> userIds) {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription(description);
        sessionDto.setTeacher_id(teacherId);
        sessionDto.setUsers(userIds);
        return sessionDto;
    }

    private Session createSession(String description, Teacher teacher, List<User> users) {
        Session session = new Session();
        session.setDescription(description);
        session.setTeacher(teacher);
        session.setUsers(users);
        return session;
    }
}
