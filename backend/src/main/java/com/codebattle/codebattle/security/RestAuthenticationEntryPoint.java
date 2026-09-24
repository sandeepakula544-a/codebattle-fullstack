package com.codebattle.codebattle.security;

import com.codebattle.codebattle.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Without this, Spring Security's default behavior for an unauthenticated
 * request to a protected endpoint is an EMPTY 403 response (no body,
 * confusing to debug). This returns a proper 401 with the same
 * ErrorResponse shape the rest of the API uses, so the frontend's
 * "clear stale token on 401" logic actually fires.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                          HttpServletResponse response,
                          AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse body = new ErrorResponse(
                401,
                "Unauthorized",
                "Your session is invalid or has expired. Please log in again."
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
