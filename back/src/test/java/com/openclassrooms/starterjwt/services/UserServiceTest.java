package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeleteUser() {
        Long id = 1L;

        userService.delete(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindUserById_ExistingId() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(id);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testFindUserById_NonExistingId() {
        Long id = 999L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        User foundUser = userService.findById(id);

        assertNull(foundUser);

        verify(userRepository, times(1)).findById(id);
    }
}
