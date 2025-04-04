package com.example.schedule.service;


import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto);
    List<ScheduleResponseDto> findAllSchedules(LocalDateTime updatedAt, String writer);
    ScheduleResponseDto findScheduleById(Long id);
    ScheduleResponseDto updateTask(Long id, String writer, String task);
    void deleteSchedule(Long id, ScheduleRequestDto requestDto);
}

