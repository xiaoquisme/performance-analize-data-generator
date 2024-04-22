package com.xiaoqu.git.log.extract.webapi.jira.board.issue.fixversions;

import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssue;
import org.apache.flink.api.common.functions.RichMapFunction;

public class JiraIssueFixVersionSinkMap extends RichMapFunction<JiraIssue, JiraIssue> {

    @Override
    public JiraIssue map(JiraIssue value) throws Exception {
        JiraIssueFixVersionSink jiraIssueFixVersionSink = new JiraIssueFixVersionSink();
        jiraIssueFixVersionSink.open(null);
        jiraIssueFixVersionSink.invoke(value, null);
        jiraIssueFixVersionSink.close();
        return value;
    }
}
