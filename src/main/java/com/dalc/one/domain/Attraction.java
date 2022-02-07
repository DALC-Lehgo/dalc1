package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name="attraction")
@Getter
@Setter
public class Attraction implements Serializable{
	@Id
	@Column(name="attraction_Id")
	private int attractionId;
	
	@Column(name="place_id")
	private int placeId;
	
	private String category;
}
