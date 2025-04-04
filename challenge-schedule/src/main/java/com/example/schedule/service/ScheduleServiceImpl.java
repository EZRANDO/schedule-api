package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code ScheduleServiceImpl} 클래스는 일정 관리 서비스의 구현체로,
 * 일정 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 담당합니다.
 * <p>
 * {@link ScheduleService} 인터페이스를 구현합니다.
 */
@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    //db
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    //응답을 위해 Schedule 객체로 저장
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        LocalDateTime update = LocalDateTime.now();
        Schedule schedule = new Schedule(
                requestDto.getPassword(),
                requestDto.getWriter(),
                requestDto.getTask(),
                update //요청을 객체에 변환하기 위해 .
        ); //리포지토리 내부에서 이미 DTO로 바꿔서 반환
        return scheduleRepository.saveSchedule(schedule);
    } //결국 json형태로 보낸 데이터를 requestDto객체로 받아 실제 DB에 저장할수 있는 Schedule엔티티객체로 변환하는 거임.

    @Override
    public List<ScheduleResponseDto> findAllSchedules(LocalDateTime updatedAt, String writer) {
        List<ScheduleResponseDto> responseList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //출처 어딘지. 응답dto for문에 리포지토리에서 db에서 조건에 맞는 데이터 조회, listponseDto형태로 반환, 조건에 updateAt, writer
        for (ScheduleResponseDto schedule : scheduleRepository.findAllSchedules(updatedAt, writer)) {
            boolean match = false;

            //updatedAt만 있을 경우,
            if (updatedAt != null && writer == null) {
                match = schedule.getUpdatedAt().equals(updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            if (updatedAt == null && writer != null) {
                match = schedule.getWriter().equals(writer);
            }

            if (updatedAt != null && writer != null) {
                match = schedule.getUpdatedAt().equals(updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) &&
                        schedule.getWriter().equals(writer);
            }
            if (match) {
                responseList.add(schedule);
            }
        }
        log.info("log.info={}", scheduleRepository.findAllSchedules(updatedAt, writer));
        return scheduleRepository.findAllSchedules(updatedAt, writer);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        ScheduleResponseDto schedule = scheduleRepository.findScheduleById(id);

        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        return new ScheduleResponseDto(schedule);
    }


    @Override
    public ScheduleResponseDto updateTask(Long id, String writer, String task) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."));
        if (writer == null && task == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 항목이 없습니다.");
        }

        if (writer != null) schedule.setWriter(writer);
        if (task != null) schedule.setTask(task);
        schedule.setUpdatedAt(LocalDateTime.now());

        // 실제 DB 업데이트
        scheduleRepository.updateTask(schedule);
        Schedule updated = scheduleRepository.updateTask(schedule);
        return new ScheduleResponseDto(updated);
    }
    //service에서 dto를 수정하면 안되고, 실제 entity를 수정해야하기 때문에, 반환을 schedule로 받는다.
    //DB수정 후에서는 다시 엔티티를 조회하고 DTO로 감싸서 리턴해야한다.


    public void deleteSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."));

        if (!schedule.getPassword().equals(requestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        int deletedRow = scheduleRepository.deleteSchedule(id, requestDto);
        if(deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        scheduleRepository.deleteSchedule(id, requestDto);
    }
}


//    //id조회
//        //db에 있는 내용 갖고옴.
//        //삭제후 dto로 다시 리턴
