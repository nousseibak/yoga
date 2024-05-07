package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("test");

        // Création d'un UserDetailsImpl simulé pour Authentication.getPrincipal()
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "John", "Doe", false, "test");

        // Création d'une authentification simulée avec le UserDetailsImpl simulé comme principal
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Simuler le comportement de userRepository
        User user = new User("test@example.com", "Doe", "John", "encodedPassword", false);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Simuler l'authentification réussie
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Simuler la génération du token JWT
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fakeJwtToken");

        // Appel de la méthode à tester
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // Vérifier que la réponse n'est pas nulle
        assertNotNull(responseEntity);

        // Vérifier que la réponse contient un objet JwtResponse
        assertTrue(responseEntity.getBody() instanceof JwtResponse);

        // Vérifier le contenu de JwtResponse
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertNotNull(jwtResponse);
        assertEquals("fakeJwtToken", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("John", jwtResponse.getFirstName());
        assertEquals("Doe", jwtResponse.getLastName());
        assertFalse(jwtResponse.getAdmin());

        // Vérifier que userRepository.findByEmail a été appelé une fois avec le bon email
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testRegisterUser() {
        // Créer un SignupRequest simulé
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        // Simuler le comportement de userRepository.existsByEmail
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        // Simuler le comportement de passwordEncoder.encode
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Appel de la méthode à tester
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Vérifier que la réponse est OK (200)
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Vérifier que la réponse contient un objet MessageResponse avec le bon message
        assertTrue(responseEntity.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertNotNull(messageResponse);
        assertEquals("User registered successfully!", messageResponse.getMessage());

        // Vérifier que userRepository.save a été appelé une fois avec le bon utilisateur
        verify(userRepository, times(1)).save(argThat(user ->
                user.getEmail().equals("test@example.com") &&
                        user.getFirstName().equals("John") &&
                        user.getLastName().equals("Doe") &&
                        user.getPassword().equals("encodedPassword") &&
                        !user.isAdmin()));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Créer un SignupRequest simulé avec un email existant
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");

        // Simuler le comportement de userRepository.existsByEmail
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Appel de la méthode à tester
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Vérifier que la réponse est une BadRequest (400)
        assertEquals(400, responseEntity.getStatusCodeValue());

        // Vérifier que la réponse contient un objet MessageResponse avec le bon message
        assertTrue(responseEntity.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertNotNull(messageResponse);
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());

        // Vérifier que userRepository.save n'a pas été appelé
        verify(userRepository, never()).save(any());
    }
}
