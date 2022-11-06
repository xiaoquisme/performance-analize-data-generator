package com.xiaoqu.git.log.extract.webapi.jira.board;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraBoardJob {

    public static void main(String[] args) throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        SystemConfig.JiraConfig jiraConfig = config.getJira();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<JiraBoardResponse.JiraBoard> stringDataStreamSource = env.addSource(new JiraBoardSouce(jiraConfig));
        stringDataStreamSource
                .addSink(new JiraBoardSink());

        env.execute("sync jira board to db");
    }
}
