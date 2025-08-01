package com.personal.examportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.personal.examportal.model.ExamModel;

public interface ExamRepository extends JpaRepository<ExamModel, Long> {
	public List<ExamModel> findBySubjectId(Long subjectId);
	long count();
	@Query("SELECT COUNT(q) FROM ExamModel e JOIN e.selectedQuestions q WHERE e.id = :examId")
	int countQuestionsByExamId(@Param("examId") Long examId);

}
