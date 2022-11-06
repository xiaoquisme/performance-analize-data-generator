package com.xiaoqu.git.log.extract.webapi.jira.epic;


import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JiraEpicSource extends RichSourceFunction<JiraBoardDb> {
    private SystemConfig.DatabaseConfig dbConfig;

    public JiraEpicSource(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void run(SourceContext<JiraBoardDb> ctx) throws Exception {
        String driver = dbConfig.getDriver();
        String url = dbConfig.getUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            try(ResultSet resultSet = statement.executeQuery("select id, name, type from jira_board")) {
                while (resultSet.next()) {
                    JiraBoardDb jiraBoardDb = new JiraBoardDb();
                    jiraBoardDb.id = resultSet.getString(1);
                    jiraBoardDb.name = resultSet.getString(2);
                    jiraBoardDb.type = resultSet.getString(3);
                    ctx.collect(jiraBoardDb);
                }
            }
        }

    }

    @Override
    public void cancel() {

    }
}
