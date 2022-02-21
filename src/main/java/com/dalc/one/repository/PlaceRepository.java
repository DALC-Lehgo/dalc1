package com.dalc.one.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.Place;

public interface PlaceRepository extends CrudRepository<Place, Long>{
	List<Place> findByPlaceNameContainingAndUserIdIsNull(String placeName) throws DataAccessException;
	List<Place> findByAttraction_CategoryAndUserIdIsNull(String category) throws DataAccessException;
	List<Place> findByAddressContainingAndUserIdIsNull(String area) throws DataAccessException;
	List<Place> findByContentContainingAndUserIdIsNull(String content)throws DataAccessException;
	Place findByPlaceId(int id) throws DataAccessException;
}
