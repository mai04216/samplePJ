package com.example.todo.service;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public String handleTypeMismatch(RedirectAttributes ra) {
		ra.addFlashAttribute("message", "不正なパラメータです");
		return "redirect:/tasks";
	}
}