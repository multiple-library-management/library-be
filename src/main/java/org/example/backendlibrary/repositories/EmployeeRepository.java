package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, rowNum) -> Employee.builder()
            .id(rs.getLong("id"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .address(rs.getString("address"))
            .district(rs.getString("district"))
            .ward(rs.getString("ward"))
            .street(rs.getString("street"))
            .city(rs.getString("city"))
            .phone(rs.getString("phone"))
            .email(rs.getString("email"))
            .salary(rs.getInt("salary"))
            .build();

    public Long save(Employee employee) {
        String sql =
                """
				INSERT INTO employees (first_name, last_name, address, district, ward, street, city, phone, email, salary)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAddress(),
                employee.getDistrict(),
                employee.getWard(),
                employee.getStreet(),
                employee.getCity(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getSalary());
    }

    public List<Employee> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM employees
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, EMPLOYEE_ROW_MAPPER);
    }

    public Employee findById(Long id) {
        String sql = """
				SELECT *
				FROM employees
				WHERE id = ?;
				""";

        return jdbcTemplate.queryForObject(sql, EMPLOYEE_ROW_MAPPER, id);
    }

    public int update(Employee employee) {
        String sql =
                """
				UPDATE employees
				SET first_name = ?, last_name = ?, address = ?, district = ?, ward = ?, street = ?, city = ?, phone = ?, email = ?, salary = ?
				WHERE id = ?;
				""";

        return jdbcTemplate.update(
                sql,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAddress(),
                employee.getDistrict(),
                employee.getWard(),
                employee.getStreet(),
                employee.getCity(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getSalary(),
                employee.getId()
        );
    }

    public int deleteById(Long id) {
        String sql = """
				DELETE FROM employees WHERE id = ?;
				""";

        return jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM employees WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM employees
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
