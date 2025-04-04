package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * {@code Schedule} 클래스는 사용자 일정 정보를 나타냅니다.
 * <p>
 * 작성자, 비밀번호, 업무 내용, 마지막 수정일 등의 정보를 포함하며,
 * 일정 정보를 수정할 수 있는 기능을 제공합니다.
 *
 */
@Setter
@Getter
@AllArgsConstructor
public class Schedule {

    /**
     * 일정의 고유 ID
     */

    private Long id;

    /**
     * 일정 비밀번호 (수정, 삭제 시 사용)
     */
    private String password;

    /**
     * 작성자 이름
     */
    private String writer;

    /**
     * 업무 내용
     */
    private String task;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public Schedule(String password, String writer, String task, LocalDateTime updatedAt) {
        this.password = password;
        this.writer = writer;
        this.task = task;
        this.updatedAt = updatedAt;
    }

    public Schedule(long id, String task, String password, String writer, LocalDateTime updatedAt) {
        this.id = id;
        this.task = task;
        this.password = password;
        this.writer = writer;
        this.updatedAt = updatedAt;
    }

//    public void update(ScheduleRequestDto requestDto) {
//        this.writer = requestDto.getWriter();
//        this.task = requestDto.getTask();
//        this.updatedAt = LocalDateTime.now();
    }

