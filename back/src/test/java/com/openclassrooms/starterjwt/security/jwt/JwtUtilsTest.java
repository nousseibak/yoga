package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void testGenerateJwtToken() {
        // Given
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "password","test",true,"test");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // When
        String token = jwtUtils.generateJwtToken(authentication);

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);

        // Validate token content
        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtUtils.getJwtSecret()).parseClaimsJws(token);
        assertEquals("username", claims.getBody().getSubject());
        assertTrue(claims.getBody().getExpiration().after(new Date()));
    }

    @Test
    public void testValidateJwtToken_ValidToken() {
        // Given
        String validToken = generateValidJwtToken();

        // When
        boolean isValid = jwtUtils.validateJwtToken(validToken);

        // Then
        assertTrue(isValid);
    }

    @Test
    public void testValidateJwtToken_InvalidToken() {
        // Given
        String invalidToken = "invalid.token.string";

        // When
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Then
        assertFalse(isValid);
    }

    private String generateValidJwtToken() {
        // Generate a valid JWT token
        return Jwts.builder()
                .setSubject("username")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1000 * 60 * 60)) // Expire in 1 hour
                .signWith(SignatureAlgorithm.HS512, jwtUtils.getJwtSecret())
                .compact();
    }
}

