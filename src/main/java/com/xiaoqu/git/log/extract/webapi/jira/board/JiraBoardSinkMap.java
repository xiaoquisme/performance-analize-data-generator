package com.xiaoqu.git.log.extract.webapi.jira.board;

import org.apache.flink.api.common.functions.RichMapFunction;

public class JiraBoardSinkMap extends RichMapFunction<JiraBoardResponse.JiraBoard, JiraBoardResponse.JiraBoard> {
    @Override
    public JiraBoardResponse.JiraBoard map(JiraBoardResponse.JiraBoard value) throws Exception {
        JiraBoardSink jiraBoardSink = new JiraBoardSink();
        jiraBoardSink.open(null);
        jiraBoardSink.invoke(value, null);
        jiraBoardSink.close();
        return value;
    }
}
