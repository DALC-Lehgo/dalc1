package com.dalc.one.repository;


import org.springframework.data.repository.CrudRepository;

import com.dalc.one.domain.UserKeyword;

public interface UserKeywordRepository extends CrudRepository<UserKeyword, Long>{
	UserKeyword findByUserId(String id);
}
