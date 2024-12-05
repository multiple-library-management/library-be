package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Transfer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransferRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Transfer> TRANSFER_ROW_MAPPER = (rs, rowNum) -> Transfer.builder()
            .id(rs.getLong("id"))
            .createdDate(rs.getTimestamp("created_date"))
            .startDate(rs.getTimestamp("start_date"))
            .endDate(rs.getTimestamp("end_date"))
            .amount(rs.getInt("amount"))
            .type(rs.getString("type"))
            .libraryId(rs.getLong("library_id"))
            .warehouseId(rs.getLong("warehouse_id"))
            .warehouseStaffId(rs.getLong("warehouse_staff_id"))
            .build();

    public Long save(Transfer transfer) {
        String sql =
                """
				INSERT INTO transfers (created_date, start_date, end_date, amount, type, library_id, warehouse_id, warehouse_staff_id)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                transfer.getCreatedDate(),
                transfer.getStartDate(),
                transfer.getEndDate(),
                transfer.getAmount(),
                transfer.getType(),
                transfer.getLibraryId(),
                transfer.getWarehouseId(),
                transfer.getWarehouseStaffId());
    }

    public List<Transfer> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM transfers
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, TRANSFER_ROW_MAPPER);
    }

    public Transfer findById(Long id) {
        String sql = """
				SELECT *
				FROM transfers
				WHERE id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, TRANSFER_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(Transfer transfer) {
        String sql =
                """
				UPDATE transfers
				SET created_date = ?, start_date = ?, end_date = ?, amount = ?, type = ?, library_id = ?, warehouse_id = ?, warehouse_staff_id = ?
				WHERE id = ?;
				""";

        jdbcTemplate.update(
                sql,
                transfer.getCreatedDate(),
                transfer.getStartDate(),
                transfer.getEndDate(),
                transfer.getAmount(),
                transfer.getType(),
                transfer.getLibraryId(),
                transfer.getWarehouseId(),
                transfer.getWarehouseStaffId(),
                transfer.getId());
    }

    public void deleteById(Long id) {
        String sql = """
				DELETE FROM transfers WHERE id = ?;
				""";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM transfers WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM transfers
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
