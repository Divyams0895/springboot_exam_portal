package com.personal.examportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.personal.examportal.model.QuestionModel;
import com.personal.examportal.repository.QuestionRepository;


@Service
public class QuestionService {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	public void addQuestion(QuestionModel QuestionModel) {
		questionRepository.save(QuestionModel);
	}
	
	public List<QuestionModel> allQuestions(){
		return questionRepository.findAll();
	}
	
	public Page<QuestionModel> getQuestions(Pageable pageable) {
	    return questionRepository.findAll(pageable);
	}

	public QuestionModel getQuestionById(int id) {
		return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
	}

	public void updateQuestion(QuestionModel updatedQuestion) {
	    Optional<QuestionModel> existingQuestionOpt = questionRepository.findById(updatedQuestion.getQuestion_id());

	    if (existingQuestionOpt.isPresent()) {
	        QuestionModel existingQuestion = existingQuestionOpt.get();
	        existingQuestion.setSubject(updatedQuestion.getSubject());
	        existingQuestion.setQuestion_text(updatedQuestion.getQuestion_text());
	        existingQuestion.setOption_a(updatedQuestion.getOption_a());
	        existingQuestion.setOption_b(updatedQuestion.getOption_b());
	        existingQuestion.setOption_c(updatedQuestion.getOption_c());
	        existingQuestion.setOption_d(updatedQuestion.getOption_d());
	        existingQuestion.setCorrect_option(updatedQuestion.getCorrect_option());
	        existingQuestion.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
	        existingQuestion.setStatus(updatedQuestion.isStatus());
	        questionRepository.save(existingQuestion); // save updates the record
	    } else {
	        throw new RuntimeException("Question not found with ID: " + updatedQuestion.getQuestion_id());
	    }
	}

	public void deleteById(int id) {
		questionRepository.deleteById(id);
	}
	
	public List<QuestionModel> findQuestionsBySubject(Long subjectId){
		return questionRepository.findBySubject_Id(subjectId);
	}
	
	public List<QuestionModel> findQuestionsBySubjectAndDifficulty(int subjectId, String difficulty) {
	    return questionRepository.findQuestionsBySubject_IdAndDifficultyLevel(subjectId, difficulty);
	}

}
