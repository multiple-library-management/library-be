package org.example.backendlibrary.repositories;

import lombok.RequiredArgsConstructor;
import org.example.backendlibrary.entities.WarehouseStaff;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WarehouseStaffRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<WarehouseStaff> WAREHOUSE_STAFF_ROW_MAPPER = (rs, rowNum) -> WarehouseStaff.builder()
            .id(rs.getLong("id"))
            .warehouseId(rs.getLong("warehouse_id"))
            .employeeId(rs.getLong("employee_id"))
            .build();

    public Long save(WarehouseStaff warehouseStaff) {
        String sql =
                """
				INSERT INTO warehouse_staffs (warehouse_id, employee_id)
				VALUES (?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(sql, Long.class, warehouseStaff.getWarehouseId(), warehouseStaff.getEmployeeId());
    }

    public List<WarehouseStaff> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM warehouse_staffs
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, WAREHOUSE_STAFF_ROW_MAPPER);
    }

    public WarehouseStaff findById(Long id) {
        String sql = """
				SELECT *
				FROM warehouse_staffs
				WHERE id = ?;
				""";

        try {
            return jdbcTemplate.queryForObject(sql, WAREHOUSE_STAFF_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public WarehouseStaff findByEmployeeId(Long employeeId) {
        String sql = """
				SELECT *
				FROM warehouse_staffs
				WHERE employee_id = ?;
				""";

        try {
            return jdbcTemplate.queryForObject(sql, WAREHOUSE_STAFF_ROW_MAPPER, employeeId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(WarehouseStaff warehouseStaff) {
        String sql = """
				UPDATE warehouse_staffs
				SET employee_id = ?, warehouse_id = ?
				WHERE id = ?;
				""";

        return jdbcTemplate.update(sql, warehouseStaff.getEmployeeId(), warehouseStaff.getWarehouseId());
    }

    public int deleteById(Long id) {
        String sql = """
				DELETE FROM warehouse_staffs WHERE id = ?;
				""";

        return jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM warehouse_staffs WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM warehouse_staffs;
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
