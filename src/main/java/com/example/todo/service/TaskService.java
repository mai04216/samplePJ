package com.example.todo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.todo.entity.Task;
import com.example.todo.form.TaskForm;
import com.example.todo.repository.TaskMapper;

@Service
public class TaskService {
    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Transactional
    public void update(Long id, String username, TaskForm form) {
        Task task = taskMapper.findByIdAndUsername(id, username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        task.setTitle(form.getTitle());
        task.setContent(form.getContent());
        task.setAssigneeName(form.getAssigneeName());
        task.setStartDate(form.getStartDate());
        task.setEndDate(form.getEndDate());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateMyTask(task);  // MyBatisは明示的にUPDATEを呼ぶ
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
        taskMapper.insert(task);
    }

    @Transactional(readOnly = true)
    public List<Task> findMyTasks(String username, int page, int size) {
        int offset = page * size;
        return taskMapper.findByUsername(username, size, offset);
    }

    public long countMyTasks(String username) {
        return taskMapper.countByUsername(username);
    }

    @Transactional(readOnly = true)
    public Task getMyTask(Long id, String username) {
        return taskMapper.findByIdAndUsername(id, username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void delete(Long id, String username) {
        getMyTask(id, username);  // 所有者チェック
        taskMapper.deleteByIdAndUsername(id, username);
    }
}