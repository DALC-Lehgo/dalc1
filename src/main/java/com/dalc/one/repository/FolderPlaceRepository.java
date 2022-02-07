package com.dalc.one.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.FolderPlace;

public interface FolderPlaceRepository extends CrudRepository<FolderPlace, Long>{

	List<FolderPlace> findByFolderId(int folderId);

	int deleteByFolderIdAndPlaceId(int folderId, int placeId);

}
