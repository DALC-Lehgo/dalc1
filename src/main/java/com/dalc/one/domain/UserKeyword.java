package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_keyword")
@SuppressWarnings("serial")
public class UserKeyword implements Serializable{
	@Id
	@Column(name="user_keyword_Id")
	private int userKeywordId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="keyword_id")
	private int keywordId;
}
