package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code ScheduleServiceImpl} 클래스는 일정 관리 서비스의 구현체로,
 * 일정 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 담당합니다.
 * <p>
 * {@link ScheduleService} 인터페이스를 구현합니다.
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    /**
     * 스케줄 정보를 다루는 Repository
     */
    private final ScheduleRepository scheduleRepository;

    /**
     * 생성자 - {@link ScheduleRepository}를 주입받아 초기화.
     *
     * @param scheduleRepository 일정 저장소 인터페이스 구현체
     */
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 새로운 일정을 저장.
     *
     * @param requestDto 저장할 일정 정보를 담은 요청 DTO
     * @return 저장된 일정 정보를 담은 응답 DTO
     */
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        LocalDateTime update = LocalDateTime.now();
        Schedule schedule = new Schedule(
                requestDto.getPassword(),
                requestDto.getWriter(),
                requestDto.getTask(),
                update
        );
        return scheduleRepository.saveSchedule(schedule);
    }

    /**
     * 모든 일정을 조회하여 리스트로 반환.
     *
     * @return 모든 일정의 응답 DTO 리스트
     */
    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        List<ScheduleResponseDto> scheduleList = new ArrayList<>();

        for (Schedule schedule : scheduleRepository.findAllSchedules()) {
            scheduleList.add(new ScheduleResponseDto(schedule));
        }

        return scheduleList;
    }

    /**
     * ID를 기준으로 일정을 조회.
     *
     * @param id 조회할 일정의 ID
     * @return 해당 일정의 응답 DTO
     * @throws ResponseStatusException 일정이 존재하지 않을 경우 404 에러 발생
     */
    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        return new ScheduleResponseDto(schedule);
    }

    /**
     * ID에 해당하는 일정을 수정.
     *
     * @param id         수정할 일정의 ID
     * @param requestDto 수정할 정보를 담은 요청 DTO
     * @return 수정된 일정의 응답 DTO
     * @throws ResponseStatusException 비밀번호가 일치하지 않을 경우 401 에러 발생
     */
    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        if (!schedule.getPassword().equals(requestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        schedule.update(requestDto);
        return new ScheduleResponseDto(schedule);
    }

    /**
     * ID에 해당하는 일정을 삭제.
     *
     * @param id         삭제할 일정의 ID
     * @param requestDto 비밀번호 확인을 위한 요청 DTO
     * @throws ResponseStatusException 비밀번호가 일치하지 않을 경우 401 에러 발생
     */
    @Override
    public void deleteSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        if (!schedule.getPassword().equals(requestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        scheduleRepository.deleteSchedule(id, requestDto);
    }
}
