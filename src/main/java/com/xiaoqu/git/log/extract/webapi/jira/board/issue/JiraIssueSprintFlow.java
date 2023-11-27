package com.xiaoqu.git.log.extract.webapi.jira.board.issue;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.webapi.jira.board.sprint.JiraSprintResponse;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;

import static com.xiaoqu.git.log.extract.common.RequestUtils.sendRequestBasic;

public class JiraIssueSprintFlow extends RichFlatMapFunction<JiraSprintResponse.JiraSprint, JiraIssue> {
    private final SystemConfig.JiraConfig jiraConfig;
    private final String requestPath;

    public JiraIssueSprintFlow(SystemConfig.JiraConfig jiraConfig, String requestPath) {
        this.jiraConfig = jiraConfig;
        this.requestPath = requestPath;
    }

    @Override
    public void flatMap(JiraSprintResponse.JiraSprint value, Collector<JiraIssue> ctx) throws Exception {
        int start = 0;
        while (true) {
            JiraIssueResponse issues = getIssues(value, start, requestPath);
            if (!issues.hasNext()) {
                break;
            } else {
                issues.issues.forEach(ctx::collect);
                start = 50 + start;
            }
        }

    }

    private JiraIssueResponse getIssues(JiraSprintResponse.JiraSprint jiraSprint, int startAt, String requestPath) throws IOException {
        String url = String.format(requestPath, jiraConfig.url, jiraSprint.boardId, jiraSprint.id, startAt);
        return sendRequestBasic(url, jiraConfig.username, jiraConfig.token, JiraIssueResponse.class).orElse(new JiraIssueResponse());
    }
}
