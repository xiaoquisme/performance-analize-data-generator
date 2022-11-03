package com.xiaoqu.git.log.extract.webapi;


import com.xiaoqu.git.log.extract.common.CommitLog;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SinkToMysqlForWebApi extends RichSinkFunction<CommitLog> {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:30006/performance_analyze";
        String username = "root";
        String password = "root";
        Class.forName(driver);

        connection = DriverManager.getConnection(url, username, password);
        String sql = "insert into git_log(id,jira_no, message, author, commit_date,commit_email)values(?,?,?,?,?,?);";
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
    public void invoke(CommitLog value, Context context) throws Exception {
        CommitLog value1 = value;
        try {
            preparedStatement.setString(1, value1.getCommitId());
            preparedStatement.setString(2, value1.getJiraNo());
            preparedStatement.setString(3, value1.getMessage());
            preparedStatement.setString(4, value1.getUserName());
            preparedStatement.setString(5, value1.getCommitDate());
            preparedStatement.setString(6, value1.getCommitEmail());
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e);
            System.out.printf("===============================================%s", value1);
        }

    }
}
