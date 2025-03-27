package com.example.basicschedule.repository;

import com.example.basicschedule.dto.ScheduleRequestDto;
import com.example.basicschedule.entity.Schedule;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final Map<Long, Schedule> scheduleMap = new HashMap<>();

    @Override
    public Schedule saveSchedule(Schedule schedule) {
        Long scheduleId = scheduleMap.isEmpty() ? 1 : Collections.max(scheduleMap.keySet()) + 1;

        schedule.setId(scheduleId);

        LocalDateTime update = LocalDateTime.now();

        scheduleMap.put(scheduleId, schedule);

        return schedule;
    }

    @Override
    public Collection<Schedule> findAllSchedules() {
        return scheduleMap.values();
    }

    @Override
    public Schedule findScheduleById(Long id) {
        return scheduleMap.get(id);
    }

    public void deleteSchedule(Long id, ScheduleRequestDto requestDto) {
        scheduleMap.remove(id);
    }

    }

