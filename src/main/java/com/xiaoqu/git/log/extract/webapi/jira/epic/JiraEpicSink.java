package com.xiaoqu.git.log.extract.webapi.jira.epic;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JiraEpicSink extends RichSinkFunction<JiraEpic> {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    @Override
    public void open(Configuration parameters) throws Exception {
        extracted("insert into jira_epic(id, `key`, link, name, summary, is_done, board_id)values(?,?,?,?,?,?,?) on duplicate key update id = id;", SystemConfigLoader.config.getDb());
    }

    private void extracted(String sql, SystemConfig.DatabaseConfig dbConfig) throws ClassNotFoundException, SQLException {
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

    @Override
    public void invoke(JiraEpic value, Context context) throws SQLException {
        preparedStatement.setString(1, value.id);
        preparedStatement.setString(2, value.key);
        preparedStatement.setString(3, value.link);
        preparedStatement.setString(4, value.name);
        preparedStatement.setString(5, value.summary);
        preparedStatement.setBoolean(6, value.isDone);
        preparedStatement.setString(7, value.boardId);
        preparedStatement.execute();
    }
}
