package com.personal.examportal.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.personal.examportal.dto.ExamReportDTO;
import com.personal.examportal.dto.TopStudentDTO;
import com.personal.examportal.dto.UserDTO;
import com.personal.examportal.model.ResultModel;
import com.personal.examportal.repository.ResultRepository;
import com.personal.examportal.service.DashboardService;
import com.personal.examportal.service.ResultService;
import com.personal.examportal.service.StudentService;

@Controller
public class AdminController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private DashboardService dashboardService;
	
	@Autowired
	private ResultService resultService;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@RequestMapping("dashboard")
	public String home(Model model) {
		model.addAttribute("title", "Admin Dashboard");
		Map<String, Long> counts = dashboardService.getCounts();
        model.addAttribute("subjectCount", counts.get("subjects"));
        model.addAttribute("examCount", counts.get("exams"));
        model.addAttribute("questionCount", counts.get("questions"));
        model.addAttribute("studentCount", counts.get("students"));
        model.addAttribute("topStudents", resultService.getTopStudents(PageRequest.of(0, 5)));
  
		return "index";
	}
	
	@RequestMapping("admin/profile")
	public String profile(Model model) {
		model.addAttribute("user", dashboardService.getUserData());
		model.addAttribute("title", "Online Exam Portal | Admin Profile");
		return "profile";
	}
	
	@GetMapping("students/list")
	public String getSubjects(Model model,
	                          @RequestParam(defaultValue = "0") int page,
	                          @RequestParam(defaultValue = "5") int size) {
	    Page<UserDTO> studentPage = studentService.getStudentsByRole(PageRequest.of(page, size));
	    model.addAttribute("studentPage", studentPage);
	    model.addAttribute("title", "Online Exam Portal | Students");
	    return "student/list";
	}
	
	@GetMapping("results")
	public String resultList(Model model,
	                          @RequestParam(defaultValue = "0") int page,
	                          @RequestParam(defaultValue = "5") int size) {
	    Page<ResultModel> resultPage = resultService.getAllResults(PageRequest.of(page, size));
	    model.addAttribute("resultPage", resultPage);
	    model.addAttribute("title", "Online Exam Portal | Exam Results");
	    return "results/list";
	}
	
	@GetMapping("students/reports")
	public String showExamReports(Model model,
								@RequestParam(defaultValue = "0") int page,
								@RequestParam(defaultValue = "5") int size) {
	    Page<ExamReportDTO> reportsPage = resultService.getStudentsReport(PageRequest.of(page, size)); // assuming passMark = 3
	    model.addAttribute("reportsPage", reportsPage);
	    model.addAttribute("title", "Online Exam Portal | Exam Reports");
	    return "reports/list"; // Thymeleaf template
	}
	
	@GetMapping("students/performance")
	public String showStudentsPerformance(Model model,
								@RequestParam(defaultValue = "0") int page,
								@RequestParam(defaultValue = "5") int size) {
	    Page<TopStudentDTO> performancePage = resultService.getTopStudents(PageRequest.of(page, size)); // assuming passMark = 3
	    model.addAttribute("performancePage", performancePage);
	    model.addAttribute("title", "Online Exam Portal | Students Performance");
	    return "reports/performance-list"; // Thymeleaf template
	}
	
	@RequestMapping("profile/edit")
	public String editProfile(Model model) {
		model.addAttribute("user", dashboardService.getUserData());
		model.addAttribute("title", "Online Exam Portal | Profile Edit");
		return "view";
	}
	
	@PostMapping("/update")
	public String updateAdminProfile(@ModelAttribute("user") UserDTO userDTO,
	                                 @RequestParam("profileImage") MultipartFile file,
	                                 @RequestParam(value = "existingImage", required = false) String existingImage,
	                                 Model model,RedirectAttributes redirectAttributes) {
	    try {
	        // Save new image if uploaded
	        String fileName = existingImage;
	        if (!file.isEmpty()) {
	            fileName = file.getOriginalFilename();
	            Path uploadPath = Paths.get("src/main/resources/static/uploads");
	            if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);
	            }
	            Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
	        }

	        userDTO.setProfilePhoto(fileName);

	        // ✅ Fetch current admin details to fill required fields
	        UserDTO currentUser = dashboardService.getUserData(); // assumed to return logged-in admin
	        userDTO.setEmail(currentUser.getEmail());
	        userDTO.setStatus(currentUser.isStatus());

	        dashboardService.updateAdminDetails(userDTO);

	        redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");
	    } catch (Exception e) {
	        model.addAttribute("error", "Failed to update profile.");
	        e.printStackTrace();
	    }

	    return "redirect:/admin/profile";
	}
}
