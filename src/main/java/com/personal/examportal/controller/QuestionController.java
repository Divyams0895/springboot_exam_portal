package com.personal.examportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.personal.examportal.model.QuestionModel;
import com.personal.examportal.service.QuestionService;
import com.personal.examportal.service.SubjectService;


@Controller
public class QuestionController {
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private SubjectService subjectService;
	
	@GetMapping("newquestion")
	public String newquestion(Model model) {
		model.addAttribute("title", "Admin Dashboard | New Question");
		model.addAttribute("question", new QuestionModel());
		model.addAttribute("subjects",subjectService.listBooks());
		return "question/add";
	}
	
	@PostMapping("/question/save")
	public String saveQuestion(@ModelAttribute QuestionModel question,Model model) {
	    if (question.getQuestion_id() == 0) {
	        // Add new subject
	        questionService.addQuestion(question);
	    } else {
	        // Update existing subject
	        questionService.updateQuestion(question);
	    }
	    model.addAttribute("title", "Admin Dashboard | Question List");
	    return "redirect:/questions";
	}

	
	@GetMapping("questions")
	public String getQuestions(Model model,
	                          @RequestParam(defaultValue = "0") int page,
	                          @RequestParam(defaultValue = "5") int size) {
	    Page<QuestionModel> questionPage = questionService.getQuestions(PageRequest.of(page, size));
	    model.addAttribute("questionPage", questionPage);
	    model.addAttribute("title", "Online Exam Portal | All Questions");
	    return "question/list";
	}
	
	@GetMapping("/question/edit/{id}")
	public String showEditForm(@PathVariable int id, Model model) {
	    QuestionModel question = questionService.getQuestionById(id);
	    model.addAttribute("question", question);
		model.addAttribute("subjects",subjectService.listBooks());
	    return "question/add";
	}
	
	@GetMapping("/question/delete/{id}")
	public String deleteQuestion(@PathVariable("id") int id) {
	    questionService.deleteById(id);
	    return "redirect:/questions";
	}

	

}
