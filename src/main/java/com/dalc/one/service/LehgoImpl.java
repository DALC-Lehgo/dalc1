package com.dalc.one.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dalc.one.dao.UserDAO;
import com.dalc.one.domain.Place;
import com.dalc.one.domain.User;
import com.dalc.one.domain.UserLikePlace;
import com.dalc.one.domain.UserSearchPlace;
import com.dalc.one.repository.PlaceRepository;
import com.dalc.one.repository.UserLikePlaceRepository;
import com.dalc.one.repository.UserSearchRepository;
import com.dalc.one.service.LehgoFacade;

@Service
@Transactional
public class LehgoImpl implements LehgoFacade{

	@Autowired
	private UserDAO userDao;

	@Autowired
	private PlaceRepository placeRepo; 
	
	@Autowired
	private UserLikePlaceRepository ulpRepo;
	
	@Autowired
	private UserSearchRepository usRepo;
	
	public List<User> getUserList(){
		return userDao.getUserList();
	}
	public User findUserbyUserId(String id) {
		return userDao.findUserbyUserId(id);
	}
	public void signUp(User user) {
		userDao.signUp(user);
	}
	public String findUserID(String email) {
		return userDao.findIdbyEmail(email);
	}
	
	public User findUser(String id, String email) {
		return userDao.findUserbyIdAndEmail(id, email);
	}
	@Override
	public int checkUserEmail(String email) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int checkUserNickname(String nickname) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void updateUserInfo(User newUserInfo) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resetPw(User user) {
		userDao.resetPw(user.getPassword(), user.getId());
	}
	
	// ** Place **
	@Override
	public List<Place> getPlaceListbyName(String name) throws DataAccessException {
		return placeRepo.findByPlaceNameContaining(name);
	}
	@Override
	public List<Place> getPlaceListbyCategory(String category) throws DataAccessException {
		return placeRepo.findByAttraction_Category(category);
	}
	@Override
	public List<Place> getPlaceListbyArea(String area) throws DataAccessException {
		return placeRepo.findByAddressContaining(area);
	}
	@Override
	public List<Place> getPlaceListbyContent(String content) throws DataAccessException {
		List<Place> result = placeRepo.findByContentContaining(content);
		result.addAll(placeRepo.findByPlaceNameContaining(content));
		return result;
	}
	
	
	/* UserPlace List */
	@Override
	public UserLikePlace addUserplace(User user, int id) throws DataAccessException {
		UserLikePlace ulp = new UserLikePlace();
		ulp.setPlaceId(id);
		ulp.setUserId(user.getId());
	
		return ulpRepo.save(ulp);
	}
	@Override
	public int deleteUserPlace(@Valid User user, int id) throws DataAccessException {
		return ulpRepo.deleteByUserIdAndPlaceId(user.getId(), id);
	}
	@Override
	public List<UserLikePlace> getUserPlaceList(@Valid User user) throws DataAccessException {
		return ulpRepo.findByUserId(user.getId());
	}
	
	/* User Visited Place List */
	@Override
	public List<UserSearchPlace> getUserVisitedList(@Valid User user) throws DataAccessException {
		return usRepo.findByUserId(user.getId());
	}
	@Override
	public UserSearchPlace addUserVisitedPlace(@Valid User user, int id) {
		UserSearchPlace result = new UserSearchPlace();
		result.setPlaceId(id);
		result.setUserId(user.getId());
	
		return usRepo.save(result);
	}
	@Override
	public int deleteUserVisitedPlace(@Valid User user, int id) {
		return usRepo.deleteByUserIdAndPlaceId(user.getId(), id);
	}
}