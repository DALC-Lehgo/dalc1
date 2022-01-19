package com.dalc.one.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class User implements Serializable {
	private String id;
	private String name;
	private String password;
	private String email;
	private String nickname;
	private String gender;
	private String age;
	private String platform;
	private String token;
	private String auth;
	
	@Builder
	public User(String id, String name, String password, String auth) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.auth = auth;
	}
	/* JavaBeans Properties */
}