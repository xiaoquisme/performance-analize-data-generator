package com.xiaoqu.git.log.extract.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.xiaoqu.git.log.extract.common.SystemConfigLoader.getHikariConfig;

public class MysqlThreadPool {

    private static final HikariConfig config = getHikariConfig();
    public static Connection getConnection() throws SQLException {
        String driver = config.getDriverClassName();
        String url = config.getJdbcUrl();
        String username = config.getUsername();
        String password = config.getPassword();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {

        }
        return DriverManager.getConnection(url, username, password);
    }
}
