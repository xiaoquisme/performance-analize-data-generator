package com.xiaoqu.git.log.extract.webapi.jira.board.issue.worklog;

import com.xiaoqu.git.log.extract.common.RequestUtils;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssue;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;

public class JiraWorkLogFlow extends RichFlatMapFunction<JiraIssue, JiraWorkLog> {
    private final SystemConfig.JiraConfig jiraConfig;

    public JiraWorkLogFlow(SystemConfig.JiraConfig jiraConfig) {
        this.jiraConfig = jiraConfig;
    }

    @Override
    public void flatMap(JiraIssue value, Collector<JiraWorkLog> out) throws Exception {
        JiraWorkLogResponse jiraWorkLogResponse = getJiraWorkLog(value.id);
        jiraWorkLogResponse.workLogs.forEach(out::collect);
    }
    private JiraWorkLogResponse getJiraWorkLog (String issueId) throws IOException {
        String path = String.format("%s/rest/api/3/issue/%s/worklog", jiraConfig.url, issueId);
        return RequestUtils.sendRequestBasic(path, jiraConfig.username, jiraConfig.password, JiraWorkLogResponse.class).orElse(new JiraWorkLogResponse());
    }
}
