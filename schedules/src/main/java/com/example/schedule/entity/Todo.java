package com.example.schedule.entity;


import lombok.Getter;

@Getter
public class Todo {
    private Long id;
    private String task;
    private Long authorId; // 이것만 있으면 됨

    public Todo(String task) {
        this.task = task;
    }
}