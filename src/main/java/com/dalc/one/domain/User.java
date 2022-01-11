package com.dalc.one.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String user_id;
	private String name;
	
	public String getId() {
		return user_id;
	}
	public void setId(String id) {
		this.user_id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/* JavaBeans Properties */
}