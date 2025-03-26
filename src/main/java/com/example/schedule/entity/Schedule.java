package com.example.schedule.entity;

import com.example.schedule.dto.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    /** 고유 식별자 */
    private Long id;

    /** 비밀번호 */
    private String password;

    /** 작성자 */
    private String writer;

    /** 업무 내용 */
    private String task;

    /** 마지막 수정일 */
    private LocalDateTime update;//수정일

    /**
     * 요청 DTO를 기반으로 일정 정보를 수정합니다.
     * 수정 시점의 시간으로 update 필드도 갱신됩니다.
     *
     * @param requestDto 수정할 정보가 담긴 요청 DTO
     */
    public void update(ScheduleRequestDto requestDto) {
        this.writer = requestDto.getWriter();
        this.task = requestDto.getTask();
        this.update = LocalDateTime.now(); //생성 시 수정일도 동일
    }

}

