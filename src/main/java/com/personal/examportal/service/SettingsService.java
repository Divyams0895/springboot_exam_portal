package com.personal.examportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.examportal.model.SettingsModel;
import com.personal.examportal.repository.SettingsRepository;

@Service
public class SettingsService {

    @Autowired
    private SettingsRepository settingsRepository;

    public SettingsModel getSettings() {
        return settingsRepository.findById(1L).orElse(new SettingsModel());
    }

    public void saveSettings(SettingsModel settings) {
        SettingsModel existing = settingsRepository.findById(1L).orElse(null);
        
        if (existing != null) {
            settings.setId(existing.getId());
        } else {
            settings.setId(null); // Let it be saved as a new record
        }
        
        settingsRepository.save(settings);
        
    }
}

