package com.xiaoqu.git.log.extract.common;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.*;

public abstract class SourceBase<T> extends RichSourceFunction<T> {
    private final SystemConfig.DatabaseConfig dbConfig;

    public SourceBase(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void run(SourceContext<T> ctx) throws Exception {
        String driver = dbConfig.driver;
        String url = dbConfig.url;
        String username = dbConfig.username;
        String password = dbConfig.password;
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            try (ResultSet resultSet = statement.executeQuery(getSql())) {
                while (resultSet.next()) {
                    collectResult(ctx, resultSet);
                }
            }
        }

    }

    @Override
    public void cancel() {

    }

    protected abstract String getSql();

    protected abstract void collectResult(SourceContext<T> ctx, ResultSet resultSet) throws SQLException;

}
