package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schedules")
public class ScheduleController {


    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    //Valid를 통해 클라이언트의 요청 RequestDto의 필드에 붙은 유효성 검사를 수행. null값 요청 x
    //ResponseEntity는 Http응답 전체를 커스터마이징.(상태코드(201), 응답 본문 결과(포함됨), 헤더도 가능(포함하고 있지 않음)) 그냥 반환하면 상태코드 변경못함.
    //요청을 받고 requestDto를 인자로 받음. JSON데이터를 담아 보내기 위함.
    //상태 코드를 바꿔야 할 때 → 201 Created, 204 No Content, 404 Not Found, 400 Bad Request 등
    //헤더를 조작할 때 → Location, Authorization, Set-Cookie 등
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto) {
        return new ResponseEntity<>(scheduleService.saveSchedule(requestDto), HttpStatus.CREATED);
    }

    //여러 개의 스케줄 정보를 반환하기 위해 List로.(List는 순서가 중요), responseDto도 정보를 담는 객체
    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime updateAt, @RequestParam(required= false) String writer) {
        return scheduleService.findAllSchedules(updateAt, writer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id,
                                                              @Valid @RequestBody ScheduleRequestDto requestDto) {
        return new ResponseEntity<>(scheduleService.updateTask(id,requestDto.getTask(), requestDto.getWriter()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        scheduleService.deleteSchedule(id, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}



//https://ottl-seo.tistory.com/44