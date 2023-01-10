package com.xiaoqu.git.log.extract;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import com.xiaoqu.git.log.extract.webapi.github.GithubWebApiJob;
import com.xiaoqu.git.log.extract.webapi.jira.board.JiraBoardJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class EntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) throws Exception {
        createJiraTable();
        GithubWebApiJob.run();
        JiraBoardJob.run();
    }

    private static void createJiraTable() throws SQLException, ClassNotFoundException {
        try (Connection dbConnection = getDbConnection(SystemConfigLoader.config.getDb())) {
            List<String> list = Arrays.asList(
                    "call create_jira_board(DATE_FORMAT(CURDATE(), '%Y_%m_%d'));",
                    "call create_jira_epic(DATE_FORMAT(CURDATE(), '%Y_%m_%d'));",
                    "call create_jira_sprint(DATE_FORMAT(CURDATE(), '%Y_%m_%d'));",
                    "call create_jira_issue(DATE_FORMAT(CURDATE(), '%Y_%m_%d'));",
                    "call create_jira_worklog(DATE_FORMAT(CURDATE(), '%Y_%m_%d'));"
            );
            Statement statement = dbConnection.createStatement();
            list.forEach(sql -> {
                try {
                    statement.executeUpdate(sql);
                } catch (Exception e) {
                    logger.error("exec create jira table error{}.{}", sql, e.getMessage(), e);
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
