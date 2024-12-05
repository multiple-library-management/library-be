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
            .libraryId(rs.getLong("library_id"))
            .employeeId(rs.getLong("employee_id"))
            .build();

    public Long save(Librarian librarian) {
        String sql =
                """
				INSERT INTO librarians (library_id, employee_id)
				VALUES (?, ?)
				RETURNING employee_id;
				""";

        return jdbcTemplate.queryForObject(sql, Long.class, librarian.getLibraryId(), librarian.getEmployeeId());
    }

    public List<Librarian> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM librarians
				ORDER BY employee_id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, LIBRARIAN_ROW_MAPPER);
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

    //    public Librarian findByEmployeeId(Long employeeId) {
    //        String sql = """
    //				SELECT *
    //				FROM librarians
    //				WHERE employee_id = ?;
    //				""";
    //
    //        try {
    //            return jdbcTemplate.queryForObject(sql, LIBRARIAN_ROW_MAPPER, employeeId);
    //        } catch (EmptyResultDataAccessException e) {
    //            return null;
    //        }
    //    }

    public void update(Librarian librarian) {
        String sql = """
				UPDATE librarians
				SET library_id = ?
				WHERE employee_id = ?;
				""";

        jdbcTemplate.update(sql, librarian.getLibraryId(), librarian.getEmployeeId());
    }

    public void deleteByEmployeeId(Long employeeId) {
        String sql = """
				DELETE FROM librarians WHERE employee_id = ?;
				""";

        jdbcTemplate.update(sql, employeeId);
    }

    public boolean existsByEmployeeId(Long employeeId) {
        String sql = """
				SELECT COUNT(*) FROM librarians WHERE employee_id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {employeeId}, Integer.class);

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
