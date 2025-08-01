package com.personal.examportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.personal.examportal.model.ExamModel;
import com.personal.examportal.repository.ExamRepository;


@Service
public class ExamService {
	
	@Autowired
	private ExamRepository examRepository;
	
	public void addExam(ExamModel examModel) {
		examRepository.save(examModel);
	}
	
	public List<ExamModel> listExams(){
		return examRepository.findAll();
	}
	
	public Page<ExamModel> getExams(Pageable pageable) {
	    return examRepository.findAll(pageable);
	}

	public ExamModel getExamById(Long id) {
		return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
	}

	public void updateExam(ExamModel updatedExam) {
	    Optional<ExamModel> existingExamOpt = examRepository.findById(updatedExam.getId());

	    if (existingExamOpt.isPresent()) {
	        ExamModel existingExam = existingExamOpt.get();
	        existingExam.setSubject(updatedExam.getSubject());
	        existingExam.setTitle(updatedExam.getTitle());
	        existingExam.setStatus(updatedExam.getStatus());
	        existingExam.setSelectedQuestions(updatedExam.getSelectedQuestions());
	        examRepository.save(existingExam); // save updates the record
	    } else {
	        throw new RuntimeException("Exam not found with ID: " + updatedExam.getId());
	    }
	}

	public void deleteById(Long id) {
		examRepository.deleteById(id);
	}

	public List<ExamModel> getExamsBySubjectId(Long subjectId) {
		return examRepository.findBySubjectId(subjectId);
	}
	
	public int getQuestionCountByExam(Long examId) {
	    return examRepository.countQuestionsByExamId(examId);
	}


}
