package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.entity.Schedule;

import java.util.Collection;

public interface ScheduleRepository {
    Schedule saveSchedule(Schedule schedule);
    Collection<Schedule> findAllSchedules();
    Schedule findScheduleById(Long id);
    void deleteSchedule(Long id, ScheduleRequestDto requestDto);

}
