package com.xiaoqu.git.log.extract.webapi.jira.board.issue;

import org.apache.flink.api.common.functions.RichMapFunction;

public class JiraIssueSinkMap extends RichMapFunction<JiraIssue, JiraIssue> {
    @Override
    public JiraIssue map(JiraIssue value) throws Exception {
        JiraIssueSink jiraIssueSink = new JiraIssueSink();
        jiraIssueSink.open(null);
        jiraIssueSink.invoke(value, null);
        jiraIssueSink.close();
        return value;
    }
}
