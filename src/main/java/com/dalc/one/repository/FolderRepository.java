package com.dalc.one.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.Folder;

public interface FolderRepository extends CrudRepository<Folder, Long>{

	int deleteByUserIdAndFolderName(String id, String name);

	int deleteByUserIdAndFolderId(String id, int id2);

	List<Folder> findByUserId(String userId);

	Folder findByFolderId(int id);
	
	Folder findByUserIdAndFolderPlace_PlaceId(String userId, int placeId);

}
