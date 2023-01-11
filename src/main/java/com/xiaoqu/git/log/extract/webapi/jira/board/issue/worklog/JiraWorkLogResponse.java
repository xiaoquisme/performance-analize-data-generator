package com.xiaoqu.git.log.extract.webapi.jira.board.issue.worklog;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraWorkLogResponse {
    @JsonProperty("worklogs")
    public List<JiraWorkLog> workLogs = Collections.emptyList();
}
