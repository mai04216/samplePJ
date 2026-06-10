package com.example.todo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterForm {
	@NotBlank(message = "※ユーザー名は必須です")
	@Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "ユーザー名は半角英字のみ")
	private String username;

	@NotBlank(message = "※パスワードは必須です")
	@Size(min = 8, max = 100, message = "パスワードは最低8文字最大100文字で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "パスワードは半角英数字のみ")
	private String password;

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
}