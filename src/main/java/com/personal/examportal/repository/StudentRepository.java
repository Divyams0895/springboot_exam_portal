package com.personal.examportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.personal.examportal.dto.UserDTO;
import com.personal.examportal.model.StudentModel;

public interface StudentRepository extends JpaRepository<StudentModel, Long> {
	long count();
	
	@Query("SELECT new com.personal.examportal.dto.UserDTO(s.fullName, s.phone, s.profilePhoto, s.status, u.username) " +
		       "FROM StudentModel s JOIN s.user u WHERE u.role = 'ROLE_STUDENT'")
		Page<UserDTO> getAllStudentDetails(Pageable page);
	
	@Query("SELECT new com.personal.examportal.dto.UserDTO(s.fullName, s.phone, s.profilePhoto, s.status, u.username) " +
		       "FROM StudentModel s JOIN s.user u WHERE u.role = 'ROLE_ADMIN'")
		UserDTO getAdminDetails();
	
	@Query("SELECT new com.personal.examportal.dto.UserDTO(s.fullName, s.phone, s.profilePhoto, s.status, u.username) " +
		       "FROM StudentModel s JOIN s.user u WHERE u.role = 'ROLE_STUDENT'")
		UserDTO getStudentDetails();
	
	@Query("SELECT s FROM StudentModel s WHERE s.user.username = :username")
    StudentModel findByUsername(@Param("username") String username);
	
	@Modifying
	@Query("UPDATE StudentModel s " +
	       "SET s.fullName = :fullName, " +
	           "s.phone = :phone, " +
	           "s.profilePhoto = :profilePhoto, " +
	           "s.status = :status " +
	       "WHERE s.user.role = 'ROLE_ADMIN' AND s.user.username = :username")
	int updateAdminDetails(@Param("fullName") String fullName,
	                       @Param("phone") String phone,
	                       @Param("profilePhoto") String profilePhoto,
	                       @Param("status") Boolean status,
	                       @Param("username") String username);

}
