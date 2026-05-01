package com.example.teamtaskmanager.controller;

import com.example.teamtaskmanager.entity.Task;
import com.example.teamtaskmanager.entity.User;
import com.example.teamtaskmanager.service.TaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public Task create(@RequestBody Task t,
                       @RequestParam Long projectId,
                       @RequestParam Long userId) {
        return service.create(t, projectId, userId, getUser());
    }

    @GetMapping
    public List<Task> getAll() {
        return service.getAll(getUser());
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task t) {
        return service.update(id, t, getUser());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id, getUser());
    }
}