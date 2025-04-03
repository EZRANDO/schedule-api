package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;


@Repository
public class JdbcTemplateScheduleRepository implements com.example.schedule.repository.ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<Schedule> findById(Long id) {
        String sql = "SELECT * FROM schedule WHERE id = ?";
        List<Schedule> list = jdbcTemplate.query(sql, (rs, rowNum) -> new Schedule(
                rs.getLong("id"),
                rs.getString("task"),
                rs.getString("password"),
                rs.getString("writer"),
                rs.getTimestamp("updatedAt").toLocalDateTime()
        ), id);
        return list.stream().findAny();
    }
    //Schedule객체 일정 데이터를 입력받기 때문에 Schedule을 매개변수로 받음.
    //SimpleJdbcInsert는 Spring에서 제공하는 DB insert임.
    //usingGeneratedKeyColumns("id")자동으로 생성되는 id값을 insert 끝나고 Java 코드에서 다시 받아오겠다.
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", schedule.getTask());
        parameters.put("password", schedule.getPassword());
        parameters.put("writer", schedule.getWriter());
        parameters.put("updatedAt", schedule.getUpdatedAt());
        //parameters를 가지고 DB에 insert
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        //응답DTO생성 반환
        return new ScheduleResponseDto(key.longValue(), schedule.getTask(), schedule.getWriter(), schedule.getUpdatedAt());
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(LocalDateTime updatedAt, String writer) {
        //writer schedule이 조회가 가능하다.
        List<Object> params = new ArrayList<>();
        //두 가지 조건을 처리하려면, list<Object>처리
        StringBuilder sql = new StringBuilder("SELECT * FROM schedule WHERE 1=1");

        //조건처리, sql .append, prams.add'
        if (writer != null) {
            sql.append(" AND writer = ?");
            params.add(writer);
        }
        if (updatedAt != null) {
            sql.append(" And updated_at >= ?");
            params.add(updatedAt);
        }
        //1. 실행할 sql, 2. 결과값을 객체로 매핑할 방법 db행을 java객체로 바꿈. dto로., 3.SQL의 ?에 바인딩할 파라미터 list<object>로 된 파라미터를 배열로 변환.
        return jdbcTemplate.query(sql.toString(), scheduleRowMapper(), params.toArray());
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {
        return new RowMapper<>() {
            //reponse값
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {

                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("writer"),
                        rs.getString("task"),
                        rs.getTimestamp("updatedAt").toLocalDateTime()
                );
            }

        };
    }

    //http://localhost:8080/schedules?updated_at=2025-03-30T23:52:43
    //http://localhost:8080/schedules?updatedAt=2025-03-30T23:52:43&writer=23342
    public ScheduleResponseDto findScheduleById(Long id) {
        List<ScheduleResponseDto> result = jdbcTemplate.query(
                "SELECT id, task, password, writer, updatedAt FROM schedule WHERE id = ?",
                scheduleRowMapper2(), id);
        return result.stream().findAny().orElse(null);
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper2() {
        return new RowMapper<>() {
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("writer"),
                        rs.getString("task"),
                        rs.getTimestamp("updatedAt").toLocalDateTime()
                );
            }
        };
    }

    public Schedule updateTask(Schedule schedule) {
        String sql = "UPDATE schedule SET task = ?, writer = ?, updatedAt = ? WHERE id = ?";
        jdbcTemplate.update(sql, schedule.getTask(), schedule.getWriter(), schedule.getUpdatedAt(), schedule.getId());

        // 수정된 결과 다시 조회해서 리턴
        return findById(schedule.getId()).orElseThrow(() -> new IllegalStateException("수정 후 데이터 조회 실패"));
    }

    public int deleteSchedule(Long id, ScheduleRequestDto requestDto) {
        return jdbcTemplate.update("delete from schedule where id = ?", id);
    }
}


