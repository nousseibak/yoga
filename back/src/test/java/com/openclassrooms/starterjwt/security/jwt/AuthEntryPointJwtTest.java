package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthEntryPointJwtTest {

    @Mock
    private AuthenticationException authException;

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCommence() throws IOException, ServletException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // When
        authEntryPointJwt.commence(request, response, authException);

        // Then
        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());

        ObjectMapper mapper = new ObjectMapper();
        String responseBody = response.getContentAsString();
        ErrorDetails expectedErrorDetails = new ErrorDetails(401, "Unauthorized", "Unauthorized error: Unauthorized", request.getServletPath());
        ErrorDetails actualErrorDetails = mapper.readValue(responseBody, ErrorDetails.class);

        assertEquals(expectedErrorDetails.getStatus(), actualErrorDetails.getStatus());
        assertEquals(expectedErrorDetails.getError(), actualErrorDetails.getError());
        assertEquals(expectedErrorDetails.getPath(), actualErrorDetails.getPath());
    }

    private static class ErrorDetails {
        private int status;
        private String error;
        private String message;
        private String path;

        public ErrorDetails() {}

        public ErrorDetails(int status, String error, String message, String path) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public String getPath() {
            return path;
        }

        // Implement equals method to compare objects
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ErrorDetails that = (ErrorDetails) obj;
            return status == that.status &&
                    error.equals(that.error) &&
                    message.equals(that.message) &&
                    path.equals(that.path);
        }
    }
}
