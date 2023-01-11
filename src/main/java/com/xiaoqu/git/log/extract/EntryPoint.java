package com.xiaoqu.git.log.extract;

import com.xiaoqu.git.log.extract.common.DatetimeUtils;
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
        String currentDay = DatetimeUtils.currentDay;
        createJiraTable(currentDay);
        GithubWebApiJob.run();
        JiraBoardJob.run();
    }

    private static void createJiraTable(String currentDay) throws SQLException, ClassNotFoundException {
        try (Connection dbConnection = getDbConnection(SystemConfigLoader.config.getDb())) {
            List<String> list = Arrays.asList(
                    String.format("call create_jira_board('%s');", currentDay),
                    String.format("call create_jira_epic('%s');", currentDay),
                    String.format("call create_jira_sprint('%s');", currentDay),
                    String.format("call create_jira_issue('%s');", currentDay),
                    String.format("call create_jira_worklog('%s');", currentDay)
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
