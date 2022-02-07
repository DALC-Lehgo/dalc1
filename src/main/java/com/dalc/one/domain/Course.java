package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="course")
public class Course implements Serializable{
	@Id
	@Column(name="course_Id")
	private int courseId;
	
	@Column(name="user_id")
	private String userId;
	
	private int visibility;
	
	private int editable;
	
	@Column(name="like_count")
	private int likeCount;
}
