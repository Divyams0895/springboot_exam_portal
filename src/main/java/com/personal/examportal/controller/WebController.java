package com.personal.examportal.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.personal.examportal.model.ExamModel;
import com.personal.examportal.model.QuestionModel;
import com.personal.examportal.model.ResultModel;
import com.personal.examportal.model.StudentModel;
import com.personal.examportal.repository.ExamRepository;
import com.personal.examportal.repository.ResultRepository;
import com.personal.examportal.repository.StudentRepository;
import com.personal.examportal.service.EmailService;
import com.personal.examportal.service.ExamService;
import com.personal.examportal.service.QuestionService;
import com.personal.examportal.service.SettingsService;
import com.personal.examportal.service.SubjectService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {
	
	@Autowired
	private ExamService examService;
	
	@Autowired
	private SubjectService subjectService;
		
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private SettingsService settingsService;
	
	@Autowired
	private ExamRepository examRepository;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping("")
	public String webHome(HttpSession session,Model model) {
		model.addAttribute("activePage", "home");
		model.addAttribute("settings", settingsService.getSettings());
		return "web/home";
	}
	
	@RequestMapping("about")
	public String about(Model model) {
		model.addAttribute("activePage", "about");
		model.addAttribute("settings", settingsService.getSettings());
		return "web/about";
	}
	
	@RequestMapping("exams")
	public String exams(Model model, Principal principal) {
		model.addAttribute("activePage", "exams");
		 String email = principal.getName(); // Usually this is the username (email)
//		 System.out.println("Mail id : "+email);
	        StudentModel student = studentRepository.findByUsername(email); // Fetch user using email
//	        System.out.println(student.getFullName());
	        if (student != null) {
	            model.addAttribute("username", student.getFullName()); // Use full name
	        }
		model.addAttribute("subjects", subjectService.listBooks());
		model.addAttribute("exams", examService.listExams());
		model.addAttribute("settings", settingsService.getSettings());
		return "web/subjects";
	}
	

	@GetMapping("/exams/subject/{id}")
	public String viewExamsBySubject(@PathVariable Long id, Model model) {
	    List<ExamModel> exams = examService.getExamsBySubjectId(id);

	    // ✅ Create a map of examId -> question count
	    Map<Long, Integer> questionCounts = new HashMap<>();
	    for (ExamModel exam : exams) {
	        int count = exam.getSelectedQuestions().size(); // make sure selectedQuestions is loaded
	        questionCounts.put(exam.getId(), count);
	    }

	    model.addAttribute("exams", exams);
	    model.addAttribute("totalQuestions", questionCounts); // ✅ pass to view
	    model.addAttribute("subject", subjectService.getSubjectById(id));
	    model.addAttribute("settings", settingsService.getSettings());

	    return "web/exams";
	}

	@GetMapping("/exam/instruction/{id}")
	public String showExamInstruction(@PathVariable Long id, Model model, Principal principal) {
	    if (principal == null) {
	        return "redirect:/login";
	    }

	    ExamModel exam = examService.getExamById(id);
	    model.addAttribute("exam", exam);
	    model.addAttribute("settings", settingsService.getSettings());
	    model.addAttribute("totalQuestions", exam.getSelectedQuestions().size());

	    return "web/exam-instruction";
	}
	
	@GetMapping("exam/{examId}/start")
    public String startExam(@PathVariable Long examId, Model model, HttpSession session,
    		HttpServletRequest request) {
        ExamModel exam = examRepository.findById(examId).orElseThrow();
        List<QuestionModel> questions = exam.getSelectedQuestions();

        session.setAttribute("examId", examId);
        session.setAttribute("questions", questions);
        session.setAttribute("currentQuestionIndex", 0);
        session.setAttribute("answers", new HashMap<Integer, String>());
        session.setAttribute("startTime", LocalDateTime.now());

        model.addAttribute("duration", exam.getDuration());
        model.addAttribute("question", questions.get(0));
        model.addAttribute("index", 0);
        model.addAttribute("total", questions.size());
        model.addAttribute("settings", settingsService.getSettings());
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", token);
        return "web/question-page";
    }

    @PostMapping("/exam/next")
    public String nextQuestion(@RequestParam("selectedOption") String selectedOption,
                               @RequestParam("index") int index,
                               HttpSession session, Model model) {

        List<QuestionModel> questions = (List<QuestionModel>) session.getAttribute("questions");
        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
        answers.put(index, selectedOption);

        int nextIndex = index + 1;

        if (nextIndex < questions.size()) {
            model.addAttribute("question", questions.get(nextIndex));
            model.addAttribute("index", nextIndex);
            model.addAttribute("total", questions.size());
            return "web/question-fragment :: questionContent";
        } else {
            return "redirect:/exam/submit";
        }
    }

//    @GetMapping("exam/submit")
//    public String submitExam(HttpSession session, Model model,Principal principal) {
//        List<QuestionModel> questions = (List<QuestionModel>) session.getAttribute("questions");
//        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
//        Long examId = (Long) session.getAttribute("examId");
//        String email = principal.getName(); // Usually this is the username (email)
//		 System.out.println("Mail id : "+email);
//	        StudentModel student = studentRepository.findByUsername(email);
//
//        ExamModel exam = examRepository.findById(examId).orElseThrow();
//        int score = 0;
//
//        for (int i = 0; i < questions.size(); i++) {
//            String correct = questions.get(i).getCorrect_option();
//            String given = answers.get(i);
//            if (correct.equals(given)) {
//                score++;
//            }
//        }
//
//        ResultModel result = new ResultModel();
//        result.setExam(exam);
//        result.setStudent(student); // Set the student here
//        result.setSubject(exam.getSubject());
//        result.setTotalQuestions(questions.size());
//        result.setCorrectAnswers(score);
//        result.setSubmittedAt(LocalDateTime.now());
//        System.out.println("Student: " + student);
//        System.out.println("Student ID: " + (student != null ? student.getId() : "null"));
//        System.out.println("Exam ID: " + examId);
//        System.out.println("Exam: " + exam);
//        
//
//        resultRepository.save(result);
//        System.out.println("total score: "+score);
//        model.addAttribute("score", score);
//        model.addAttribute("total", questions.size());
//        model.addAttribute("answers", answers);
//        model.addAttribute("questions", questions);
//        model.addAttribute("settings", settingsService.getSettings());
//
////        session.invalidate();
//        return "web/result";
//    }
    
    @GetMapping("exam/submit")
    public String submitExam(HttpSession session, Model model, Principal principal) {
        // Get session attributes
        List<QuestionModel> questions = (List<QuestionModel>) session.getAttribute("questions");
        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
        Long examId = (Long) session.getAttribute("examId");

        // Get logged-in student
        String email = principal.getName();
        System.out.println("Mail id : " + email);
        StudentModel student = studentRepository.findByUsername(email);

        // Fetch the exam
        ExamModel exam = examRepository.findById(examId).orElseThrow();

        int score = 0;

        // Create a new map for normalized answers
        Map<Integer, String> normalizedAnswers = new HashMap<>();

        // Evaluate answers and normalize them
        for (int i = 0; i < questions.size(); i++) {
            String correct = questions.get(i).getCorrect_option();  // e.g., "A"
            String given = answers.get(i);                          // e.g., "option_a"

            // Normalize: if answer is like "option_a", extract the last char and make uppercase
            String normalized = null;
            if (given != null && given.startsWith("option_")) {
                normalized = given.substring("option_".length()).toUpperCase();
            } else {
                normalized = given;
            }

            // Store normalized answer for displaying in result page
            normalizedAnswers.put(i, normalized);

            // Compare normalized answer with correct answer
            if (correct != null && correct.equalsIgnoreCase(normalized)) {
                score++;
            }
        }

        // Create and save result
        ResultModel result = new ResultModel();
        result.setExam(exam);
        result.setStudent(student);
        result.setSubject(exam.getSubject()); // subject must not be null
        result.setTotalQuestions(questions.size());
        result.setCorrectAnswers(score);
        result.setSubmittedAt(LocalDateTime.now());

        resultRepository.save(result);

        // Add to model for result page
        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());
        model.addAttribute("answers", normalizedAnswers); // use normalized answers here
        model.addAttribute("questions", questions);
        model.addAttribute("settings", settingsService.getSettings());

        // Optionally clear session
        // session.invalidate();

        return "web/result";
    }

    
    // Show certificate
    
    @GetMapping("/exam/certificate")
    public String getCertificate(Model model, Principal principal, HttpSession session) {
        Long examId = (Long) session.getAttribute("examId");
        String email = principal.getName();
        StudentModel student = studentRepository.findByUsername(email);
        ExamModel exam = examRepository.findById(examId).orElseThrow();
        ResultModel result = resultRepository.findTopByStudentAndExam(student, exam);

        model.addAttribute("student", student);
        model.addAttribute("exam", exam);
        model.addAttribute("result", result);

        // Send certificate email asynchronously
        emailService.sendCertificate(student, exam, result);

        return "web/certificate :: certificateFragment"; 
    }



	@RequestMapping("contact")
	public String contact(Model model) {
		model.addAttribute("activePage", "contact");
		model.addAttribute("settings", settingsService.getSettings());
		return "web/contact";
	}
}
