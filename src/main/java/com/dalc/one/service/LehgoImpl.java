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
import com.dalc.one.domain.Course;
import com.dalc.one.domain.CoursePlace;
import com.dalc.one.domain.Folder;
import com.dalc.one.domain.FolderPlace;
import com.dalc.one.repository.CoursePlaceRepository;
import com.dalc.one.repository.CourseRepository;
import com.dalc.one.repository.FolderPlaceRepository;
import com.dalc.one.repository.FolderRepository;
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
	
	@Autowired
	private FolderRepository folderRepo;
	
	@Autowired
	private FolderPlaceRepository fpRepo;
	
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private CoursePlaceRepository cpRepo;
	
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
	public int checkUserId(String id){
		return userDao.checkUserId(id);
	}
	public int checkUserEmail(String email){
		return userDao.checkUserEmail(email);
	}
	public int checkUserNickname(String nickname){
		return userDao.checkUserNickname(nickname);
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
	public Place getPlace(int id) {
		return placeRepo.findByPlaceId(id);
	}
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
	@Override
	public List<Place> getPlaceListbyAll(String query) {
		List<Place> result = placeRepo.findByContentContaining(query);
		result.addAll(placeRepo.findByPlaceNameContaining(query));
		result.addAll(placeRepo.findByAddressContaining(query));
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

	
	/* Folder */
	@Override
	public Folder addFolder(@Valid User user, String name) throws DataAccessException {
		Folder entity = new Folder();
		entity.setFolderName(name);
		entity.setUserId(user.getId());
		return folderRepo.save(entity);
	}
	@Override
	public int deleteFolder(@Valid User user, int id) {
		fpRepo.deleteByFolderId(id);
		return folderRepo.deleteByUserIdAndFolderId(user.getId(), id);
	}
	@Override
	public List<Folder> getFolderList(String userId) {
		return folderRepo.findByUserId(userId);
	}
	@Override
	public Folder getFolder(int id) {
		return folderRepo.findByFolderId(id);
	}
	
	/* FolderPlace */
	@Override
	public FolderPlace addFolderPlace(int folderId, int placeId) {
		FolderPlace entity = new FolderPlace();
		entity.setFolderId(folderId);
		entity.setPlaceId(placeId);
		
		return fpRepo.save(entity);
	}
	@Override
	public List<FolderPlace> getFolderPlaceList(int folderId) {
		return fpRepo.findByFolderId(folderId);
	}
	@Override
	public int deleteFolderPlace(int folderId, int placeId) {
		return fpRepo.deleteByFolderIdAndPlaceId(folderId, placeId);
	}
	
	/* Course */
	@Override
	public List<Course> getCourseList(String userId) {
		return courseRepo.findByUserId(userId);
	}
	@Override
	public Course getCourse(int id) {
		return courseRepo.findByCourseId(id);
	}
	@Override
	public Course getCourseByUserId(String id, int courseId) {
		return courseRepo.findByCourseIdAndUserId(courseId, id);
	}
	@Override
	public Course addCourse(@Valid User user) {
		Course entity = new Course();
		entity.setUserId(user.getId());
		entity.setEditable(1);
		entity.setLikeCount(0);
		entity.setVisibility(1);

		return courseRepo.save(entity);
	}
	@Override
	public Course updateCourse(Course course) {
		return courseRepo.save(course);
	}
	@Override
	public int deleteCourse(int cid) {
		cpRepo.deleteByCourseId(cid);
		return courseRepo.deleteByCourseId(cid);
	}
	
	/* Course Detail */
	@Override
	public List<CoursePlace> getCourseDetail(int courseId) {
		return cpRepo.findByCourseId(courseId);
	}
	@Override
	public CoursePlace addCourseDetail(int courseId, int placeId) {
		CoursePlace entity = new CoursePlace();
		entity.setCourseId(courseId);
		entity.setPlaceId(placeId);
		
		return cpRepo.save(entity);
	}
	@Override
	public int deleteCourseDetail(int cid, int pid) {
		return cpRepo.deleteByCourseIdAndPlaceId(cid, pid);
	}

}