package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * {@code ScheduleController}는 일정 생성, 조회, 수정, 삭제에 대한 HTTP 요청을 처리하는 REST 컨트롤러입니다.
 * <p>
 * 임시 저장소로 {@code Map<Long, Schedule>}을 사용
 *
 * <pre>
 * 기본 URL: /schedules
 *
 * 예시 요청 (POST /schedules):
 * {
 *   "password": "1234",
 *   "writer": "홍길동",
 *   "task": "ㅇㅇ회의"
 * }
 *
 * 예시 응답:
 * {
 *   "id": 1,
 *   "writer": "홍길동",
 *   "task": "ㅇㅇ회의",
 *   "update": "2025-03-26"
 * }
 * </pre>
 */
@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final Map<Long, Schedule> scheduleMap = new HashMap<>();

    /**
     *  <b>새로운 일정 생성</b>
     *
     * @param requestDto 생성할 일정 정보가 담긴 요청 DTO
     * @return 생성된 일정의 응답 DTO와 201(CREATED) 상태 코드
     *
     * <pre>
     * POST /schedules
     *
     * {
     *   "password": "1234",
     *   "writer": "홍길동",
     *   "task": "회의 준비"
     * }
     * </pre>
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto) {
        Long scheduleId = scheduleMap.isEmpty() ? 1 : Collections.max(scheduleMap.keySet()) + 1;
        LocalDateTime update = LocalDateTime.now();

        Schedule schedule = new Schedule(scheduleId, requestDto.getPassword(), requestDto.getWriter(), requestDto.getTask(), update);
        scheduleMap.put(scheduleId, schedule);

        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.CREATED);
    }

    /**
     * <b>모든 일정 목록 조회</b>
     *
     * @return 전체 일정 정보를 담은 응답 DTO 리스트
     *
     * <pre>
     * GET /schedules
     * </pre>
     */
    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules() {
        List<ScheduleResponseDto> responseList = new ArrayList<>();
        for (Schedule schedule : scheduleMap.values()) {
            responseList.add(new ScheduleResponseDto(schedule));
        }
        return responseList;
    }

    /**
     * <b>특정 id일정 조회</b>
     *
     * @param id 조회할 일정의 ID
     * @return 일정이 존재하면 응답 DTO와 200 OK, 없으면 404 NOT FOUND
     *
     * <pre>
     * GET /schedules/{id}
     * </pre>
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {
        Schedule schedule = scheduleMap.get(id);

        if (schedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.OK);
    }

    /**
     * <b>특정 ID의 일정 수정</b>
     * <p>
     * 비밀번호가 일치해야 수정이 가능하며, 수정 시점은 update 필드에 반영됩니다.
     *
     * @param id 수정할 일정의 ID
     * @param requestDto 수정할 정보가 담긴 요청 DTO (비밀번호 포함)
     * @return 수정 성공 시 응답 DTO와 200 OK, 비밀번호 불일치 시 401 UNAUTHORIZED
     *
     * <pre>
     * PUT /schedules/{id}
     *
     * {
     *   "password": "1234",
     *   "writer": "홍길동",
     *   "task": "보고서 작성"
     * }
     * </pre>
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id,
                                                              @Valid @RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleMap.get(id);

        if (schedule.getPassword().equals(requestDto.getPassword())) {
            schedule.update(requestDto);
            return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * <b>특정 ID의 일정 삭제</b>
     * <p>
     * 비밀번호가 일치해야 삭제가 가능합니다.
     *
     * @param id 삭제할 일정의 ID
     * @param requestDto 비밀번호가 담긴 요청 DTO
     * @return 삭제 성공 시 200 OK, 실패 시 404 NOT FOUND
     *
     * <pre>
     * DELETE /schedules/{id}
     *
     * {
     *   "password": "1234"
     * }
     * </pre>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleMap.get(id);
        if (scheduleMap.containsKey(id) && schedule.getPassword().equals(requestDto.getPassword())) {
            scheduleMap.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
