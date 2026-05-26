package com.example.demo;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

//　Controllerクラス

@Controller
public class PageController {

	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;

	private static final int PAGE_SIZE = 10;

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

		int safePage = Math.max(1, page); // 0や負数を1に丸める

		Page<Task> taskPage = taskService.findMyTasks(principal.getName(), PageRequest.of(safePage - 1, PAGE_SIZE));

		// 最大ページを超えていたら末尾ページに丸める
		if (taskPage.getTotalPages() > 0 && safePage > taskPage.getTotalPages()) {
			safePage = taskPage.getTotalPages();
			taskPage = taskService.findMyTasks(principal.getName(), PageRequest.of(safePage - 1, PAGE_SIZE));
		}

		model.addAttribute("taskPage", taskPage);
		model.addAttribute("currentPage", safePage);
		return "tasks";
	}

//	タスク更新画面　
	@GetMapping("/tasks/edit/{id}")
	public String editPage(@PathVariable("id") Long id, Principal principal, Model model) {
		Task task = taskService.getMyTask(id, principal.getName());
		TaskForm form = new TaskForm();
		form.setTitle(task.getTitle());
		form.setContent(task.getContent());
		form.setName(task.getName());
		form.setStartDate(task.getStartDate());
		form.setEndDate(task.getEndDate());
		model.addAttribute("form", form);
		model.addAttribute("taskId", id);
		return "edit";
	}

//	タスク削除処理
	@PostMapping("/tasks/delete/{id}")
	public String deleteTask(@PathVariable("id") Long id, Principal principal) {
		taskService.delete(id, principal.getName());
		return "redirect:/tasks";
	}

//	タスク新規作成画面
	@GetMapping("/tasks/new")
	public String newPage(Model model) {
		model.addAttribute("form", new TaskForm());
		return "new";
	}

	@PostMapping("/tasks")
	public String create(@Valid @ModelAttribute("form") TaskForm form, BindingResult br, Principal principal) {
		if (br.hasErrors())
			return "new";
		taskService.create(principal.getName(), form);
		return "redirect:/tasks";
	}

	@PostMapping("/tasks/update/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("form") TaskForm form, BindingResult br,
			Principal principal, Model model) {
		if (br.hasErrors()) {
			model.addAttribute("taskId", id);
			return "edit";
		}
		taskService.update(id, principal.getName(), form);
		return "redirect:/tasks";
	}
}