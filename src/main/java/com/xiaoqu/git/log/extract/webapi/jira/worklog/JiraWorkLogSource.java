package com.xiaoqu.git.log.extract.webapi.jira.worklog;

import com.xiaoqu.git.log.extract.common.SourceBase;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.webapi.jira.issue.JiraIssue;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JiraWorkLogSource extends SourceBase<JiraIssue> {

    public JiraWorkLogSource(SystemConfig.DatabaseConfig dbConfig) {
        super(dbConfig);
    }


    @Override
    protected String getSql() {
        return "select id from jira_issue";
    }

    @Override
    protected void collectResult(SourceContext<JiraIssue> ctx, ResultSet resultSet) throws SQLException {
        JiraIssue issue = new JiraIssue();
        issue.id = resultSet.getString(1);
        ctx.collect(issue);
    }
}
