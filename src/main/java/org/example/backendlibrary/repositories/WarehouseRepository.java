package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Warehouse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WarehouseRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Warehouse> WAREHOUSE_ROW_MAPPER = (rs, rowNum) -> Warehouse.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .address(rs.getString("address"))
            .district(rs.getString("district"))
            .ward(rs.getString("ward"))
            .street(rs.getString("street"))
            .city(rs.getString("city"))
            .build();

    public Long save(Warehouse warehouse) {
        String sql =
                """
				INSERT INTO warehouses (name, address, district, ward, street, city)
				VALUES (?, ?, ?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                warehouse.getName(),
                warehouse.getAddress(),
                warehouse.getDistrict(),
                warehouse.getWard(),
                warehouse.getStreet(),
                warehouse.getCity());
    }

    public List<Warehouse> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM warehouses
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, WAREHOUSE_ROW_MAPPER);
    }

    public Warehouse findById(Long id) {
        String sql = """
				SELECT *
				FROM warehouses
				WHERE id = ?;
				""";
        try {
            return jdbcTemplate.queryForObject(sql, WAREHOUSE_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(Warehouse warehouse) {
        String sql =
                """
				UPDATE warehouses
				SET name = ?, address = ?, district = ?, ward = ?, street = ?, city = ?
				WHERE id = ?;
				""";

        jdbcTemplate.update(
                sql,
                warehouse.getName(),
                warehouse.getAddress(),
                warehouse.getDistrict(),
                warehouse.getWard(),
                warehouse.getStreet(),
                warehouse.getCity(),
                warehouse.getId());
    }

    public void deleteById(Long id) {
        String sql = """
				DELETE FROM warehouses WHERE id = ?;
				""";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM warehouses WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM warehouses
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
