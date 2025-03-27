package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code ScheduleController}는 일정(Schedule) 관련 요청을 처리하는 REST 컨트롤러입니다.
 * <p>
 * 일정을 생성, 조회, 수정, 삭제하는 엔드포인트를 제공.
 */
@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    /**
     * 일정 비즈니스 로직을 담당.
     */
    private final ScheduleService scheduleService;

    // (사용되지 않음) 임시 저장용 Map
    private final Map<Long, Schedule> scheduleMap = new HashMap<>();

    /**
     * 생성자 - {@link ScheduleService}를 주입받습니다.
     *
     * @param scheduleService 일정 서비스 구현체
     */
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 새로운 일정을 생성.
     *
     * @param requestDto 일정 생성 요청 데이터
     * @return 생성된 일정 정보와 HTTP 상태 201(CREATED)
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto) {
        return new ResponseEntity<>(scheduleService.saveSchedule(requestDto), HttpStatus.CREATED);
    }

    /**
     * 모든 일정을 조회.
     *
     * @return 일정 응답 DTO 리스트
     */
    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleService.findAllSchedules();
    }

    /**
     * 특정 ID의 일정을 조회.
     *
     * @param id 조회할 일정의 ID
     * @return 해당 일정 정보와 HTTP 상태 200(OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    /**
     * 특정 ID의 일정을 수정.
     *
     * @param id         수정할 일정의 ID
     * @param requestDto 수정 요청 데이터
     * @return 수정된 일정 정보와 HTTP 상태 200(OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id,
                                                              @Valid @RequestBody ScheduleRequestDto requestDto) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, requestDto), HttpStatus.OK);
    }

    /**
     * 특정 ID의 일정을 삭제.
     *
     * @param id         삭제할 일정의 ID
     * @param requestDto 비밀번호 확인용 요청 데이터
     * @return HTTP 상태 200(OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        scheduleService.deleteSchedule(id, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
