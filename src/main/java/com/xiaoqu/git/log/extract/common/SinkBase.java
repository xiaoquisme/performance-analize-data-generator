package com.xiaoqu.git.log.extract.common;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.xiaoqu.git.log.extract.common.MysqlThreadPool.getConnection;

public abstract class SinkBase<T> extends RichSinkFunction<T> {
    protected Connection connection = null;
    protected PreparedStatement preparedStatement = null;

    protected void prepare(String sql) throws SQLException {
        connection = getConnection();
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
        return String.format("%s_%s", tableName, DatetimeUtils.currentDay);
    }
}

