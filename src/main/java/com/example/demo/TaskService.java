package com.example.demo;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        task.setName(form.getName());
        task.setStartDate(form.getStartDate());
        task.setEndDate(form.getEndDate());
        task.setUpdatedAt(LocalDateTime.now());
    }
}