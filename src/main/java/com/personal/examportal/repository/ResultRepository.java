package com.personal.examportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.personal.examportal.model.ExamModel;
import com.personal.examportal.model.ResultModel;
import com.personal.examportal.model.StudentModel;

public interface ResultRepository extends JpaRepository<ResultModel, Long> {

	ResultModel findTopByStudentAndExam(StudentModel student, ExamModel exam);

	

}
