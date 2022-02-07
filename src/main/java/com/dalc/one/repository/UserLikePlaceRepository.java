package com.dalc.one.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.UserLikePlace;

public interface UserLikePlaceRepository extends CrudRepository<UserLikePlace, Long>{
	int deleteByUserIdAndPlaceId(String userId, int placeId) throws DataAccessException;
	List<UserLikePlace> findByUserId(String id);
}
