package com.dalc.one.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="folder")
public class Folder implements Serializable{
	@Id
	@Column(name="folder_Id")
	private int folderId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="folder_name")
	private String folderName;
}
