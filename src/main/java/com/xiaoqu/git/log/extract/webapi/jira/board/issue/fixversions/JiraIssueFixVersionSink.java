package com.xiaoqu.git.log.extract.webapi.jira.board.issue.fixversions;

import com.xiaoqu.git.log.extract.common.SinkBase;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssue;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JiraIssueFixVersionSink extends SinkBase<JiraIssue> {
    private final Logger logger = LoggerFactory.getLogger(JiraIssueFixVersionSink.class);
    @Override
    public void open(Configuration parameters) throws Exception {
        String table = getTableName("jira_issue_fix_version");
        String sql = String.format("INSERT INTO %s(`issue_key`,name) VALUES (?, ?) ", table);
        prepare(sql);
    }

    @Override
    public void invoke(JiraIssue value, Context context) throws Exception {
        for (JiraIssue.Fields.FixVersion fixVersion : value.fields.fixVersions) {
            preparedStatement.setString(1, value.key);
            preparedStatement.setString(2, fixVersion.name);
            try {
                preparedStatement.execute();
            } catch (Exception e) {
                logger.error("exec insert jira_issue_fix_version error.{}", e.getMessage(), e);
            }
        }
    }
}
