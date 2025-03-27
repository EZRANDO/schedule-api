package com.example.basicschedule.repository;

import com.example.basicschedule.dto.ScheduleRequestDto;
import com.example.basicschedule.entity.Schedule;

import java.util.Collection;

public interface ScheduleRepository {
    Schedule saveSchedule(Schedule schedule);
    Collection<Schedule> findAllSchedules();
    Schedule findScheduleById(Long id);
    void deleteSchedule(Long id, ScheduleRequestDto requestDto);

}
