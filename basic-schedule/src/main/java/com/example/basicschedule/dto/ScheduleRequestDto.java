package com.example.basicschedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * {@code ScheduleRequestDto}는 클라이언트가 일정 생성을 요청할 때 사용하는 데이터 전송 객체(DTO)입니다.
 * <p>
 * 비밀번호, 작성자, 업무 내용은 필수 입력 항목입니다.
 */

@Getter
public class ScheduleRequestDto {

    /**비밀번호*/
    @NotNull
    private String password;

    /**작성자*/
    @NotNull
    private String writer;

    /**업무 내용*/
    @NotNull
    private String task;

}
