package com.example.teamtaskmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate dueDate;
    private String priority;
    private String status;

    @ManyToOne
    private User assignedTo; // assigned user

    @ManyToOne
    private Project project; //project relation
}