package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="user_like_course")
public class UserLikeCourse implements Serializable{
	@Id
	@Column(name="attraction_Id")
	private int attractionId;
	
	@Column(name="place_id")
	private int placeId;
	
	private String category;
}
