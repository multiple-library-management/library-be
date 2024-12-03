package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Library;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LibraryRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Library> LIBRARY_ROW_MAPPER = (rs, rowNum) -> Library.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .address(rs.getString("address"))
            .district(rs.getString("district"))
            .ward(rs.getString("ward"))
            .street(rs.getString("street"))
            .city(rs.getString("city"))
            .build();

    public Long save(Library library) {
        String sql =
                """
				INSERT INTO libraries (name, address, district, ward, street, city)
				VALUES (?, ?, ?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                library.getName(),
                library.getAddress(),
                library.getDistrict(),
                library.getWard(),
                library.getStreet(),
                library.getCity());
    }

    public List<Library> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM libraries
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, LIBRARY_ROW_MAPPER);
    }

    public Library findById(Long id) {
        String sql = """
				SELECT *
				FROM libraries
				WHERE id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, LIBRARY_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(Library library) {
        String sql =
                """
				UPDATE libraries
				SET name = ?, address = ?, district = ?, ward = ?, street = ?, city = ?
				WHERE id = ?;
				""";

        return jdbcTemplate.update(
                sql,
                library.getName(),
                library.getAddress(),
                library.getDistrict(),
                library.getWard(),
                library.getStreet(),
                library.getCity(),
                library.getId());
    }

    public int deleteById(Long id) {
        String sql = """
				DELETE FROM libraries WHERE id = ?;
				""";

        return jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM libraries WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM libraries
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
