package com.xiaoqu.git.log.extract.webapi.jira.board.sprint;

import com.xiaoqu.git.log.extract.common.SinkBase;
import org.apache.flink.configuration.Configuration;

public class JiraSprintSink extends SinkBase<JiraSprintResponse.JiraSprint> {
    @Override
    public void open(Configuration parameters) throws Exception {
        String tableName = getTableName("jira_sprint");
        String sql = String.format("INSERT INTO %s(id, boardId, state, name, goal) VALUES (?, ?, ?, ?, ?) on duplicate key update id = id;", tableName);
        prepare(sql);
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
