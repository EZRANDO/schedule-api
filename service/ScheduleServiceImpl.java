package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {this.scheduleRepository = scheduleRepository;}

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        LocalDateTime update = LocalDateTime.now();

        Schedule schedule = new Schedule(requestDto.getPassword(), requestDto.getWriter(), requestDto.getTask(), update);

        Schedule savedSchedule = scheduleRepository.saveSchedule(schedule);

        return new ScheduleResponseDto(savedSchedule);

    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String update, String writer) {
        List<ScheduleResponseDto> responseList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Schedule schedule : scheduleRepository.findAllSchedules()) {
            boolean match = true;

            if (update != null && !update.isEmpty()) {
                String formatted = schedule.getUpdate().format(formatter);
                match = match && formatted.equals(update);
            }

            if (writer != null && !writer.isEmpty()) {
                match = match && schedule.getWriter().equals(writer);
            }

            if (match) {
                responseList.add(new ScheduleResponseDto(schedule));
            }
        }
        if  (responseList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 조건에 맞는 일정이 없습니다.");


        }

        return responseList;
    }
    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {

        Schedule schedule = scheduleRepository.findScheduleById(id);

        if (!schedule.getPassword().equals(requestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        schedule.update(requestDto);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        if (!schedule.getPassword().equals(requestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        scheduleRepository.deleteSchedule(id, requestDto);
    }
}

