package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Workshift;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WorkshiftRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Workshift> WORKSHIFT_ROW_MAPPER = (rs, rowNum) -> Workshift.builder()
            .id(rs.getLong("id"))
            .date(rs.getDate("date").toLocalDate())
            .startTime(rs.getTime("start_time").toLocalTime())
            .endTime(rs.getTime("end_time").toLocalTime())
            .employeeId(rs.getLong("employee_id"))
            .build();

    public Long save(Workshift workshift) {
        String sql =
                """
				INSERT INTO workshifts (date, start_time, end_time, employee_id)
				VALUES (?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                workshift.getDate(),
                workshift.getStartTime(),
                workshift.getEndTime(),
                workshift.getEmployeeId());
    }

    public List<Workshift> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
					SELECT *
					FROM workshifts
					ORDER BY id
					LIMIT ? OFFSET ?;
					""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, WORKSHIFT_ROW_MAPPER);
    }

    public Workshift findById(Long id) {
        String sql = """
				SELECT *
				FROM workshifts
				WHERE id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, WORKSHIFT_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(Workshift workshift) {
        String sql =
                """
				UPDATE workshifts
				SET date = ?, start_time = ?, end_time = ?, employee_id = ?
				WHERE id = ?;
				""";

        jdbcTemplate.update(
                sql,
                workshift.getDate(),
                workshift.getStartTime(),
                workshift.getEndTime(),
                workshift.getEmployeeId(),
                workshift.getId());
    }

    public void deleteById(Long id) {
        String sql = """
				DELETE FROM workshifts WHERE id = ?;
				""";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM workshifts WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM workshifts
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
