package org.example.backendlibrary.repositories;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DocumentGenresRepository {
    private final JdbcTemplate jdbcTemplate;

    public int addGenreToDocument(Long documentId, Long genreId) {
        String sql = """
				INSERT INTO document_genres (document_id, genre_Id)
				VALUES (?, ?);
				""";

        return jdbcTemplate.update(sql, documentId, genreId);
    }

    public List<String> getGenresByDocumentId(Long documentId) {
        String sql =
                """
				SELECT g.name
				FROM genres g
				JOIN document_genres dg ON g.id = dg.genre_id
				WHERE dg.document_id = ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {documentId}, (rs, rowNum) -> rs.getString("name"));
    }

    public void deleteGenresByDocumentId(int documentId) {
        String deleteSql = "DELETE FROM document_genres WHERE document_id = ?";
        jdbcTemplate.update(deleteSql, documentId);
    }
}
