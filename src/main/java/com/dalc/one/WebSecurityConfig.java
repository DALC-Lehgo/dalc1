package com.dalc.one;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dalc.one.jwt.JwtAuthenticationFilter;
import com.dalc.one.jwt.JwtTokenProvider;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    private JwtTokenProvider jwtTokenProvider;
    
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Override 
	protected void configure(HttpSecurity http) throws Exception { 
		http
		.csrf().disable()
		.httpBasic().disable()
		.authorizeRequests()
//		.antMatchers('/인증 권한이 필요한 페이지').authenticated()
		.antMatchers("/login").permitAll()
		.antMatchers("/checkUser").hasRole("USER")
		.antMatchers("/admin/**").hasRole("ADMIN")
		.anyRequest().permitAll()
		.and() 
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and() 
		.formLogin()
			.disable();
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean //비밀번호 암호화
	public BCryptPasswordEncoder bCryptPasswordEncoder() 
	{ 
		return new BCryptPasswordEncoder(); 
	} 
}