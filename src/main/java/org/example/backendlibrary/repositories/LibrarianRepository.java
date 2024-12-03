package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Librarian;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LibrarianRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Librarian> LIBRARIAN_ROW_MAPPER = (rs, rowNum) -> Librarian.builder()
            .id(rs.getLong("id"))
            .libraryId(rs.getLong("library_id"))
            .employeeId(rs.getLong("employee_id"))
            .build();

    public Long save(Librarian librarian) {
        String sql =
                """
				INSERT INTO librarians (library_id, employee_id)
				VALUES (?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(sql, Long.class, librarian.getLibraryId(), librarian.getEmployeeId());
    }

    public List<Librarian> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM librarians
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, LIBRARIAN_ROW_MAPPER);
    }

    public Librarian findById(Long id) {
        String sql = """
				SELECT *
				FROM librarians
				WHERE id = ?;
				""";

        try {
            return jdbcTemplate.queryForObject(sql, LIBRARIAN_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Librarian findByEmployeeId(Long employeeId) {
        String sql = """
				SELECT *
				FROM librarians
				WHERE employee_id = ?;
				""";

        try {
            return jdbcTemplate.queryForObject(sql, LIBRARIAN_ROW_MAPPER, employeeId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(Librarian librarian) {
        String sql = """
				UPDATE librarians
				SET employee_id = ?, library_id = ?
				WHERE id = ?;
				""";

        return jdbcTemplate.update(sql, librarian.getEmployeeId(), librarian.getLibraryId());
    }

    public int deleteById(Long id) {
        String sql = """
				DELETE FROM librarians WHERE id = ?;
				""";

        return jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM librarians WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM librarians
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
