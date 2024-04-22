package com.xiaoqu.git.log.extract.webapi.jira.board.issue.lables;

import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssue;
import org.apache.flink.api.common.functions.RichMapFunction;

public class JiraIssueLabelSinkMap extends RichMapFunction<JiraIssue, JiraIssue> {

    @Override
    public JiraIssue map(JiraIssue value) throws Exception {
        JiraIssueLablesSink jiraIssueLablesSink = new JiraIssueLablesSink();
        jiraIssueLablesSink.open(null);
        jiraIssueLablesSink.invoke(value, null);
        jiraIssueLablesSink.close();
        return value;
    }
}
