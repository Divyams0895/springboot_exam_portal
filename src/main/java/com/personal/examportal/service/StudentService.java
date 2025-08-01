package com.personal.examportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.personal.examportal.dto.UserDTO;
import com.personal.examportal.model.StudentModel;
import com.personal.examportal.model.SubjectModel;
import com.personal.examportal.repository.StudentRepository;


@Service
public class StudentService {
	@Autowired
	private StudentRepository studentRepository;
	
	public void saveStudent(StudentModel student) {
//		System.out.println("Check 9");
		studentRepository.save(student);
//		System.out.println("Check 10");
	}
	
	public List<StudentModel> allStudents(){
		return studentRepository.findAll();
	}
	
	public Page<StudentModel> getStudents(Pageable pageable) {
	    return studentRepository.findAll(pageable);
	}
	
	public Page<UserDTO> getStudentsByRole(Pageable pageable) {
	    return studentRepository.getAllStudentDetails(pageable);
	}
	
	public UserDTO getStudentProfile() {
		return studentRepository.getStudentDetails();
	}

}
