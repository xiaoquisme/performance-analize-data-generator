package com.xiaoqu.git.log.extract.webapi.jira.worklog;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraWorkLog {
    public String id;
    public String issueId;
    public Author updateAuthor;
    public String created;
    public String timeSpent;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {
        public String emailAddress;
        public String displayName;
    }
}
