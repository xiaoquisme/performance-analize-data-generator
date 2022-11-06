package com.xiaoqu.git.log.extract.webapi.jira.epic;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraEpic {
    public String id;
    public String key;
    @JsonProperty(value = "self")
    public String link;
    public String name;
    public String summary;
    @JsonProperty(value = "done")
    public Boolean isDone;
    public String boardId;
}
