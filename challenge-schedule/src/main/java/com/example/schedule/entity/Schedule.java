package com.example.schedule.entity;

import com.example.schedule.dto.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * {@code Schedule} 클래스는 사용자 일정 정보를 나타냅니다.
 * <p>
 * 작성자, 비밀번호, 업무 내용, 마지막 수정일 등의 정보를 포함하며,
 * 일정 정보를 수정할 수 있는 기능을 제공합니다.
 *
 */
@Getter
@AllArgsConstructor
public class Schedule {

    /**
     * 일정의 고유 ID
     */
    private Long id;

    /**
     * 작성자 정보를 담는 Author 객체
     */
    private Author author;

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

    /**
     * 마지막 수정일
     */
    private LocalDateTime updated_at;

    /**
     * 일정 정보를 생성하는 생성자
     *
     * @param password   비밀번호
     * @param writer     작성자 이름
     * @param task       업무 내용
     * @param updatedAt  마지막 수정일
     */
    public Schedule(String password, String writer, String task, LocalDateTime updatedAt) {
        this.password = password;
        this.writer = writer;
        this.task = task;
        this.updated_at = updatedAt;
    }

    /**
     * 요청으로 받은 일정 정보로 현재 일정을 수정
     *
     * @param requestDto 수정할 일정 정보를 담은 DTO 객체
     */
    public void update(ScheduleRequestDto requestDto) {
        this.writer = requestDto.getWriter();
        this.task = requestDto.getTask();
        this.updated_at = LocalDateTime.now();
    }
}
