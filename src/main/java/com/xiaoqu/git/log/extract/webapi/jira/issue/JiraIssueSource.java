package com.xiaoqu.git.log.extract.webapi.jira.issue;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.webapi.jira.epic.JiraEpic;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JiraIssueSource extends RichSourceFunction<JiraEpic> {
    private SystemConfig.DatabaseConfig dbConfig;

    public JiraIssueSource(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void run(SourceContext<JiraEpic> ctx) throws Exception {
        String driver = dbConfig.getDriver();
        String url = dbConfig.getUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            try(ResultSet resultSet = statement.executeQuery("select id, board_id from jira_epic")) {
                while (resultSet.next()) {
                    JiraEpic jiraEpic = new JiraEpic();
                    jiraEpic.id = resultSet.getString(1);
                    jiraEpic.boardId = resultSet.getString(2);
                    ctx.collect(jiraEpic);
                }
            }
        }

    }

    @Override
    public void cancel() {

    }
}
