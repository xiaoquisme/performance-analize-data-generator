package com.xiaoqu.git.log.extract.localfile;


import com.xiaoqu.git.log.extract.common.CommitLog;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SinkToMysql extends RichSinkFunction<Tuple2<CommitLog, String>> {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/actix_example";
        String username = "root";
        String password = "123456";
        Class.forName(driver);

        connection = DriverManager.getConnection(url, username, password);
        String sql = "insert into git_log(id,jira_no, message, author)values(?,?,?,?);";
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
    public void invoke(Tuple2<CommitLog, String> value, Context context) throws Exception {
        CommitLog value1 = value.f0;
        try {
            preparedStatement.setString(1, value1.getCommitId());
            preparedStatement.setString(2, value1.getJiraNo());
            preparedStatement.setString(3, value1.getMessage());
            preparedStatement.setString(4, value1.getUserName());
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.printf("===============================================%s, %s%n", value1, value.f1);
        }

    }
}
