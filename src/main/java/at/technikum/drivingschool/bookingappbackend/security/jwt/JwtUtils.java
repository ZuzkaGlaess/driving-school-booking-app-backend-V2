package at.technikum.drivingschool.bookingappbackend.security.jwt;

import at.technikum.drivingschool.bookingappbackend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

/**
 * JWT Utility class
 */
@Component
public class JwtUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

  /**
   * Token en- & decryption secret - from properties
   */
  @Value("${jwt.secret}")
  private String jwtSecret;

  /**
   * Token expiration time in MS - from properties
   */
  @Value("${jwt.expirationMs}")
  private int jwtExpirationMs;

  /**
   * Cookie name for Token - from properties
   */
  @Value("${jwt.cookieName}")
  private String jwtCookie;

  /**
   * Read token from header (cookie)
   */
  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public String extractTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");

    if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
      return token.substring("Bearer ".length());
    }

    return null;
  }

  /**
   * Generate response cookie with token
   */
  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(jwtExpirationMs).httpOnly(true).build();
    return cookie;
  }

  /**
   * Generate empty response cookie
   */
  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }

  /**
   * Extract Username from Token
   */
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Generate an en- & decryption key
   */
  private Key key() {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
    return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
  }

  /**
   * Validate Token format, timestamp, arguments, ... correctness
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      LOGGER.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOGGER.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOGGER.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOGGER.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  /**
   * Generate token for user
   */
  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
              .setSubject(username)
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
              .signWith(key(), SignatureAlgorithm.HS256)
              .compact();
  }
}
