package com.xiaoqu.git.log.extract.webapi.jira.issue;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.webapi.jira.epic.JiraEpic;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;

import static com.xiaoqu.git.log.extract.common.RequestUtils.sendRequest;

public class JiraIssueFlow extends RichFlatMapFunction<JiraEpic, JiraIssue> {
    private final SystemConfig.JiraConfig jiraConfig;

    public JiraIssueFlow(SystemConfig.JiraConfig jiraConfig) {
        this.jiraConfig = jiraConfig;
    }

    @Override
    public void flatMap(JiraEpic value, Collector<JiraIssue> ctx) throws Exception {
        int start = 0;
        while (true) {
            JiraIssueResponse issues = getIssues(value, start);
            if (!issues.hasNext()) {
                break;
            } else {
                issues.issues.forEach(ctx::collect);
                start = 50 + start;
            }
        }

    }

    private JiraIssueResponse getIssues(JiraEpic jiraEpic, int startAt) throws IOException {
        String url = String.format("%s/rest/agile/1.0/board/%s/epic/%s/issue?startAt=%s&limit=50", jiraConfig.getUrl(), jiraEpic.boardId, jiraEpic.id, startAt);
        return sendRequest(url, jiraConfig.getToken(), JiraIssueResponse.class);
    }
}
