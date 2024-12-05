package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Document;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DocumentRepository {
    private final JdbcTemplate jdbcTemplate;

    public long save(Document document) {
        String sql =
                """
				INSERT INTO documents (title, language, image, price, publisher_name, document_type, volume, frequency, edition)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                document.getTitle(),
                document.getLanguage(),
                document.getImage(),
                document.getPrice(),
                document.getPublisherName(),
                document.getDocumentType(),
                document.getVolume(),
                document.getFrequency(),
                document.getEdition());
    }

    public Document findById(Long id) {
        String sql = """
				SELECT *
				FROM documents
				WHERE id = ?;
				""";

        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Document.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Get all documents
    public List<Document> findAll(int page, int size) {
        // Calculate the offset based on the page number and size
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM documents
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, new BeanPropertyRowMapper<>(Document.class));
    }

    // Update document
    public int update(Document document) {
        String sql =
                """
				UPDATE documents
				SET title = ?, language = ?, image = ?, price = ?, publisher_name = ?, document_type = ?, volume = ?,
					frequency = ?, edition = ?
				WHERE id = ?;
				""";

        return jdbcTemplate.update(
                sql,
                document.getTitle(),
                document.getLanguage(),
                document.getImage(),
                document.getPrice(),
                document.getPublisherName(),
                document.getDocumentType(),
                document.getVolume(),
                document.getFrequency(),
                document.getEdition(),
                document.getId());
    }

    // Delete document by ID
    public int deleteById(Long id) {
        String sql = """
				DELETE FROM documents WHERE id = ?;
				""";

        return jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM documents WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM documents
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
