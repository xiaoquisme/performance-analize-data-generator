package com.xiaoqu.git.log.extract.webapi.jira.board.sprint;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraSprintResponse {
    @JsonProperty("values")
    public List<JiraSprint> items = Collections.emptyList();
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JiraSprint {
        public String id;
        public String boardId;
        public String state;
        public String name;
        public String goal;
    }
}
