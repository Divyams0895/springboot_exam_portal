package com.personal.examportal.dto;

public class StudentRegistrationDTO {
    private String fullName;
    private String phone;
//    private String profilePhoto;
    private String username; // this is email
    private String password;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
//	public String getProfilePhoto() {
//		return profilePhoto;
//	}
//	public void setProfilePhoto(String profilePhoto) {
//		this.profilePhoto = profilePhoto;
//	} 
	
	
    
}

