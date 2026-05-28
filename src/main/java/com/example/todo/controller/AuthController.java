package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.todo.form.RegisterForm;
import com.example.todo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

//	トップ画面
	@GetMapping("")
	public String topPage() {
		return "top";
	}

	// ログイン画面
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	// 登録画面
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("form", new RegisterForm());
		return "register";
	}

	// 登録処理
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("form") RegisterForm form, BindingResult br, RedirectAttributes ra) {
		if (br.hasErrors())
			return "register";
		try {
			userService.register(form.getUsername(), form.getPassword());
			ra.addFlashAttribute("message", "登録が完了しました。ログインしてください。");
			return "redirect:/login";
		} catch (IllegalArgumentException e) {
			br.reject("duplicate", e.getMessage());
			return "register";
		}
	}
}