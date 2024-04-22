package com.xiaoqu.git.log.extract.common;

import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.xiaoqu.git.log.extract.common.SystemConfigLoader.getHikariConfig;

public class MysqlThreadPool {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MysqlThreadPool.class);
    private static final HikariConfig config = getHikariConfig();

    private static final List<Connection> connections = new ArrayList<>();

    public synchronized static Connection getConnection() throws SQLException {
        if (!connections.isEmpty()) {
            return connections.remove(0);
        } else {
            String driver = config.getDriverClassName();
            String url = config.getJdbcUrl();
            String username = config.getUsername();
            String password = config.getPassword();
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                logger.error("mysql driver not found.{}", e.getMessage(), e);
            }
            for (int i = 0; i < 10; i++) {
                connections.add(DriverManager.getConnection(url, username, password));
            }
            return getConnection();
        }

    }
}
