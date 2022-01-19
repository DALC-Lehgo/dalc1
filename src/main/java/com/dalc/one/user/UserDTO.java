package com.dalc.one.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO{
	private String id;
	private String password;
	
	@Builder
	public UserDTO(String id, String password) {
		this.id = id;
		this.password = password;
	}
	/* JavaBeans Properties */
}