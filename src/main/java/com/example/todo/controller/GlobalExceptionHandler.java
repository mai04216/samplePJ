package com.example.todo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public String handleTypeMismatch(MethodArgumentTypeMismatchException e, RedirectAttributes ra) {
		log.warn("不正なパラメータ: name={}, value={}", e.getName(), e.getValue());
		ra.addFlashAttribute("message", "不正なパラメータです");
		return "redirect:/tasks";
	}

	@ExceptionHandler(ResponseStatusException.class)
	public String handleResponseStatus(ResponseStatusException e, Model model) {
		if (e.getStatusCode().value() == 404) {
			return "error/404";
		}
		log.error("予期せぬエラー", e);
		return "error/500";
	}
}