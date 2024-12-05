package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.WarehouseStaff;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WarehouseStaffRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<WarehouseStaff> WAREHOUSE_STAFF_ROW_MAPPER = (rs, rowNum) -> WarehouseStaff.builder()
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

        return jdbcTemplate.queryForObject(
                sql, Long.class, warehouseStaff.getWarehouseId(), warehouseStaff.getEmployeeId());
    }

    public List<WarehouseStaff> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM warehouse_staffs
				ORDER BY employee_id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, WAREHOUSE_STAFF_ROW_MAPPER);
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

    //    public WarehouseStaff findByEmployeeId(Long employeeId) {
    //        String sql = """
    //				SELECT *
    //				FROM warehouse_staffs
    //				WHERE employee_id = ?;
    //				""";
    //
    //        try {
    //            return jdbcTemplate.queryForObject(sql, WAREHOUSE_STAFF_ROW_MAPPER, employeeId);
    //        } catch (EmptyResultDataAccessException e) {
    //            return null;
    //        }
    //    }

    public void update(WarehouseStaff warehouseStaff) {
        String sql = """
				UPDATE warehouse_staffs
				SET warehouse_id = ?
				WHERE employee_id = ?;
				""";

        jdbcTemplate.update(sql, warehouseStaff.getWarehouseId(), warehouseStaff.getEmployeeId());
    }

    public void deleteByEmployeeId(Long employeeId) {
        String sql = """
				DELETE FROM warehouse_staffs WHERE employee_id = ?;
				""";

        jdbcTemplate.update(sql, employeeId);
    }

    public boolean existsByEmployeeId(Long employeeId) {
        String sql = """
				SELECT COUNT(*) FROM warehouse_staffs WHERE employee_id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {employeeId}, Integer.class);

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
