package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository {
    Optional<Schedule> findById(Long id);
    ScheduleResponseDto saveSchedule(Schedule schedule);
    List<ScheduleResponseDto> findAllSchedules(LocalDateTime updatedAt, String name);
    ScheduleResponseDto findScheduleById(Long id);
    Schedule updateTask(Schedule schedule);
    int deleteSchedule(Long id, ScheduleRequestDto requestDto);
   // void deleteSchedule(Long id, ScheduleRequestDto requestDto);


}
