package com.xiaoqu.git.log.extract.webapi.jira.board;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraBoardResponse {
    public List<JiraBoard> values;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JiraBoard {
        public String id;
        public String name;
        public String type;
    }
}
