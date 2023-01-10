package com.xiaoqu.git.log.extract.webapi.jira.board.issue;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueResponse {
    public int startAt;
    public int total;
    public int maxResults;
    public List<JiraIssue> issues;
    public boolean hasNext() {
        return issues.size() > 0;
    }
}
