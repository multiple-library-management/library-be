package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Genre;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    public Long save(Genre genre) {
        String sql = """
				INSERT INTO genres (name)
				VALUES (?)
				RETURNING id;
				""";

        //        System.out.println("Genre name " + genre.getName());

        return jdbcTemplate.queryForObject(sql, new Object[] {genre.getName()}, Long.class);
    }

    public Genre findById(Long id) {
        String sql = """
				SELECT *
				FROM genres
				WHERE id = ?;
				""";

        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Genre.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Genre> findAll(int page, int size) {
        // Calculate the offset based on the page number and size
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM genres
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, new BeanPropertyRowMapper<>(Genre.class));
    }

    public Genre findByName(String name) {
        String sql = """
				SELECT *
				FROM genres
				WHERE LOWER(name) = LOWER(?);
				""";

        //        System.out.println("name: " + name);

        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Genre.class), name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(Genre genre) {
        String sql = """
				UPDATE genres
				SET name = ?
				WHERE id = ?;
				""";

        jdbcTemplate.update(sql, genre.getName(), genre.getId());
    }

    public void deleteById(Long id) {
        String sql = """
				DELETE FROM genres WHERE id = ?;
				""";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM genres WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM genres
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
