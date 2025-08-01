package com.personal.examportal.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.personal.examportal.dto.UserDTO;
import com.personal.examportal.repository.ExamRepository;
import com.personal.examportal.repository.QuestionRepository;
import com.personal.examportal.repository.StudentRepository;
import com.personal.examportal.repository.SubjectRepository;

@Service
public class DashboardService {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private ExamRepository examRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private StudentRepository studentRepository;
		
	public Map<String, Long> getCounts() {
		Map<String, Long> counts = new HashMap<>();
        counts.put("subjects", subjectRepository.count());
        counts.put("exams", examRepository.count());
        counts.put("questions", questionRepository.count());
        counts.put("students", studentRepository.count());
        return counts;
    }
	
	public UserDTO getUserData() {
		return studentRepository.getAdminDetails();
	}
	
	@Transactional
	public void updateAdminDetails(UserDTO dto) {
	    studentRepository.updateAdminDetails(
	        dto.getFullName(),
	        dto.getPhone(),
	        dto.getProfilePhoto(),
	        dto.isStatus(),
	        dto.getEmail()
	    );
	}

}
