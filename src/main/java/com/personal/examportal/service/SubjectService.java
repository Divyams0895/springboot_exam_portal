package com.personal.examportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.personal.examportal.model.SubjectModel;
import com.personal.examportal.repository.SubjectRepository;

@Service
public class SubjectService {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	public void addSubject(SubjectModel subjectModel) {
		subjectRepository.save(subjectModel);
	}
	
	public List<SubjectModel> listBooks(){
		return subjectRepository.findAll();
	}
	
	public Page<SubjectModel> getSubjects(Pageable pageable) {
	    return subjectRepository.findAll(pageable);
	}

	public SubjectModel getSubjectById(Long id) {
		return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
	}

	public void updateSubject(SubjectModel updatedSubject) {
	    Optional<SubjectModel> existingSubjectOpt = subjectRepository.findById(updatedSubject.getId());

	    if (existingSubjectOpt.isPresent()) {
	        SubjectModel existingSubject = existingSubjectOpt.get();
	        existingSubject.setSubjectName(updatedSubject.getSubjectName());
	        existingSubject.setDesc(updatedSubject.getDesc());
	        existingSubject.setStatus(updatedSubject.isStatus());
	        existingSubject.setImage(updatedSubject.getImage()); 
	        subjectRepository.save(existingSubject); // save updates the record
	    } else {
	        throw new RuntimeException("Subject not found with ID: " + updatedSubject.getId());
	    }
	}

	public void deleteById(Long id) {
		subjectRepository.deleteById(id);
	}

}
