package com.xiaoqu.git.log.extract.webapi.jira.board;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JiraBoardSink extends RichSinkFunction<JiraBoardResponse.JiraBoard> {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    @Override
    public void open(Configuration parameters) throws Exception {
        SystemConfig.DatabaseConfig dbConfig = SystemConfigLoader.config.getDb();
        String driver = dbConfig.getDriver();
        String url = dbConfig.getUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        Class.forName(driver);

        connection = DriverManager.getConnection(url, username, password);
        String sql = "insert into jira_board(id,name, type)values(?,?,?) on duplicate key update id = id;";
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
    public void invoke(JiraBoardResponse.JiraBoard value, Context context) throws Exception {
        try {
            preparedStatement.setString(1, value.id);
            preparedStatement.setString(2, value.name);
            preparedStatement.setString(3, value.type);
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e);
            System.out.printf("===============================================%s", value);
        }

    }
}
