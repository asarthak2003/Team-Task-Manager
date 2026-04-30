package com.example.teamtaskmanager.service;

import com.example.teamtaskmanager.entity.Project;
import com.example.teamtaskmanager.entity.User;
import com.example.teamtaskmanager.repository.ProjectRepository;
import com.example.teamtaskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository repo;
    private final UserRepository userRepo;

    public ProjectService(ProjectRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public Project create(Project p, User user) {
        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Only admin can create project");
        }
        p.setCreatedBy(user);
        p.setMembers(new ArrayList<>(List.of(user)));
        return repo.save(p);
    }

    public List<Project> getAll(User user) {
        if ("ADMIN".equals(user.getRole())) {
            return repo.findAll();
        }
        return repo.findByMembers_Id(user.getId());
    }

    public Project addMember(Long projectId, Long userId, User admin) {
        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Only admin can add members");
        }
        Project project = repo.findById(projectId).orElseThrow();
        User newUser = userRepo.findById(userId).orElseThrow();

        boolean alreadyMember = project.getMembers().stream()
                .anyMatch(m -> m.getId().equals(userId));
        if (!alreadyMember) {
            project.getMembers().add(newUser);
        }
        return repo.save(project);
    }

    public Project removeMember(Long projectId, Long userId, User admin) {
        if (!"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Only admin can remove members");
        }
        Project project = repo.findById(projectId).orElseThrow();
        project.getMembers().removeIf(m -> m.getId().equals(userId));
        return repo.save(project);
    }

    public void delete(Long id, User user) {
        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Only admin can delete project");
        }
        repo.deleteById(id);
    }
}