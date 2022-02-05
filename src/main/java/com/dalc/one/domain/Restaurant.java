package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="restaurant")
public class Restaurant implements Serializable{
	@Id
	@Column(name="restaurant_id")
	private int restaurantId;
	
	@Column(name="place_id")
	private int placeId;
	
	private String category;
	
	private String menu;
}
