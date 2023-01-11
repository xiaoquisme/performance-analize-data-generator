package com.xiaoqu.git.log.extract.webapi.jira.board.issue;

import com.xiaoqu.git.log.extract.common.SinkBase;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.configuration.Configuration;

import java.util.Optional;

public class JiraIssueSink extends SinkBase<JiraIssue> {
    private final SystemConfig.DatabaseConfig dbConfig;

    public JiraIssueSink(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        String table = getTableName("jira_issue");
        String sql = String.format("INSERT INTO %s(id, epic_id, epic_key, `key`, issue_type, title, discription, timetracking_spent, story_point, current_sprint, status, reporter, assignee) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  on duplicate key update id = id;", table);
        prepare(sql, dbConfig);
    }

    @Override
    public void invoke(JiraIssue value, Context context) throws Exception {
        preparedStatement.setString(1, value.id);
        preparedStatement.setString(2, Optional.ofNullable(value.fields.epic).map(item -> item.id).orElse(null));
        preparedStatement.setString(3, Optional.ofNullable(value.fields.epic).map(item -> item.key).orElse(null));
        preparedStatement.setString(4, value.key);
        preparedStatement.setString(5, value.getIssueType());
        preparedStatement.setString(6, value.fields.title);
        preparedStatement.setString(7, value.fields.description);
        preparedStatement.setString(8, value.fields.timetracking.timeTrackingSpent);
        preparedStatement.setString(9, value.getStoryPoint());
        preparedStatement.setString(10, Optional.ofNullable(value.fields.sprint).map(item -> item.currentSprint).orElse(null));
        preparedStatement.setString(11, value.fields.status.name);
        preparedStatement.setString(12, value.fields.reporter.email);
        preparedStatement.setString(13, Optional.ofNullable(value.fields.assignee).map(item -> item.email).orElse(null));
        preparedStatement.execute();
    }
}
