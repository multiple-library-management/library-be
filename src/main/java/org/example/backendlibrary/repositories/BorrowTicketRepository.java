package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.BorrowTicket;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BorrowTicketRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<BorrowTicket> BORROW_TICKET_ROW_MAPPER = (rs, rowNum) -> BorrowTicket.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("member_id"))
            .librarianId(rs.getLong("librarian_id"))
            .build();

    public Long save(BorrowTicket borrowTicket) {
        System.out.println(borrowTicket);
        String sql =
                """
				INSERT INTO borrow_tickets (member_id, librarian_id)
				VALUES (?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(sql, Long.class, borrowTicket.getMemberId(), borrowTicket.getLibrarianId());
    }

    public List<BorrowTicket> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM borrow_tickets
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, BORROW_TICKET_ROW_MAPPER);
    }

    public BorrowTicket findById(Long id) {
        String sql = """
				SELECT *
				FROM borrow_tickets
				WHERE id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, BORROW_TICKET_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(BorrowTicket borrowTicket) {
        String sql = """
				UPDATE borrow_tickets
				SET member_id = ?, librarian_id = ?
				WHERE id = ?;
				""";

        return jdbcTemplate.update(
                sql, borrowTicket.getMemberId(), borrowTicket.getLibrarianId(), borrowTicket.getId());
    }

    public int deleteById(Long id) {
        String sql = """
				DELETE FROM borrow_tickets WHERE id = ?;
				""";

        return jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM borrow_tickets WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM borrow_tickets
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
