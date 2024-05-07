package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDetailsImplTest {

    @Test
    public void testUserDetailsImplInitialization() {
        // Given
        Long id = 1L;
        String username = "john.doe@example.com";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;
        String password = "password123";

        // When
        UserDetails userDetails = UserDetailsImpl.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .admin(admin)
                .password(password)
                .build();

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(((UserDetailsImpl) userDetails).getId()).isEqualTo(id);
        assertThat(((UserDetailsImpl) userDetails).getFirstName()).isEqualTo(firstName);
        assertThat(((UserDetailsImpl) userDetails).getLastName()).isEqualTo(lastName);
        assertThat(((UserDetailsImpl) userDetails).getAdmin()).isEqualTo(admin);
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    @Test
    public void testUserDetailsImplAuthorities() {
        // Given
        UserDetails userDetails = UserDetailsImpl.builder().build();

        // When
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Then
        assertThat(authorities).isNotNull().isEmpty();
    }

    @Test
    public void testUserDetailsImplAccountExpiration() {
        // Given
        UserDetails userDetails = UserDetailsImpl.builder().build();

        // Then
        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    public void testUserDetailsImplAccountLock() {
        // Given
        UserDetails userDetails = UserDetailsImpl.builder().build();

        // Then
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testUserDetailsImplCredentialsExpiration() {
        // Given
        UserDetails userDetails = UserDetailsImpl.builder().build();

        // Then
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void testUserDetailsImplEnabled() {
        // Given
        UserDetails userDetails = UserDetailsImpl.builder().build();

        // Then
        assertThat(userDetails.isEnabled()).isTrue();
    }
}
