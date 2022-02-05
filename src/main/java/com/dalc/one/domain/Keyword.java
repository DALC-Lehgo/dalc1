package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="keyword")
public class Keyword implements Serializable{
	@Id
	@Column(name="keyword_id")
	private int keywordId;
	
	@Column(name="keyword_name")
	private String keywordName;
	
}
