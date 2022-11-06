package com.xiaoqu.git.log.extract.webapi.jira.issue;

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
        String sql = "INSERT INTO performance_analyze.jira_issue (id, epic_id, epic_key, `key`, issue_type, title, discription, timetracking_spent, story_pont, current_sprint, status, reporter, assignee) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  on duplicate key update id = id;";
        prepare(sql, dbConfig);
    }

    @Override
    public void invoke(JiraIssue value, Context context) throws Exception {
        preparedStatement.setString(1, value.id);
        preparedStatement.setString(2, value.fields.epic.id);
        preparedStatement.setString(3, value.fields.epic.key);
        preparedStatement.setString(4, value.key);
        preparedStatement.setString(5, value.getIssueType());
        preparedStatement.setString(6, value.fields.title);
        preparedStatement.setString(7, value.fields.description);
        preparedStatement.setString(8, value.fields.timetracking.timeTrackingSpent);
        preparedStatement.setString(9, value.getStoryPoint());
        preparedStatement.setString(10, Optional.ofNullable( value.fields.sprint).map(item -> item.currentSprint).orElse(null));
        preparedStatement.setString(11, value.fields.status.name);
        preparedStatement.setString(12, value.fields.reporter.email);
        preparedStatement.setString(13, Optional.ofNullable( value.fields.assignee).map(item -> item.email).orElse(null));
        preparedStatement.execute();
    }
}
