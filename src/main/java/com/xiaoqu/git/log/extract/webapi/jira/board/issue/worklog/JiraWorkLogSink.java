package com.xiaoqu.git.log.extract.webapi.jira.board.issue.worklog;

import com.xiaoqu.git.log.extract.common.SinkBase;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.configuration.Configuration;

public class JiraWorkLogSink extends SinkBase<JiraWorkLog> {
    private final SystemConfig.DatabaseConfig dbConfig;

    public JiraWorkLogSink(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        String tableName = getTableName("jira_worklog");
        String sql = String.format("INSERT INTO %s (id, issue_id, updater_name, update_author, created, time_spent) VALUES (?, ?, ?, ?, ?, ?) on duplicate key update id = id;", tableName);
        prepare(sql, dbConfig);
    }

    @Override
    public void invoke(JiraWorkLog value, Context context) throws Exception {
        preparedStatement.setString(1, value.id);
        preparedStatement.setString(2, value.issueId);
        preparedStatement.setString(3, value.updateAuthor.displayName);
        preparedStatement.setString(4, value.updateAuthor.emailAddress);
        preparedStatement.setString(5, value.created);
        preparedStatement.setString(6, value.timeSpent);
        preparedStatement.execute();
    }
}
