package com.dalc.one.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name="course")
@SequenceGenerator(
		name = "COURSE_SEQ_GENERATOR"
	    , sequenceName = "COURSE_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable{
	@Id
	@Column(name="course_id")
	@GeneratedValue(
    	strategy = GenerationType.SEQUENCE
    	, generator = "COURSE_SEQ_GENERATOR"
    )
	private int courseId;
	
	@Column(name="user_id")
	private String userId;
	
	@OneToMany
    @JoinColumn(name="course_id", insertable = false, updatable = false)
	private Collection<CoursePlace> coursePlace;
	
	private int visibility;
	
	private int editable;
	
	@Column(name="like_count")
	private int likeCount;
}
