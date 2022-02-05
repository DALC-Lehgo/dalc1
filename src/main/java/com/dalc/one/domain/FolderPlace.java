package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="folder_place")
public class FolderPlace implements Serializable{
	@Id
	@Column(name="folder_id")
	private int folder_Id;
	
	@Column(name="place_id")
	private int placeId;
}
