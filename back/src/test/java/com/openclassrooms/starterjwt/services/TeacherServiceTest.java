package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("John", "Doe"));
        teachers.add(new Teacher("Jane", "Doe"));
        
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> foundTeachers = teacherService.findAll();

        assertNotNull(foundTeachers);
        assertEquals(2, foundTeachers.size());

        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void testFindTeacherById_ExistingId() {
        Long id = 1L;
        Teacher teacher = new Teacher("John", "Doe");

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher foundTeacher = teacherService.findById(id);

        assertNotNull(foundTeacher);
        assertEquals(teacher, foundTeacher);

        verify(teacherRepository, times(1)).findById(id);
    }

    @Test
    public void testFindTeacherById_NonExistingId() {
        Long id = 999L;

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        Teacher foundTeacher = teacherService.findById(id);

        assertNull(foundTeacher);

        verify(teacherRepository, times(1)).findById(id);
    }
}
