package com.dalc.one.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name="folder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
		name = "FOLDER_SEQ_GENERATOR"
	    , sequenceName = "FOLDER_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
public class Folder implements Serializable{
	@Id
	@Column(name="folder_id")
	@GeneratedValue(
    	strategy = GenerationType.SEQUENCE
    	, generator = "FOLDER_SEQ_GENERATOR"
    )
	private int folderId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="folder_name")
	private String folderName;
	
	@OneToMany
    @JoinColumn(name="folder_id", insertable = false, updatable = false)
	private Collection<FolderPlace> folderPlace;
}
