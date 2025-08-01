package com.personal.examportal.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.personal.examportal.model.SubjectModel;
import com.personal.examportal.service.SubjectService;

@Controller
public class SubjectController {
	@Autowired
	private SubjectService subjectService;
	
	@GetMapping("newsubject")
	public String newsub(Model model) {
		model.addAttribute("title", "Admin Dashboard | New Subject");
		model.addAttribute("subject", new SubjectModel());
		return "subject/add";
	}
	
	@PostMapping("/subject/save")
	public String saveSubject(@ModelAttribute SubjectModel subject,
	                          Model model,
	                          @RequestParam("imageFile") MultipartFile file,
	                          @RequestParam(value = "existingImage", required = false) String existingImage) throws IOException {

	    if (!file.isEmpty()) {
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        Path uploadPath = Paths.get("src/main/resources/static/uploads/subjects/");
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }
	        Path filePath = uploadPath.resolve(fileName);
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	        subject.setImage(fileName); // set new image
	    } else {
	        subject.setImage(existingImage); // retain old image
	    }

	    if (subject.getId() == null || subject.getId() == 0) {
	        subjectService.addSubject(subject);
	    } else {
	        subjectService.updateSubject(subject);
	    }

	    return "redirect:/subjects";
	}

	
	@GetMapping("subjects")
	public String getSubjects(Model model,
	                          @RequestParam(defaultValue = "0") int page,
	                          @RequestParam(defaultValue = "5") int size) {
	    Page<SubjectModel> subjectPage = subjectService.getSubjects(PageRequest.of(page, size));
	    model.addAttribute("subjectPage", subjectPage);
	    model.addAttribute("title", "Online Exam Portal | All Subjects");
	    return "subject/list";
	}
	
	@GetMapping("/subject/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
	    SubjectModel subject = subjectService.getSubjectById(id);
	    model.addAttribute("subject", subject);
	    return "subject/add";
	}
	
	@GetMapping("/subject/delete/{id}")
	public String deleteSubject(@PathVariable("id") Long id) {
	    subjectService.deleteById(id);
	    return "redirect:/subjects";
	}

	

}
