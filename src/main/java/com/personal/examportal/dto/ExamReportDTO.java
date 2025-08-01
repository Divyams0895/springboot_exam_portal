package com.personal.examportal.dto;

public class ExamReportDTO {
    private String examName;
    private String subject;
    private long totalStudents;
    private double averageMarks;
    private int highestMarks;
    private int lowestMarks;
    private double passPercentage;
    
    
	public ExamReportDTO(String examName, String subject, long totalStudents, double averageMarks, int highestMarks,
			int lowestMarks, double passPercentage) {
		this.examName = examName;
		this.subject = subject;
		this.totalStudents = totalStudents;
		this.averageMarks = averageMarks;
		this.highestMarks = highestMarks;
		this.lowestMarks = lowestMarks;
		this.passPercentage = passPercentage;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public long getTotalStudents() {
		return totalStudents;
	}
	public void setTotalStudents(long totalStudents) {
		this.totalStudents = totalStudents;
	}
	public double getAverageMarks() {
		return averageMarks;
	}
	public void setAverageMarks(double averageMarks) {
		this.averageMarks = averageMarks;
	}
	public int getHighestMarks() {
		return highestMarks;
	}
	public void setHighestMarks(int highestMarks) {
		this.highestMarks = highestMarks;
	}
	public int getLowestMarks() {
		return lowestMarks;
	}
	public void setLowestMarks(int lowestMarks) {
		this.lowestMarks = lowestMarks;
	}
	public double getPassPercentage() {
		return passPercentage;
	}
	public void setPassPercentage(double passPercentage) {
		this.passPercentage = passPercentage;
	}

    // Constructor, getters, setters
}

