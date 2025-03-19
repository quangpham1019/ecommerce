package org.ecommerce.common.jwt;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * This filter intercepts incoming HTTP requests and checks for a valid JWT token in the "Authorization" header.
     * If a valid token is found, it extracts the username and authorities (roles) from the token, validates the token,
     * and sets the authentication details in the Spring Security context.
     *
     * If the token is expired or invalid, a corresponding error response is sent to the client.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain to pass the request and response to the next filter in the chain.
     * @throws ServletException If the request could not be processed.
     * @throws IOException If an input or output exception occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")
                || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username != null) {

                List<GrantedAuthority> authorities = new ArrayList<>(jwtUtil
                        .extractAuthorities(token)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain");
            response.getWriter().write("Token has expired");
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain");
            response.getWriter().write("Invalid JWT token");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
