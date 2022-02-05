package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="course_place")
public class CoursePlace implements Serializable{
	@Id
	@Column(name="course_Id")
	private int courseId;
	
	@Column(name="place_id")
	private int placeId;
	
	private int column2;
}
