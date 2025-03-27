package com.example.basicschedule.entity;

import com.example.basicschedule.dto.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * {@code Schedule}은 Schedule 사용자 정보를 저장하기 위한 클래스입니다.
 * <p>
 * 작성자, 비밀번호, 업무 내용, 마지막 수정일 등을 포함하며,
 * 일정 정보를 수정하는 기능도 제공합니다.
 */

@Getter
@AllArgsConstructor
public class Schedule {

    @Setter
    private Long id;
    private String password;
    private String writer;
    private String task;
    private LocalDateTime update;//수정일

    public Schedule(String password, String writer, String task, LocalDateTime update) {
        this.password = password;
        this.writer = writer;
        this.task = task;
        this.update = update;
    }


    public void update(ScheduleRequestDto requestDto) {
        this.writer = requestDto.getWriter();
        this.task = requestDto.getTask();
        this.update = LocalDateTime.now(); //생성 시 수정일도 동일
    }


}

