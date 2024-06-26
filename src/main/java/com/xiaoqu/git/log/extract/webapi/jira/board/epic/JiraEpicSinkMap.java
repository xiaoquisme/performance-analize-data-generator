package com.xiaoqu.git.log.extract.webapi.jira.board.epic;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.api.common.functions.RichMapFunction;

public class JiraEpicSinkMap extends RichMapFunction<JiraEpic, JiraEpic> {
    @Override
    public JiraEpic map(JiraEpic value) throws Exception {
        JiraEpicSink jiraEpicSink = new JiraEpicSink();
        jiraEpicSink.open(null);
        jiraEpicSink.invoke(value,null);
        jiraEpicSink.close();
        return value;
    }
}
