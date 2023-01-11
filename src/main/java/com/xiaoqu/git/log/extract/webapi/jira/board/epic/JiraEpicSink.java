package com.xiaoqu.git.log.extract.webapi.jira.board.epic;

import com.xiaoqu.git.log.extract.common.SinkBase;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.configuration.Configuration;

import java.sql.SQLException;

public class JiraEpicSink extends SinkBase<JiraEpic> {
    private final SystemConfig.DatabaseConfig dbConfig;

    public JiraEpicSink(SystemConfig.DatabaseConfig config) {
        dbConfig = config;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        String sql = "insert into jira_epic(id, `key`, link, name, summary, is_done, board_id)values(?,?,?,?,?,?,?) on duplicate key update id = id;";
        prepare(sql, dbConfig);
    }

    @Override
    public void invoke(JiraEpic value, Context context) throws SQLException {
        preparedStatement.setString(1, value.id);
        preparedStatement.setString(2, value.key);
        preparedStatement.setString(3, value.link);
        preparedStatement.setString(4, value.name);
        preparedStatement.setString(5, value.summary);
        preparedStatement.setBoolean(6, value.isDone);
        preparedStatement.setString(7, value.boardId);
        preparedStatement.execute();
    }
}