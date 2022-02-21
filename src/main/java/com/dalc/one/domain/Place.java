package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name="place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
		name = "PLACE_SEQ_GENERATOR"
	    , sequenceName = "PLACE_SEQ"
	    , initialValue = 2558
	    , allocationSize = 1
	)
public class Place implements Serializable{
	@Id
	@Column(name="place_id")
	@GeneratedValue(
	    	strategy = GenerationType.SEQUENCE
	    	, generator = "PLACE_SEQ_GENERATOR"
	    )
	private int placeId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="place_name")
	private String placeName;
	
	
	@OneToOne
    @JoinColumn(name="place_id", insertable = false, updatable = false)
	private Attraction attraction;
	
	private double latitude;
	
	private double longitude;
	
	private String address;
	
	private String tel;
	
	private String time;
	
	private String content;
	
	@Column(name="like_count")
	private int likeCount;
	
	private String img1;
	
	private String img2;
	
	private String img3;
	
	private String img4;
}
