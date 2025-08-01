package com.personal.examportal.dto;

public class UserDTO {
	private int id;
    private String fullName;
    private String phone;
    private String profilePhoto;
    private Boolean status;
    private String email;

    public UserDTO(String fullName, String phone, String profilePhoto, Boolean status, String email) {
        this.fullName = fullName;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.status = status;
        this.email = email;
    }
    
    
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    
}

