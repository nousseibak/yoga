package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TeacherMapperTest {

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    public void testToEntity() {
        // Given
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John Doe");

        // When
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Then
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
    }

    @Test
    public void testToDto() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John Doe");

        // When
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Then
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
    }

    @Test
    public void testToEntityList() {
        // Given
        List<TeacherDto> teacherDtoList = Arrays.asList(
                createTeacherDto(1L, "John Doe",""),
                createTeacherDto(2L, "Jane Smith","")
        );

        // When
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // Then
        assertEquals(teacherDtoList.size(), teacherList.size());
        assertEquals(teacherDtoList.get(0).getId(), teacherList.get(0).getId());
        assertEquals(teacherDtoList.get(0).getFirstName(), teacherList.get(0).getFirstName());
        assertEquals(teacherDtoList.get(1).getId(), teacherList.get(1).getId());
        assertEquals(teacherDtoList.get(1).getFirstName(), teacherList.get(1).getFirstName());
    }

    @Test
    public void testToDtoList() {
        // Given
        List<Teacher> teacherList = Arrays.asList(
                createTeacher(1L, "John Doe","test"),
                createTeacher(2L, "Jane Smith","test"));

        // When
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        // Then
        assertEquals(teacherList.size(), teacherDtoList.size());
        assertEquals(teacherList.get(0).getId(), teacherDtoList.get(0).getId());
        assertEquals(teacherList.get(0).getFirstName(), teacherDtoList.get(0).getFirstName());
        assertEquals(teacherList.get(1).getId(), teacherDtoList.get(1).getId());
        assertEquals(teacherList.get(1).getFirstName(), teacherDtoList.get(1).getFirstName());
    }

    private TeacherDto createTeacherDto(Long id, String firstName,String lastName) {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(id);
        teacherDto.setFirstName(firstName);
        teacherDto.setLastName(lastName);
        return teacherDto;
    }

    private Teacher createTeacher(Long id, String firstName,String lastName) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        return teacher;
    }
}
