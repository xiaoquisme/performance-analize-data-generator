package com.xiaoqu.git.log.extract.common;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class SinkBase<T> extends RichSinkFunction<T> {
    protected Connection connection = null;
    protected PreparedStatement preparedStatement = null;

    protected void prepare(String sql, SystemConfig.DatabaseConfig dbConfig) throws ClassNotFoundException, SQLException {
        String driver = dbConfig.driver;
        String url = dbConfig.url;
        String username = dbConfig.username;
        String password = dbConfig.password;
        Class.forName(driver);

        connection = DriverManager.getConnection(url, username, password);
        preparedStatement = connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        super.close();
    }

    protected String getTableName(String tableName) {
        return String.format("%s_%s", tableName, DatetimeUtils.currentDay.get());
    }
}

