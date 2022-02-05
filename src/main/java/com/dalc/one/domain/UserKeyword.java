package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="userkeyword")
public class UserKeyword implements Serializable{
	@Id
	@Column(name="user_keyword_Id")
	private int userKeywordId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="keyword_id")
	private int keywordId;
}
