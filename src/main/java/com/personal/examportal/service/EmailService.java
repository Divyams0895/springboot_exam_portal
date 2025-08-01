package com.personal.examportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.personal.examportal.model.ExamModel;
import com.personal.examportal.model.ResultModel;
import com.personal.examportal.model.StudentModel;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    
    public void sendCertificate(StudentModel student, ExamModel exam, ResultModel result) {
        String to = student.getUser().getUsername(); // email id
        String subject = "Your Exam Certificate - " + exam.getTitle();
        String text = "Dear " + student.getFullName() + ",\n\n"
                + "Congratulations! You have completed the exam " + exam.getTitle() + ".\n"
                + "Score: " + result.getCorrectAnswers() + "/" + result.getTotalQuestions() + "\n\n"
                + "Best regards,\nThinkTrack Online Exam Portal Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}

