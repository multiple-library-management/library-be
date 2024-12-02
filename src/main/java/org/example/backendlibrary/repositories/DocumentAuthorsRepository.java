package org.example.backendlibrary.repositories;


import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DocumentAuthorsRepository {
    private final JdbcTemplate jdbcTemplate;

    public int addAuthorToDocument(Long documentId, String authorName){
        String sql = """
                INSERT INTO document_authors (document_id, author_name)
                VALUES (?, ?);
                """;

//        log.info("authorname: {}", authorName);
        return jdbcTemplate.update(sql, documentId, authorName);
    }

    public List<String> getAuthorsByDocumentId(Long documentId){
        String sql = """
                SELECT author_name
                FROM document_authors
                WHERE document_id = ?;
                """;

        return jdbcTemplate.query(sql,
                new Object[]{documentId},
                (rs, rowNum) -> rs.getString("author_name")
        );
    }

    public void deleteAuthorByDocumentId(int documentId) {
        String deleteSql = "DELETE FROM document_authors WHERE document_id = ?";
        jdbcTemplate.update(deleteSql, documentId);
    }
}
