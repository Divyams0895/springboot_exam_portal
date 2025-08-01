package com.personal.examportal.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.personal.examportal.dto.StudentRegistrationDTO;
import com.personal.examportal.model.PasswordResetToken;
import com.personal.examportal.model.StudentModel;
import com.personal.examportal.model.UserModel;
import com.personal.examportal.repository.TokenRepository;
import com.personal.examportal.repository.UserRepository;
import com.personal.examportal.service.EmailService;
import com.personal.examportal.service.StudentService;
import com.personal.examportal.service.UserService;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("studentDto", new StudentRegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("studentDto") StudentRegistrationDTO dto,
    		 			   @RequestParam("profilePhoto") MultipartFile file) throws IOException {
    	String fileName = file.getOriginalFilename();
        Path uploadPath = Paths.get("src/main/resources/static/uploads/");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        dto.setProfilePhoto(fileName);

    	UserModel user=new UserModel();
    	user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ROLE_STUDENT"); // default role
        userService.saveUser(user);
        
        StudentModel student = new StudentModel();
        student.setFullName(dto.getFullName());
        student.setPhone(dto.getPhone());
        student.setProfilePhoto(fileName);
        student.setStatus(true);
        student.setUser(user);
        studentService.saveStudent(student);
        
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("home")
    public String webHome() {
    	return "web/home";
    }
    
    @GetMapping("/session-timeout")
    public String sessionTimeout(Model model) {
        model.addAttribute("timeout", true);
        return "login";
    }
    
    @GetMapping("forgot-password")
    public String forgotPassword() {
    	return "forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        // Check if user exists
        UserModel user = userRepository.findByUsername(email);
        if (user == null) {
            model.addAttribute("error", "Email not found");
            return "forgot-password";
        }

        // Generate token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);

        // Send email with reset link
        String resetLink = "http://localhost:9000/reset-password?token=" + token;
        emailService.sendEmail(email, "Password Reset", 
            "Click to reset: " + resetLink);

        model.addAttribute("message", "Reset link sent to your email.");
        return "forgot-password";
    }
    
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password"; // Must match the HTML filename
    }
    
   
    @PostMapping("/reset-password")
    public String handleReset(@RequestParam("token") String token,
                              @RequestParam("password") String password,
                              Model model) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token");
            return "reset-password";
        }

        String email = resetToken.getEmail();
        UserModel user = userRepository.findByUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        // Optionally delete the token after use
        tokenRepository.delete(resetToken);

        model.addAttribute("message", "Password reset successful. You may now log in.");
        return "login";
    }

}

