package com.dalc.one.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.Place;

public interface PlaceRepository extends CrudRepository<Place, Long>{
	List<Place> findByPlaceNameContaining(String placeName) throws DataAccessException;
	List<Place> findByAttraction_Category(String category) throws DataAccessException;
	List<Place> findByAddressContaining(String area) throws DataAccessException;
	List<Place> findByContentContaining(String content)throws DataAccessException;
}
