package com.dalc.one.user;

import com.dalc.one.domain.User;
import com.dalc.one.service.LehgoFacade;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService{
	LehgoFacade lehgo;
	
	@Autowired
	public void setFacade(LehgoFacade lehgo) {
		this.lehgo = lehgo;
	}
	
	@Override
	public UserVO loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = lehgo.findUserbyUserId(userId);
		
		if (user == null) {
			new UsernameNotFoundException("User Not Found with username: " + userId);
		}
		return new UserVO(user);
	}
	
	public void signUp(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		
		lehgo.signUp(user);
	}
	
	public void resetPw(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		
		lehgo.resetPw(user);
	}
	
	public boolean checkPassword(UserDTO user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(user.getPassword(), lehgo.findUserbyUserId(user.getId()).getPassword());
    }
}
