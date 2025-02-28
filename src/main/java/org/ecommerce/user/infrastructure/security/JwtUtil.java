package org.ecommerce.user.infrastructure.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class for handling JWT creation, extraction, and validation.
 */
@Component
public class JwtUtil {

    private static final String SECRET_KEY = "ecc6665e80e155613f04465d0a3d68dfed537cc36820ba6c7ec10a919f3bfc05d24f2c7920d74e698f7510560ef11e54ef1df7b682475d557b938934646a8171";

    /**
     * Retrieves the signing key used to sign and verify JWTs.
     * The key is decoded from a BASE64 encoded string.
     *
     * @return The signing key.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the list of authorities (roles) from the JWT.
     *
     * @param token The JWT token.
     * @return A list of authority names (strings) extracted from the JWT.
     */
    public List<String> extractAuthorities(String token) {
        return extractClaim(token, claims -> {
            List<?> authorities = claims.get("authorities", List.class);

            // Filter and convert authorities to a List of Strings
            return authorities
                    .stream()
                    .filter(authority -> authority instanceof String)
                    .map(authority -> (String) authority)
                    .toList();
        });
    }

    /**
     * Extracts the username (subject) from the JWT.
     *
     * @param token The JWT token.
     * @return The username extracted from the JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the JWT token using the provided claim resolver.
     *
     * @param token The JWT token.
     * @param claimsResolver A function to extract a claim from the JWT's claims.
     * @param <T> The type of the claim.
     * @return The value of the extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return The claims contained in the JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generates a JWT token for the given user details, including the username and authorities.
     * The token will be signed with the secret key and will have a 1-hour expiration.
     *
     * @param userDetails The user details for which the JWT token will be generated.
     * @return The generated JWT token as a string.
     */
    public String generateToken(UserDetails userDetails) {

        List<String> authorities = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .claim("sub", userDetails.getUsername())  // "sub" claim represents the subject (username)
                .claim("authorities", authorities)      // Include the user's authorities (roles)
                .issuedAt(new Date(System.currentTimeMillis()))  // Issue time is current time
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(getSigningKey())  // Sign the JWT with the secret key
                .compact();  // Return the compact JWT token as a string
    }

    /**
     * Validates the JWT token by checking if the username matches the one in the token
     * and if the token is not expired.
     *
     * @param token The JWT token.
     * @param username The username to check against the one in the token.
     * @return true if the token is valid (username matches and token is not expired), false otherwise.
     */
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token has expired based on its expiration claim.
     *
     * @param token The JWT token.
     * @return true if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}