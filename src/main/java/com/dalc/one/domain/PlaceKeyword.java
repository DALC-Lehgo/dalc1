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
@Table(name="placekeyword")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceKeyword implements Serializable{
	@Id
	@Column(name="place_keyword_id")
	private int placeKeywordId;
	
	@Column(name="keyword_id")
	private int keywordId;
	
	@Column(name="place_id")
	private int placeId;
	
	@OneToOne
    @JoinColumn(name="place_id", insertable = false, updatable = false)
	private Place place;
}
