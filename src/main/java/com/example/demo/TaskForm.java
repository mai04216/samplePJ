package com.example.demo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class TaskForm {
	
	@NotBlank(message = "※タイトルは必須です")
	private String title;
	
	@NotBlank(message = "※内容は必須です")
	private String content;
	
    
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}