package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Copy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CopyRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Copy> COPY_ROW_MAPPER = (rs, rowNum) -> Copy.builder()
            .id(rs.getLong("id"))
            .fee(rs.getInt("fee"))
            .status(rs.getString("status"))
            .documentId(rs.getLong("document_id"))
            .libraryId(rs.getLong("library_id"))
            .warehouseId(rs.getLong("warehouse_id"))
            .orderId(rs.getLong("order_id"))
            .build();

    public Long save(Copy copy) {
        String sql =
                """
				INSERT INTO copies (fee, status, document_id, library_id, warehouse_id, order_id)
				VALUES (?, ?, ?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                copy.getFee(),
                copy.getStatus(),
                copy.getDocumentId(),
                copy.getLibraryId(),
                copy.getWarehouseId(),
                copy.getOrderId());
    }

    public List<Copy> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM copies
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, COPY_ROW_MAPPER);
    }

    public Copy findById(Long id) {
        String sql = """
				SELECT *
				FROM copies
				WHERE id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, COPY_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(Copy copy) {
        String sql =
                """
				UPDATE copies
				SET fee = ?, status = ?, document_id = ?, library_id = ?, warehouse_id = ?, order_id = ?
				WHERE id = ?;
				""";

        jdbcTemplate.update(
                sql,
                copy.getFee(),
                copy.getStatus(),
                copy.getDocumentId(),
                copy.getLibraryId(),
                copy.getWarehouseId(),
                copy.getOrderId(),
                copy.getId());
    }

    public void deleteById(Long id) {
        String sql = """
				DELETE FROM copies WHERE id = ?;
				""";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM copies WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM copies
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
