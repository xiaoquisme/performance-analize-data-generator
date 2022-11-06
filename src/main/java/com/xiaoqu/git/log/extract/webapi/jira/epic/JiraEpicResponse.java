package com.xiaoqu.git.log.extract.webapi.jira.epic;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraEpicResponse {
    public List<JiraEpic> values;
}
