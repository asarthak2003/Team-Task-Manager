package com.example.teamtaskmanager.repository;

import com.example.teamtaskmanager.entity.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByAssignedToId(Long userId);
}