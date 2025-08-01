package com.personal.examportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.examportal.model.PasswordResetToken;

public interface TokenRepository extends JpaRepository<PasswordResetToken, Long>{
	PasswordResetToken findByToken(String token);

}
