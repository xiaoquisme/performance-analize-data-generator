package com.xiaoqu.git.log.extract.webapi.jira.board.sprint;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.api.common.functions.RichMapFunction;

public class JIraSprintSinkMap extends RichMapFunction<JiraSprintResponse.JiraSprint, JiraSprintResponse.JiraSprint> {
    private final SystemConfig.DatabaseConfig dbConfig;

    public JIraSprintSinkMap(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public JiraSprintResponse.JiraSprint map(JiraSprintResponse.JiraSprint value) throws Exception {
        JiraSprintSink jiraSprintSink = new JiraSprintSink(this.dbConfig);
        jiraSprintSink.open(null);
        jiraSprintSink.invoke(value, null);
        jiraSprintSink.close();
        return value;
    }
}
