package com.example.todo.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.todo.entity.Task;
import com.example.todo.form.TaskForm;
import com.example.todo.service.TaskService;
import com.example.todo.service.UserService;

import jakarta.validation.Valid;

//　Controllerクラス
@Controller
public class TaskController {
	
	private final UserService userService;
	private final TaskService taskService;
	private static final int PAGE_SIZE = 10;
	
	public TaskController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
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
		model.addAttribute("loginUsername", principal.getName());
		return "tasks";
	}

//	タスク更新画面　
	@GetMapping("/tasks/edit/{id}")
	public String editPage(@PathVariable("id") Long id, Principal principal, Model model) {
		Task task = taskService.getMyTask(id, principal.getName());
		TaskForm form = new TaskForm();
		form.setTitle(task.getTitle());
		form.setContent(task.getContent());
		form.setAssigneeName(task.getAssigneeName());
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
	public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("form") TaskForm form, BindingResult br,
			Principal principal, Model model) {
		if (br.hasErrors()) {
			model.addAttribute("taskId", id);
			return "edit";
		}
		taskService.update(id, principal.getName(), form);
		return "redirect:/tasks";
	}

	public UserService getUserService() {
		return userService;
	}
}