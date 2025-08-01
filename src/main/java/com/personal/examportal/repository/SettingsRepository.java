package com.personal.examportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.examportal.model.SettingsModel;

public interface SettingsRepository extends JpaRepository<SettingsModel, Long> {
  
}
