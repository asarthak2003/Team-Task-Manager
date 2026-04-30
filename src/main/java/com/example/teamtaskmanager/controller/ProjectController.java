package com.example.teamtaskmanager.controller;

import com.example.teamtaskmanager.entity.Project;
import com.example.teamtaskmanager.entity.User;
import com.example.teamtaskmanager.service.ProjectService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public Project create(@RequestBody Project p) {
        return service.create(p, getUser());
    }

    @GetMapping
    public List<Project> getAll() {
        return service.getAll(getUser());
    }

    @PostMapping("/{id}/add-member/{userId}")
    public Project addMember(@PathVariable Long id, @PathVariable Long userId) {
        return service.addMember(id, userId, getUser());
    }

    @DeleteMapping("/{id}/remove-member/{userId}")
    public Project removeMember(@PathVariable Long id, @PathVariable Long userId) {
        return service.removeMember(id, userId, getUser());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id, getUser());
    }
}