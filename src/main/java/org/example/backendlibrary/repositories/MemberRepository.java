package org.example.backendlibrary.repositories;

import java.util.List;

import org.example.backendlibrary.entities.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) -> Member.builder()
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
            .build();

    public Long save(Member member) {
        String sql =
                """
				INSERT INTO members (first_name, last_name, address, district, ward, street, city, phone, email)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				RETURNING id;
				""";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                member.getFirstName(),
                member.getLastName(),
                member.getAddress(),
                member.getDistrict(),
                member.getWard(),
                member.getStreet(),
                member.getCity(),
                member.getPhone(),
                member.getEmail());
    }

    public List<Member> findAll(int page, int size) {
        int offset = (page - 1) * size;

        // SQL query for pagination
        String sql = """
				SELECT *
				FROM members
				ORDER BY id
				LIMIT ? OFFSET ?;
				""";

        return jdbcTemplate.query(sql, new Object[] {size, offset}, MEMBER_ROW_MAPPER);
    }

    public Member findById(Long id) {
        String sql = """
				SELECT *
				FROM members
				WHERE id = ?;
				""";

        return jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, id);
    }

    public void update(Member member) {
        String sql =
                """
				UPDATE members
				SET first_name = ?, last_name = ?, address = ?, district = ?, ward = ?, street = ?, city = ?, phone = ?, email = ?
				WHERE id = ?;
				""";

        jdbcTemplate.update(
                sql,
                member.getFirstName(),
                member.getLastName(),
                member.getAddress(),
                member.getDistrict(),
                member.getWard(),
                member.getStreet(),
                member.getCity(),
                member.getPhone(),
                member.getEmail(),
                member.getId());
    }

    public void deleteById(Long id) {
        String sql = """
				DELETE FROM members WHERE id = ?;
				""";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = """
				SELECT COUNT(*) FROM members WHERE id = ?;
				""";
        // Execute the query and check if the count is greater than 0
        Integer count = jdbcTemplate.queryForObject(sql, new Object[] {id}, Integer.class);

        return count != null && count > 0;
    }

    public long count() {
        // SQL query to count total documents
        String sql = """
				SELECT COUNT(*) FROM members
				""";

        // Execute the query and return the result
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
