package com.example.teamtaskmanager.service;

import com.example.teamtaskmanager.entity.*;
import com.example.teamtaskmanager.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public TaskService(TaskRepository repo, ProjectRepository projectRepo, UserRepository userRepo) {
        this.repo = repo;
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    public Task create(Task t, Long projectId, Long userId, User admin) {
        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Only admin can create task");
        }
        Project project = projectRepo.findById(projectId).orElseThrow();
        User assignedUser = userRepo.findById(userId).orElseThrow();
        t.setProject(project);
        t.setAssignedTo(assignedUser);
        return repo.save(t);
    }

    public List<Task> getAll(User user) {
        if ("ADMIN".equals(user.getRole())) {
            return repo.findAll();
        }
        return repo.findByAssignedToId(user.getId());
    }

    public Task update(Long id, Task t, User user) {
        Task existing = repo.findById(id).orElseThrow();
        if (!"ADMIN".equals(user.getRole()) &&
                !existing.getAssignedTo().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed");
        }
        existing.setStatus(t.getStatus());
        return repo.save(existing);
    }

    public void delete(Long id, User user) {
        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Only admin can delete");
        }
        repo.deleteById(id);
    }
}