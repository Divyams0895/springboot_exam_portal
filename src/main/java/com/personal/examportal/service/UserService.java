package com.personal.examportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.examportal.model.UserModel;
import com.personal.examportal.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public void saveUser(UserModel user) {
//		System.out.println("Check 4");
		userRepository.save(user);
//		System.out.println("Check 5");
	}

}
