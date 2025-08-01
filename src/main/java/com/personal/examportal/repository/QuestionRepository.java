package com.personal.examportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.personal.examportal.model.QuestionModel;

public interface QuestionRepository extends JpaRepository<QuestionModel, Integer> {
	public List<QuestionModel> findBySubject_Id(Long subjectId);
	public List<QuestionModel> findQuestionsBySubject_IdAndDifficultyLevel(int subjectId, String difficulty);
	long count();
	@Query("SELECT e.selectedQuestions FROM ExamModel e WHERE e.id = :examId")
	public List<QuestionModel> findQuestionsByExamId(@Param("examId") Long examId);

}
