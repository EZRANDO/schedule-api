package com.example.basicschedule.dto;

import com.example.basicschedule.entity.Schedule;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

/**
 * {@code ScheduleResponseDto}는 Schedule 엔티티 객체를 클라이언트에게 반환할 형식으로 변환하는 DTO 클래스입니다.
 * <p>
 * 날짜는 "yyyy-MM-dd" 형식의 문자열로 포맷팅됩니다.
 */

@Getter
public class ScheduleResponseDto {
    /** 일정의 고유 ID */
    private Long id;
    /** 작성자 */
    private String writer;
    /**작업 내용*/
    private String task;
    /**일정 마지막 수정일*/
    private String update;

    /**날짜 포맷 지정자*/
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Schedule 엔티티를 기반으로 ScheduleResponseDto객체 생성
     * @param schedule 변환할 Schedule엔티티 객체
     */
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.writer = schedule.getWriter();
        this.task = schedule.getTask();
        this.update = schedule.getUpdate().format(formatter);
    }
}
//https://velog.io/@jrl103/Java-%EC%9D%BC%EC%A0%95-%EA%B4%80%EB%A6%AC-%EC%95%B1-%EB%A7%8C%EB%93%A4%EA%B8%B0-%EA%B3%BC%EC%A0%9C