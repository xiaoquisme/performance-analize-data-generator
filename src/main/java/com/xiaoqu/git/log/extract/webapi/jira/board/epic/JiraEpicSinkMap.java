package com.xiaoqu.git.log.extract.webapi.jira.board.epic;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.api.common.functions.RichMapFunction;

public class JiraEpicSinkMap extends RichMapFunction<JiraEpic, JiraEpic> {
    private final SystemConfig.DatabaseConfig dbConfig;

    public JiraEpicSinkMap(SystemConfig.DatabaseConfig dbConfig) {this.dbConfig = dbConfig;}

    @Override
    public JiraEpic map(JiraEpic value) throws Exception {
        JiraEpicSink jiraEpicSink = new JiraEpicSink(this.dbConfig);
        jiraEpicSink.open(null);
        jiraEpicSink.invoke(value,null);
        jiraEpicSink.close();
        return value;
    }
}
