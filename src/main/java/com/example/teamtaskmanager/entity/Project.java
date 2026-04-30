package com.example.teamtaskmanager.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    private User createdBy; //admin

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> members = new ArrayList<>();
}