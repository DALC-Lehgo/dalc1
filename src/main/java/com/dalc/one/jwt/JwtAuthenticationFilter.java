package com.dalc.one.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dalc.one.user.UserVO;

@Getter
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Builder
  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public static JwtAuthenticationFilter of(JwtTokenProvider jwtTokenProvider) {
    return JwtAuthenticationFilter.builder()
        .jwtTokenProvider(jwtTokenProvider)
        .build();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if(authorizationHeader != null) {
	    try {
	     UserVO user = jwtTokenProvider.getUserOf(authorizationHeader);
	      SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
	          user,
	          "",
	          user.getAuthorities()));
	
	     
	    } catch (ExpiredJwtException exception) {
	      logger.error("ExpiredJwtException", exception);
	    }
	}
    filterChain.doFilter(request, response);
  }
}