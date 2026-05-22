package com.example.demo;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserService userService;

//	TOP画面
	@GetMapping("/")
	public String top() {
		return "top";
	}

//	ログイン画面
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

//	登録画面
	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

//	登録後の画面(postで受取る)
	@PostMapping("/register")
	public String register(@RequestParam("username") String username, @RequestParam("password") String password,
			Model model, RedirectAttributes ra) {
		try {
			userService.register(username, password);
			ra.addFlashAttribute("message", "登録が完了しました。ログインしてください。");
			return "redirect:/login";
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			return "register";
		}

	}

//	タスク一覧画面
	@GetMapping("/tasks")
	public String taskPage(@RequestParam(name = "page", defaultValue = "1") int page, Principal principal,
			Model model) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		Page<Task> taskPage = taskRepository.findByUsername(principal.getName(), pageable);
		model.addAttribute("taskPage", taskPage);
		model.addAttribute("currentPage", page);
		return "tasks";
	}

//	タスク内容編集
	@GetMapping("/tasks/edit/{id}")
	public String editPage(@PathVariable("id") Long id, Principal principal, Model model) {
		Task task = taskRepository.findByIdAndUsername(id, principal.getName())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute("task", task);
		return "edit";
	}

//	タスク編集後更新
	@PostMapping("/tasks/update/{id}")
	public String updateTask(@PathVariable("id") Long id, @ModelAttribute Task task, Principal principal) {
		Task existingTask = taskRepository.findByIdAndUsername(id, principal.getName())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		existingTask.setTitle(task.getTitle());
		existingTask.setContent(task.getContent());
		existingTask.setName(task.getName());
		existingTask.setStartDate(task.getStartDate());
		existingTask.setEndDate(task.getEndDate());
		existingTask.setUpdatedAt(LocalDateTime.now());
		taskRepository.save(existingTask);
		return "redirect:/tasks";
	}

//	タスク削除処理
	@PostMapping("/tasks/delete/{id}")
	public String deleteTask(@PathVariable("id") Long id, Principal principal) {
		Task task = taskRepository.findByIdAndUsername(id, principal.getName())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		taskRepository.deleteById(id);
		return "redirect:/tasks";
	}

}