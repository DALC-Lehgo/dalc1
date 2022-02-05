package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="placekeyword")
public class PlaceKeyword implements Serializable{
	@Id
	@Column(name="place_keyword_id")
	private int placeKeywordId;
	
	@Column(name="keyword_id")
	private int keywordId;
	
	@Column(name="place_id")
	private int placeId;
}
