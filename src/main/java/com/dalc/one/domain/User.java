package com.dalc.one.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class User implements Serializable {
    @NotBlank
	private String id;
    @NotBlank
	private String name;
    @NotBlank
	private String password;
    @NotBlank
	private String email;
    @NotBlank
	private String nickname;
    @NotBlank
	private String gender;
    @NotBlank
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

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", nickname="
				+ nickname + ", gender=" + gender + ", age=" + age + ", platform=" + platform + ", token=" + token
				+ ", auth=" + auth + "]";
	}

}