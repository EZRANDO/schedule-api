package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {
    ScheduleResponseDto saveSchedule(Schedule schedule);
    List<Schedule> findAllSchedules();
    Schedule findScheduleById(Long id);
    void deleteSchedule(Long id, ScheduleRequestDto requestDto);

}
