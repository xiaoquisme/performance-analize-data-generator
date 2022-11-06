package com.xiaoqu.git.log.extract.webapi;


import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MysqlSink extends RichSinkFunction<CommitLog> {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    @Override
    public void open(Configuration parameters) throws Exception {
        SystemConfig.DatabaseConfig dbConfig = DataStreamWebApiJob.config.getDb();
        String driver = dbConfig.getDriver();
        String url = dbConfig.getUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        Class.forName(driver);

        connection = DriverManager.getConnection(url, username, password);
        String sql = "insert into git_log(id,repo_owner,project,jira_no, message, author, commit_date,commit_email)values(?,?,?,?,?,?,?,?) on duplicate key update id = id;";
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
            preparedStatement.setString(2, value1.getRepoOwner());
            preparedStatement.setString(3, value1.getRepoName());
            preparedStatement.setString(4, value1.getJiraNo());
            preparedStatement.setString(5, value1.getMessage());
            preparedStatement.setString(6, value1.getUserName());
            preparedStatement.setString(7, value1.getCommitDate());
            preparedStatement.setString(8, value1.getCommitEmail());
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e);
            System.out.printf("===============================================%s", value1);
        }

    }
}
