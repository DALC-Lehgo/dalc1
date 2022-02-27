package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@Entity
@Table(name="user_keyword")
@SequenceGenerator(
		name = "USER_KEYWORD_SEQ_GENERATOR"
	    , sequenceName = "USER_KEYWORD_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
public class UserKeyword implements Serializable{
	@Id
	@Column(name="user_keyword_id")
	@GeneratedValue(
    	strategy = GenerationType.SEQUENCE
    	, generator = "USER_KEYWORD_SEQ_GENERATOR"
    )
	private int userKeywordId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="keyword_id")
	private int keywordId;
}
