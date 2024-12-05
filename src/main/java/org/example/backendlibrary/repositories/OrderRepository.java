package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, rowNum) -> Order.builder()
            .id(rs.getLong("id"))
            .createdDate(rs.getTimestamp("created_date"))
            .shipStartDate(rs.getTimestamp("ship_start_date"))
            .shipEndDate(rs.getTimestamp("ship_end_date"))
            .totalPrice(rs.getInt("total_price"))
            .warehouseId(rs.getInt("warehouse_id"))
            .warehouseStaffId(rs.getInt("warehouse_staff_id"))
            .build();

    public Long save(Order order) {
        String sql =
                """
				INSERT INTO orders (created_date, ship_start_date, ship_end_date, total_price, warehouse_id, warehouse_staff_id)
				VALUES (?, ?, ?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                order.getCreatedDate(),
                order.getShipStartDate(),
                order.getShipEndDate(),
                order.getTotalPrice(),
                order.getWarehouseId(),
                order.getWarehouseStaffId());
    }

    public List<Order> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM orders
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, ORDER_ROW_MAPPER);
    }

    public Order findById(Long id) {
        String sql = """
				SELECT *
				FROM orders
				WHERE id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, ORDER_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(Order order) {
        String sql =
                """
				UPDATE orders
				SET created_date = ?, ship_start_date = ?, ship_end_date = ?, total_price = ?, warehouse_id = ?, warehouse_staff_id = ?
				WHERE id = ?;
				""";

        jdbcTemplate.update(
                sql,
                order.getCreatedDate(),
                order.getShipStartDate(),
                order.getShipEndDate(),
                order.getTotalPrice(),
                order.getWarehouseId(),
                order.getWarehouseStaffId(),
                order.getId());
    }

    public void deleteById(Long id) {
        String sql = """
				DELETE FROM orders WHERE id = ?;
				""";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM orders WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM orders
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
