package com.xiaoqu.git.log.extract.webapi.jira.board.sprint;

import org.apache.flink.api.common.functions.RichMapFunction;

public class JIraSprintSinkMap extends RichMapFunction<JiraSprintResponse.JiraSprint, JiraSprintResponse.JiraSprint> {
    @Override
    public JiraSprintResponse.JiraSprint map(JiraSprintResponse.JiraSprint value) throws Exception {
        JiraSprintSink jiraSprintSink = new JiraSprintSink();
        jiraSprintSink.open(null);
        jiraSprintSink.invoke(value, null);
        jiraSprintSink.close();
        return value;
    }
}
