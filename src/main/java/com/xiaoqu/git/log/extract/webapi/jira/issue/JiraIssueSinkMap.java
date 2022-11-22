package com.xiaoqu.git.log.extract.webapi.jira.issue;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.api.common.functions.RichMapFunction;

public class JiraIssueSinkMap extends RichMapFunction<JiraIssue, JiraIssue> {
    private final SystemConfig.DatabaseConfig dbConfig;

    public JiraIssueSinkMap(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }


    @Override
    public JiraIssue map(JiraIssue value) throws Exception {
        JiraIssueSink jiraIssueSink = new JiraIssueSink(dbConfig);
        jiraIssueSink.open(null);
        jiraIssueSink.invoke(value, null);
        jiraIssueSink.close();
        return value;
    }
}
