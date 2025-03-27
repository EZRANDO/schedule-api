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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

/**
 * {@code JdbcTemplateScheduleRepository} 클래스는 JdbcTemplate을 이용해
 * Schedule 정보를 데이터베이스에 저장, 조회하는 기능을 제공합니다.
 * <p>
 * {@code ScheduleRepository} 인터페이스의 구현체입니다.
 */
@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    /**
     * Spring JDBC에서 제공하는 JDBC 템플릿
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * 생성자. {@link DataSource}를 주입받아 JdbcTemplate을 초기화합니다.
     *
     * @param dataSource DB 연결을 위한 데이터 소스
     */
    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 전달받은 {@link Schedule} 객체를 DB에 저장하고,
     * 저장된 스케줄 정보를 {@link ScheduleResponseDto}로 반환
     *
     * @param schedule 저장할 스케줄 객체
     * @return 저장된 스케줄의 응답 DTO
     */
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", schedule.getTask()); // title 컬럼이 존재한다면 해당 데이터도 저장
        parameters.put("password", schedule.getPassword());
        parameters.put("writer", schedule.getWriter());
        parameters.put("task", schedule.getTask());
        parameters.put("updated_at", schedule.getUpdated_at());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getTask(), schedule.getWriter(), schedule.getUpdated_at());
    }

    /**
     * 전체 스케줄 목록을 조회하여 리스트로 반환
     *
     * @return 전체 {@link Schedule} 리스트
     */
    @Override
    public List<Schedule> findAllSchedules() {
        return jdbcTemplate.query("SELECT * FROM schedule", scheduleRowMapper());
    }

    /**
     * {@link java.sql.ResultSet}의 결과를 {@link Schedule} 객체로 매핑하는 RowMapper 정의
     *
     * @return RowMapper 객체
     */
    private RowMapper<Schedule> scheduleRowMapper() {
        return (rs, rowNum) -> {
            Timestamp ts = rs.getTimestamp("updated_at");
            LocalDateTime updatedAt = (ts != null) ? ts.toLocalDateTime() : null;

            return new Schedule(
                    rs.getString("password"),
                    rs.getString("writer"),
                    rs.getString("task"),
                    updatedAt
            );
        };
    }

    /**
     * ID로 스케줄을 조회하는 메서드 (미구현)
     *
     * @param id 조회할 스케줄 ID
     * @return {@link Schedule} 객체
     */
    public Schedule findScheduleById(Long id) {
        return null;
    }

    /**
     * 전달받은 ID와 요청 정보를 기반으로 스케줄을 삭제하는 메서드 (미구현)
     *
     * @param id         삭제할 스케줄 ID
     * @param requestDto 요청 DTO (비밀번호 검증 등)
     */
    public void deleteSchedule(Long id, ScheduleRequestDto requestDto) {
        // 구현 예정
    }
}
