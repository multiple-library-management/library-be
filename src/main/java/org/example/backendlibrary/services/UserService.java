package org.example.backendlibrary.services;

import lombok.RequiredArgsConstructor;
import org.example.backendlibrary.entities.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    public void saveUser(String name, int age){
        String sql = "INSERT INTO users (name, age) VALUES (?, ?)";
        jdbcTemplate.update(sql, name, age);
    }

    // Fetch all users
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    // Custom RowMapper
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setAge(rs.getInt("age"));
            return user;
        }
    }

}
