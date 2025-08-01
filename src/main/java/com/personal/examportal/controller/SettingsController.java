package com.personal.examportal.controller;

import java.nio.file.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.personal.examportal.model.SettingsModel;
import com.personal.examportal.service.SettingsService;

@Controller
@RequestMapping("/settings")
public class SettingsController {

	@Autowired
    private SettingsService settingsService;

    @GetMapping
    public String showPortalSettings(Model model) {
        model.addAttribute("settings", settingsService.getSettings());
        model.addAttribute("title", "Online Exam Portal | Settings");
        return "portal-settings"; // portal-settings.html Thymeleaf view
    }

    @PostMapping("/update")
    public String updateSettings(@ModelAttribute("settings") SettingsModel settings,
                                 @RequestParam("logoFile") MultipartFile logoFile) {
        try {
            if (!logoFile.isEmpty()) {
                String fileName = logoFile.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(logoFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                settings.setLogo(fileName);
            }
            settingsService.saveSettings(settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings";
    }
}
