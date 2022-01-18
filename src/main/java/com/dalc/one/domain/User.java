package com.dalc.one.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class User implements Serializable {
	private String id;
	private String name;
	
	/* JavaBeans Properties */
}