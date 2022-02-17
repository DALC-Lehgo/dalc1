package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name="user_like_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeCourse implements Serializable{
	@Id
	@Column(name="attraction_Id")
	private int attractionId;
	
	@Column(name="place_id")
	private int placeId;
	
	private String category;
}
