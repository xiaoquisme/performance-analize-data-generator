package com.xiaoqu.git.log.extract.webapi.jira.board.issue;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
    public String id;
    public String key;

    public Fields fields;

    public String getIssueType() {
        return fields.issuetype.name;
    }

    public String getStoryPoint() {
        return fields.stroyPoint;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fields {
        public IssueType issuetype;
        @JsonProperty("customfield_10028")
        public String stroyPoint;
        @JsonProperty("summary")
        public String title;
        @JsonProperty("description")
        public String description;

        public TimeTracking timetracking;

        @JsonProperty("reporter")
        public People reporter;

        @JsonProperty("assignee")
        public People assignee;

        @JsonProperty("sprint")
        public Sprint sprint;
        public FiledStatus  status;

        public Epic epic;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class IssueType {
            public String name;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TimeTracking {
            @JsonProperty("timeSpent")
            public String timeTrackingSpent;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Sprint {
            @JsonProperty("name")
            public String currentSprint;

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FiledStatus {
            @JsonProperty("name")
            public String name;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class People {
            @JsonProperty("emailAddress")
            public String email;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Epic {
            public String id;
            public String key;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FixVersion {
            public String name;
        }
    }
}
