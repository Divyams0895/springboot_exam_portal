package com.personal.examportal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.personal.examportal.dto.ExamReportDTO;
import com.personal.examportal.dto.TopStudentDTO;
import com.personal.examportal.model.ResultModel;

@Service
public class ResultService {

	public Page<TopStudentDTO> getTopStudents(PageRequest of) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<ResultModel> getAllResults(PageRequest of) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<ExamReportDTO> getStudentsReport(PageRequest of) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
