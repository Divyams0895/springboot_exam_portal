package com.personal.examportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.examportal.model.SubjectModel;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectModel, Long> {
	long count();
}
