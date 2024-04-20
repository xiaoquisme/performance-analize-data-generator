package com.xiaoqu.git.log.extract.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static com.xiaoqu.git.log.extract.common.SystemConfigLoader.getHikariConfig;

public class MysqlThreadPool {

    public static Connection getConnection() throws SQLException {
        HikariConfig config = getHikariConfig();
        HikariDataSource ds = new HikariDataSource(config);
        return ds.getConnection();
    }
}
