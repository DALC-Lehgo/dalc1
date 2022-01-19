package com.dalc.one.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.dalc.one.user.UserVO;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

  public static String makeJwtToken(UserVO user) {
    Date now = new Date();
    
    return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(JwtProperties.getIssuer())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
            .claim("id", user.getUsername())
            .claim("auth", user.getAuth())
//          .claim("name", user.getName())
            .signWith(SignatureAlgorithm.HS256, JwtProperties.getSecretKey())
            .compact();
  }

  public static UserVO getUserOf(String authorizationHeader) {
    validationAuthorizationHeader(authorizationHeader);

    String token = extractToken(authorizationHeader);
    Claims claims = parsingToken(token);

    return new UserVO(claims);
  }

  private static Claims parsingToken(String token) {
    return Jwts.parser()
        .setSigningKey(JwtProperties.getSecretKey())
        .parseClaimsJws(token)
        .getBody();
  }

  private static void validationAuthorizationHeader(String header) {
    if (header == null || !header.startsWith(JwtProperties.getTokenPrefix())) {
      throw new IllegalArgumentException();
    }
  }

  private static String extractToken(String authorizationHeader) {
    return authorizationHeader.substring(JwtProperties.getTokenPrefix().length());
  }
}