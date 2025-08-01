package com.personal.examportal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.personal.examportal.model.StudentModel;
import com.personal.examportal.model.UserModel;
import com.personal.examportal.repository.StudentRepository;
import com.personal.examportal.repository.UserRepository;
import com.personal.examportal.service.SettingsService;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;


@Controller
@RequestMapping("/profile")
public class StudentController {
  
    @Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private StudentRepository studentRepository;
		
	@Autowired
	private SettingsService settingsService;

    // Show profile dashboard
    @RequestMapping("")
	public String profile(HttpSession session,Model model) {
		model.addAttribute("activePage", "profile");
		String email = (String) session.getAttribute("username"); // Get email from session
        StudentModel student = studentRepository.findByUsername(email);
        if (student != null) {
            model.addAttribute("student", student);
        } else {
            return "redirect:/login"; // or show error
        }
//        model.addAttribute("examResults", resultService.getStudentResults(student));
        model.addAttribute("settings", settingsService.getSettings());
		return "web/profile";
	}
	
	@PostMapping("/update")
	public String updateProfile(@RequestParam("id")Long id,@RequestParam("fullname")String name,@RequestParam("phone")String phone,@RequestParam("email") String email){

	    // Retrieve existing student
	    StudentModel existingStudent = studentRepository.findById(id).orElse(null);
	    if (existingStudent == null) return "redirect:/profile";

	    // Update fields
	    existingStudent.setFullName(name);
	    existingStudent.setPhone(phone);

	 // Update email in UserModel
	    UserModel user = existingStudent.getUser();
	    user.setUsername(email);
	    userRepository.save(user); 

	    studentRepository.save(existingStudent);
	    return "redirect:/profile";
	}
	
	@PostMapping("/change-photo")
	public String changePhoto(@RequestParam("id") Long id,
	                          @RequestParam("profileImage") MultipartFile file) throws IOException {

	    StudentModel existingStudent = studentRepository.findById(id).orElse(null);
	    if (existingStudent == null) return "redirect:/profile";

	    if (!file.isEmpty()) {
	        String fileName = file.getOriginalFilename();

	        // Path to static/uploads inside the project directory
	        String uploadPath = new File("src/main/resources/static/uploads/").getAbsolutePath();

	        // Ensure the directory exists
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdirs();
	        }

	        // Save file
	        File saveFile = new File(uploadDir, fileName);
	        file.transferTo(saveFile);

	        // Save filename in DB
	        existingStudent.setProfilePhoto(fileName);
	        studentRepository.save(existingStudent);
	    }

	    return "redirect:/profile";
	}
	
	@PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpSession session,RedirectAttributes redirectAttributes,
                                 Model model) {

        // Get logged-in student from session
		String email = (String) session.getAttribute("username");
        StudentModel student = studentRepository.findByUsername(email);
        if (student == null) {
            return "redirect:/login";
        }

        UserModel user = student.getUser();

        // Check if current password is correct
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect.");
            return "redirect:/profile";
        }

        // Save new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
        return "redirect:/profile";
    }
}

