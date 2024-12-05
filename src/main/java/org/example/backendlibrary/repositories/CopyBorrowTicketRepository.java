package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.CopyBorrowTicket;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CopyBorrowTicketRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<CopyBorrowTicket> COPY_BORROW_TICKET_ROW_MAPPER =
            (rs, rowNum) -> CopyBorrowTicket.builder()
                    .startDate(rs.getTimestamp("start_date"))
                    .endDate(rs.getTimestamp("end_date"))
                    .returnDate(rs.getTimestamp("return_date"))
                    .fee(rs.getInt("fee"))
                    .fine(rs.getInt("fine"))
                    .statusOnReturn(rs.getString("status_on_return"))
                    .copyId(rs.getLong("copy_id"))
                    .borrowTicketId(rs.getLong("borrow_ticket_id"))
                    .build();

    public void save(CopyBorrowTicket copyBorrowTicket) {
        String sql =
                """
				INSERT INTO copy_borrow_tickets (start_date, end_date, return_date, fee, fine, status_on_return, copy_id, borrow_ticket_id)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?);
				""";

        jdbcTemplate.update(
                sql,
                copyBorrowTicket.getStartDate(),
                copyBorrowTicket.getEndDate(),
                copyBorrowTicket.getReturnDate(),
                copyBorrowTicket.getFee(),
                copyBorrowTicket.getFine(),
                copyBorrowTicket.getStatusOnReturn(),
                copyBorrowTicket.getCopyId(),
                copyBorrowTicket.getBorrowTicketId());
    }

    public List<CopyBorrowTicket> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM copy_borrow_tickets
				ORDER BY copy_id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, COPY_BORROW_TICKET_ROW_MAPPER);
    }

    public CopyBorrowTicket findByCopyIdAndBorrowTicketId(Long copyId, Long borrowTicketId) {
        String sql =
                """
				SELECT *
				FROM copy_borrow_tickets
				WHERE copy_id = ? AND borrow_ticket_id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, COPY_BORROW_TICKET_ROW_MAPPER, copyId, borrowTicketId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<CopyBorrowTicket> findAllByBorrowTicketId(Long borrowTicketId) {
        String sql = """
				SELECT *
				FROM copy_borrow_tickets
				WHERE borrow_ticket_id = ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {borrowTicketId}, COPY_BORROW_TICKET_ROW_MAPPER);
    }

    public void update(CopyBorrowTicket copyBorrowTicket) {
        String sql =
                """
				UPDATE copy_borrow_tickets
				SET start_date = ?, end_date = ?, return_date = ?, fee = ?, fine = ?, status_on_return = ?
				WHERE copy_id = ? AND borrow_ticket_id = ?;
				""";

        jdbcTemplate.update(
                sql,
                copyBorrowTicket.getStartDate(),
                copyBorrowTicket.getEndDate(),
                copyBorrowTicket.getReturnDate(),
                copyBorrowTicket.getFee(),
                copyBorrowTicket.getFine(),
                copyBorrowTicket.getStatusOnReturn(),
                copyBorrowTicket.getCopyId(),
                copyBorrowTicket.getBorrowTicketId());
    }

    public void deleteByCopyIdAndBorrowTicketId(Long copyId, Long borrowTicketId) {
        String sql = """
				DELETE FROM copy_borrow_tickets
				WHERE copy_id = ? AND borrow_ticket_id = ?;
				""";

        jdbcTemplate.update(sql, copyId, borrowTicketId);
    }

    public boolean existsByCopyIdAndBorrowTicketId(Long copyId, Long borrowTicketId) {
        String sql =
                """
				SELECT COUNT(*)
				FROM copy_borrow_tickets
				WHERE copy_id = ? AND borrow_ticket_id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {copyId, borrowTicketId}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*)
				FROM copy_borrow_tickets
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
