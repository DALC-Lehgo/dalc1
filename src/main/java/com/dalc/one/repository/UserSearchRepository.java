package com.dalc.one.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.UserSearchPlace;

public interface UserSearchRepository extends CrudRepository<UserSearchPlace, Long>{
	int deleteByUserIdAndPlaceId(String userId, int placeId) throws DataAccessException;
	List<UserSearchPlace> findByUserId(String id);
}
