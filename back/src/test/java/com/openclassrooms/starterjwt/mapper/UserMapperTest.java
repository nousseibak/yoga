package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void testToEntity() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("test@gmail.com");
        userDto.setPassword("test");

        // When
        User user = userMapper.toEntity(userDto);

        // Then
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
    }

    @Test
    public void testToDto() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@gmail.com");
        user.setPassword("test");

        // When
        UserDto userDto = userMapper.toDto(user);

        // Then
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
    }
    @Test
    public void testToEntityList() {
        // Given
        List<UserDto> userDtoList = Arrays.asList(
                createUserDto(1L, "John", "Doe"),
                createUserDto(2L, "Jane", "Smith")
        );

        // When
        List<User> userList = userMapper.toEntity(userDtoList);

        // Then
        assertEquals(userDtoList.size(), userList.size());
        assertEquals(userDtoList.get(0).getId(), userList.get(0).getId());
        assertEquals(userDtoList.get(0).getFirstName(), userList.get(0).getFirstName());
        assertEquals(userDtoList.get(0).getLastName(), userList.get(0).getLastName());
        assertEquals(userDtoList.get(1).getId(), userList.get(1).getId());
        assertEquals(userDtoList.get(1).getFirstName(), userList.get(1).getFirstName());
        assertEquals(userDtoList.get(1).getLastName(), userList.get(1).getLastName());
        // Add more assertions as needed
    }

    @Test
    public void testToDtoList() {
        // Given
        List<User> userList = Arrays.asList(
                createUser(1L, "John", "Doe"),
                createUser(2L, "Jane", "Smith")
        );

        // When
        List<UserDto> userDtoList = userMapper.toDto(userList);

        // Then
        assertEquals(userList.size(), userDtoList.size());
        assertEquals(userList.get(0).getId(), userDtoList.get(0).getId());
        assertEquals(userList.get(0).getFirstName(), userDtoList.get(0).getFirstName());
        assertEquals(userList.get(0).getLastName(), userDtoList.get(0).getLastName());
        assertEquals(userList.get(1).getId(), userDtoList.get(1).getId());
        assertEquals(userList.get(1).getFirstName(), userDtoList.get(1).getFirstName());
        assertEquals(userList.get(1).getLastName(), userDtoList.get(1).getLastName());
        // Add more assertions as needed
    }

    private UserDto createUserDto(Long id, String firstName, String lastName) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPassword("test");
        userDto.setEmail("test@gmail.com");
        return userDto;
    }

    private User createUser(Long id, String firstName, String lastName) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword("test");
        user.setEmail("test@gmail.com");
        return user;
    }

}

 
