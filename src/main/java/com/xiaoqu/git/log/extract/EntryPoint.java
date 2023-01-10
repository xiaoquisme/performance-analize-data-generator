package com.xiaoqu.git.log.extract;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import com.xiaoqu.git.log.extract.webapi.github.GithubWebApiJob;
import com.xiaoqu.git.log.extract.webapi.jira.board.JiraBoardJob;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class EntryPoint {
    public static void main(String[] args) throws Exception {
        trancuateJiraDB();
        GithubWebApiJob.run();
        JiraBoardJob.run();
    }

    private static void trancuateJiraDB() throws SQLException, ClassNotFoundException {
        try(Connection dbConnection = getDbConnection(SystemConfigLoader.config.getDb())) {
            List<String> list = Arrays.asList(
                    "truncate table jira_sprint;",
                    "truncate table jira_issue;",
                    "truncate table jira_worklog;",
                    "truncate table jira_board;",
                    "truncate table jira_epic;"
            );
            Statement statement = dbConnection.createStatement();
            list.forEach(sql -> {
                try  {
                    statement.executeUpdate(sql);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            statement.close();
        }
    }

    private static Connection getDbConnection(SystemConfig.DatabaseConfig dbConfig) throws ClassNotFoundException, SQLException {
        String driver = dbConfig.getDriver();
        String url = dbConfig.getUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        Class.forName(driver);

        return DriverManager.getConnection(url, username, password);
    }

}
