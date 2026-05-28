package com.example.todo.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.todo.entity.Task;
import com.example.todo.form.TaskForm;
import com.example.todo.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void update(Long id, String username, TaskForm form) {
        Task task = taskRepository.findByIdAndUsername(id, username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        task.setTitle(form.getTitle());
        task.setContent(form.getContent());
        task.setAssigneeName(form.getAssigneeName());
        task.setStartDate(form.getStartDate());
        task.setEndDate(form.getEndDate());
        task.setUpdatedAt(LocalDateTime.now());
    }
    
    @Transactional
    public void create(String username, TaskForm form) {
        Task task = new Task();
        task.setUsername(username);
        task.setTitle(form.getTitle());
        task.setContent(form.getContent());
        task.setAssigneeName(form.getAssigneeName());
        task.setStartDate(form.getStartDate());
        task.setEndDate(form.getEndDate());
        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        taskRepository.save(task);
    }
    
    @Transactional(readOnly = true)
    public Page<Task> findMyTasks(String username, Pageable pageable) {
        return taskRepository.findByUsername(username, pageable);
    }

    @Transactional(readOnly = true)
    public Task getMyTask(Long id, String username) {
        return taskRepository.findByIdAndUsername(id, username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void delete(Long id, String username) {
        Task task = getMyTask(id, username);
        taskRepository.delete(task);
    }
}