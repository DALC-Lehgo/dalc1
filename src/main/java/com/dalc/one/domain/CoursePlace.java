package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Data
class CoursePlacePK implements Serializable{
	private int courseId;
	private int placeId;	
}

@SuppressWarnings("serial")
@Entity
@Table(name="course_place")
@IdClass(CoursePlacePK.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoursePlace implements Serializable{
	@Id
	@Column(name="course_Id")
	private int courseId;
	
	@Id
	@Column(name="place_id")
	private int placeId;
	
	@OneToOne
    @JoinColumn(name="place_id", insertable = false, updatable = false)
	private Place place;
}
