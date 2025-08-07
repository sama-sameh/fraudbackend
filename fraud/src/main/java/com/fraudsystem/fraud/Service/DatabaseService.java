package com.fraudsystem.fraud.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getAllTableNames() {
        return jdbcTemplate.queryForList("SHOW TABLES", String.class);
    }
    public List<String> getColumnsForTable(String databaseName, String tableName) {
        String sql = "SELECT COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

        return jdbcTemplate.queryForList(sql, new Object[]{databaseName, tableName}, String.class);
    }
}
