package com.dalc.one.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dalc.one.domain.User;

import io.jsonwebtoken.Claims;

@SuppressWarnings("serial")
public class UserVO implements UserDetails{
	private List<GrantedAuthority> authorities;
	private String id;
	private String password;
	private String nickname;
	private String auth;

	public UserVO(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.password = user.getPassword();
		this.auth = user.getAuth();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Set<GrantedAuthority> roles = new HashSet<>();
	    for (String role : auth.split(",")) {
	      roles.add(new SimpleGrantedAuthority(role));
	    }
	    return roles;
	}
	
	public UserVO(Claims claims) {
		this.id = claims.get("id", String.class);
		this.auth = claims.get("auth", String.class);
		this.nickname = claims.get("nickname", String.class);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return id;
	}
	public String getAuth() {
		return auth;
	}
	public String getNickname() {
		return nickname;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}