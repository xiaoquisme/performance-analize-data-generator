package com.xiaoqu.git.log.extract.webapi.jira.board;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.api.common.functions.RichMapFunction;

public class JiraBoardSinkMap extends RichMapFunction<JiraBoardResponse.JiraBoard, JiraBoardResponse.JiraBoard> {

    private final SystemConfig.DatabaseConfig dbConfig;

    public JiraBoardSinkMap(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public JiraBoardResponse.JiraBoard map(JiraBoardResponse.JiraBoard value) throws Exception {
        JiraBoardSink jiraBoardSink = new JiraBoardSink(this.dbConfig);
        jiraBoardSink.open(null);
        jiraBoardSink.invoke(value, null);
        jiraBoardSink.close();
        return value;
    }
}
