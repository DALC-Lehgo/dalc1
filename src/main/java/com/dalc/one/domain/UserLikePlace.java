package com.dalc.one.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Data
class UserLikePlacePK implements Serializable{
	private String userId;
	
	private int placeId;	
}

@SuppressWarnings("serial")
@Entity
@Table(name="user_like_place")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(UserLikePlacePK.class)
public class UserLikePlace implements Serializable{
	
	@Id
	@Column(name = "user_id")
	private String userId;
	
	@Id
	@Column(name = "place_id")
	private int placeId;
	
	@OneToOne
    @JoinColumn(name="place_id", insertable = false, updatable = false)
	private Place place;
	
	@CreationTimestamp
	private Date time;
}
