package com.personal.examportal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personal.examportal.model.ExamModel;
import com.personal.examportal.model.QuestionModel;
import com.personal.examportal.repository.QuestionRepository;
import com.personal.examportal.service.ExamService;
import com.personal.examportal.service.QuestionService;
import com.personal.examportal.service.SubjectService;


@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamService examService;
    
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/newexam")
    public String showCreateExamForm(Model model) {
        model.addAttribute("subjects", subjectService.listBooks());
        model.addAttribute("exam", new ExamModel());
        model.addAttribute("selectedQuestionIds", new ArrayList<Integer>()); // ✅ Add this line
        model.addAttribute("title", "Online Exam Portal | New Exam");
        return "exam/add";
    }


    @PostMapping("/save")
    public String saveExam(@ModelAttribute ExamModel exam,
                           @RequestParam("selectedQuestionIds") List<Integer> questionIds) {
        List<QuestionModel> selectedQuestions = questionRepository.findAllById(questionIds);
        exam.setSelectedQuestions(selectedQuestions);
        examService.addExam(exam);
        return "redirect:/exam/list";
    }
    
    @GetMapping("/list")
    public String listExams(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
		Page<ExamModel> examPage = examService.getExams(PageRequest.of(page, size));
		model.addAttribute("examPage", examPage);
		model.addAttribute("title", "Online Exam Portal | All Exams");
		return "exam/list";
    }


    
    @GetMapping("/questions/{subjectId}/{difficulty}")
    @ResponseBody
    public List<QuestionModel> getQuestionsBySubjectAndDifficulty(@PathVariable int subjectId,
                                                                  @PathVariable String difficulty) {
        return questionService.findQuestionsBySubjectAndDifficulty(subjectId, difficulty);
    }

	
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ExamModel exam = examService.getExamById(id);
        model.addAttribute("exam", exam);
        model.addAttribute("subjects", subjectService.listBooks());
        model.addAttribute("questions", questionService.allQuestions());

        List<Integer> selectedIds = exam.getSelectedQuestions()
                                        .stream()
                                        .map(q->q.getQuestion_id())
                                        .collect(Collectors.toList());
        model.addAttribute("selectedQuestionIds", selectedIds); // ✅ Add this
        return "exam/add";
    }
    
    @PostMapping("/update/{id}")
    public String updateExam(@PathVariable Long id,
                             @ModelAttribute ExamModel exam,
                             @RequestParam("selectedQuestionIds") List<Integer> questionIds) {

        List<QuestionModel> selectedQuestions = questionRepository.findAllById(questionIds);
        System.out.println("selected ids are: "+selectedQuestions);
        exam.setSelectedQuestions(selectedQuestions);
        exam.setId(id); // Make sure the ID is set correctly

        examService.updateExam(exam); // You should have a method in service for update

        return "redirect:/exam/list";
    }
	
	@GetMapping("/delete/{id}")
	public String deleteexam(@PathVariable("id") Long id) {
	    examService.deleteById(id);
	    return "redirect:/exam/list";
	}
	
		

}
