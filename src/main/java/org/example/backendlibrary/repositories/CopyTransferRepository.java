package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.CopyTransfer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CopyTransferRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<CopyTransfer> COPY_TRANSFER_ROW_MAPPER = (rs, rowNum) -> CopyTransfer.builder()
            .copyId(rs.getLong("copy_id"))
            .transferId(rs.getLong("transfer_id"))
            .build();

    public void save(CopyTransfer copyTransfer) {
        String sql = """
				INSERT INTO copy_transfers (copy_id, transfer_id)
				VALUES (?, ?);
				""";

        jdbcTemplate.update(sql, copyTransfer.getCopyId(), copyTransfer.getTransferId());
    }

    public List<CopyTransfer> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM copy_transfers
				ORDER BY copy_id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, COPY_TRANSFER_ROW_MAPPER);
    }

    public CopyTransfer findByCopyIdAndTransferId(Long copyId, Long transferId) {
        String sql = """
				SELECT *
				FROM copy_transfers
				WHERE copy_id = ? AND transfer_id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, COPY_TRANSFER_ROW_MAPPER, copyId, transferId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<CopyTransfer> findAllByTransferId(Long transferId) {
        String sql = """
				SELECT *
				FROM copy_transfers
				WHERE transfer_id = ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {transferId}, COPY_TRANSFER_ROW_MAPPER);
    }

    //    public void update(CopyTransfer copyTransfer) {
    //        String sql =
    //                """
    //				UPDATE copy_transfers
    //				SET start_date = ?, end_date = ?, return_date = ?, fee = ?, fine = ?, status_on_return = ?
    //				WHERE copy_id = ? AND borrow_ticket_id = ?;
    //				""";
    //
    //        jdbcTemplate.update(
    //                sql,
    //                copyTransfer.getStartDate(),
    //                copyTransfer.getEndDate(),
    //                copyTransfer.getReturnDate(),
    //                copyTransfer.getFee(),
    //                copyTransfer.getFine(),
    //                copyTransfer.getStatusOnReturn(),
    //                copyTransfer.getCopyId(),
    //                copyTransfer.getBorrowTicketId());
    //    }

    public void deleteByCopyIdAndTransferId(Long copyId, Long transferId) {
        String sql = """
				DELETE FROM copy_transfers
				WHERE copy_id = ? AND transfer_id = ?;
				""";

        jdbcTemplate.update(sql, copyId, transferId);
    }

    public boolean existsByCopyIdAndBorrowTicketId(Long copyId, Long transferId) {
        String sql = """
				SELECT COUNT(*)
				FROM copy_transfers
				WHERE copy_id = ? AND transfer_id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {copyId, transferId}, Integer.class);

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
