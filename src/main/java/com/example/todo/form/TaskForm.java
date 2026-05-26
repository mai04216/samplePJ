package com.example.todo.form;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskForm {
	
	@NotBlank(message = "※タイトルは必須です")
	@Size(max = 100, message = "タイトルは100文字以内で入力してください")
	
	private String title;
	
	@NotBlank(message = "※内容は必須です")
	@Size(max = 1000, message = "内容は1000文字以内で入力してください")
	private String content;
	
    @NotBlank(message = "※登録者は必須です")
    @Size(max = 50)
    private String name;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    @AssertTrue(message = "※開始日は終了日より前の日付を指定してください")
    public boolean isValidDateRange() {  // メソッド名を変更
        if (startDate == null || endDate == null) return true;
        return !startDate.isAfter(endDate);
    }

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