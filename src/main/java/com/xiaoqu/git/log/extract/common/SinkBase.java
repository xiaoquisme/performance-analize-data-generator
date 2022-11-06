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
        String driver = dbConfig.getDriver();
        String url = dbConfig.getUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
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
}

