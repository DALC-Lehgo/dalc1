package com.dalc.one.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@SuppressWarnings("serial")
@Data
class UserSearchPlacePK implements Serializable{
	private String userId;
	private int placeId;	
}



@SuppressWarnings("serial")
@Entity
@Table(name="user_search_place")
@IdClass(UserLikePlacePK.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSearchPlace implements Serializable{
	@Id
	@Column(name="user_id")
	private String userId;
	
	@Id
	@Column(name="place_id")
	private int placeId;
	
	@CreationTimestamp
	private Date time;
}
