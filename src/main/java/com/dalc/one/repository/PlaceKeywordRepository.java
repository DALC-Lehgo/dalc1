package com.dalc.one.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dalc.one.domain.PlaceKeyword;

public interface PlaceKeywordRepository extends CrudRepository<PlaceKeyword, Long>{
	
	@Query(value = "SELECT * FROM place_keyword pk "+ 
			"ORDER BY (CASE WHEN pk.keyword_id = :keyword THEN 1 ELSE 2 END)", nativeQuery = true)
	List<PlaceKeyword> findPlaceKeywordBykeyword(@Param(value = "keyword") int keyword);
}