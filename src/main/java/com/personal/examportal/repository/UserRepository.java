package com.personal.examportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.personal.examportal.dto.UserDTO;
import com.personal.examportal.model.StudentModel;
import com.personal.examportal.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

	UserModel findByUsername(String username);

}
