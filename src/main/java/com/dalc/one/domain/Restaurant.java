package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name="restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant implements Serializable{
	@Column(name="restaurant_id")
	private int restaurantId;

	@Id
	@Column(name="place_id")
	private int placeId;
	
	private String category;
	
	private String menu;
}
