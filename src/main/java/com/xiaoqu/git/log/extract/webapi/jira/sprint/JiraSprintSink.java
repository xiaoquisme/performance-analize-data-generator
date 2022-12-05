package com.xiaoqu.git.log.extract.webapi.jira.sprint;

import com.xiaoqu.git.log.extract.common.SinkBase;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.configuration.Configuration;

public class JiraSprintSink extends SinkBase<JiraSprintResponse.JiraSprint> {
    private final SystemConfig.DatabaseConfig dbConfig;

    public JiraSprintSink(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        String sql = "INSERT INTO performance_analyze.jira_sprint (id, boardId, state, name, goal) VALUES (?, ?, ?, ?, ?) on duplicate key update id = id;";
        prepare(sql, dbConfig);
    }

    @Override
    public void invoke(JiraSprintResponse.JiraSprint value, Context context) throws Exception {
        preparedStatement.setString(1, value.id);
        preparedStatement.setString(2, value.boardId);
        preparedStatement.setString(3, value.state);
        preparedStatement.setString(4, value.name);
        preparedStatement.setString(5, value.goal);
        preparedStatement.execute();
    }
}
