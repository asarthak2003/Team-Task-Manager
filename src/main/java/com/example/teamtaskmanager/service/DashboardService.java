package com.example.teamtaskmanager.service;

import com.example.teamtaskmanager.entity.Task;
import com.example.teamtaskmanager.entity.User;
import com.example.teamtaskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {

    private final TaskRepository repo;

    public DashboardService(TaskRepository repo) {
        this.repo = repo;
    }

    public Map<String, Object> getDashboard(User user) {
        List<Task> tasks;

        if ("ADMIN".equals(user.getRole())) {
            tasks = repo.findAll();
        } else {
            tasks = repo.findByAssignedToId(user.getId());
        }

        int total = tasks.size();
        int todo = 0, inProgress = 0, done = 0, overdue = 0;
        Map<String, Integer> tasksPerUser = new HashMap<>();

        for (Task t : tasks) {
            String status = t.getStatus();
            if (status == null) continue;

            switch (status) {
                case "TODO" -> todo++;
                case "IN_PROGRESS" -> inProgress++;
                case "DONE" -> done++;
            }

            if (t.getDueDate() != null &&
                    t.getDueDate().isBefore(LocalDate.now()) &&
                    !"DONE".equals(status)) {
                overdue++;
            }

            if (t.getAssignedTo() != null) {
                String name = t.getAssignedTo().getName();
                tasksPerUser.put(name, tasksPerUser.getOrDefault(name, 0) + 1);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalTasks", total);
        response.put("todo", todo);
        response.put("inProgress", inProgress);
        response.put("done", done);
        response.put("overdue", overdue);
        response.put("tasksPerUser", tasksPerUser);
        return response;
    }
}