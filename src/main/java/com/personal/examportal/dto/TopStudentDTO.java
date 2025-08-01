package com.personal.examportal.dto;

public class TopStudentDTO {
    private String name;
    private long examCount;
    private double averageScore;

    public TopStudentDTO(String name, long examCount, double averageScore) {
        this.name = name;
        this.examCount = examCount;
        this.averageScore = averageScore;
    }

	public String getName() {
		return name;
	}

	public long getExamCount() {
		return examCount;
	}

	public double getAverageScore() {
		return averageScore;
	}
}

