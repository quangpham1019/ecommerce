package org.ecommerce.user.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ecommerce.common.jwt.JwtUtil;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final Environment environment;

    public CustomSuccessHandler(JwtUtil jwtUtil, Environment environment) {
        this.jwtUtil = jwtUtil;
        this.environment = environment;
    }

    /***
     * Custom login success handler to perform the following tasks:
     * - Set authorization header in response to have the JWT Bearer token.
     * - Invoke dummy endpoint to retrieve and set XSRF cookie.
     * - Return both value in response body.
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);

        // Add JWT to the response header
        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");

        // Also include the token in the response body
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);

        Optional<String> csrfToken = retrieveAndSetCsrfCookieFromDummyEndpoint();
        assert Objects.requireNonNull(csrfToken).isPresent();

        responseBody.put("csrf_token", csrfToken.orElse(""));
        response.addCookie(new Cookie("XSRF-TOKEN", csrfToken.orElse("")));
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }

    private Optional<String> retrieveAndSetCsrfCookieFromDummyEndpoint() {

        RestTemplate restTemplate = getRestTemplate();

//         Send POST request
        String url = String.format("http://localhost:%s/api/v1/csrf", environment.getProperty("server.port"));

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, null, String.class);

        for (String cookie : Objects.requireNonNull(responseEntity.getHeaders().get("Set-Cookie"))) {
            if (cookie.startsWith("XSRF-TOKEN=")) {
                // Extract the CSRF token value
                return Optional.of(cookie.split(";")[0].split("=")[1]);
            }
        }
        return Optional.empty();
    }

    private static RestTemplate getRestTemplate() {
        // Set up RestTemplate to send POST request to dummy endpoint
        RestTemplate restTemplate = new RestTemplate();

        // ✅ Custom error handler to prevent throwing exceptions on 403
        // Allowing responseEntity to have value and headers for extraction
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // Do nothing to prevent exceptions on 403 responses
            }
        });
        return restTemplate;
    }
}
