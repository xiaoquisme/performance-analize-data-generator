package com.xiaoqu.git.log.extract.webapi.jira.board;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraBoardJob {

    public static void run() throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        SystemConfig.JiraConfig jiraConfig = config.getJira();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new JiraBoardSouce(jiraConfig))
                .addSink(new JiraBoardSink())
                .setParallelism(10);

        env.execute("sync jira board to db");
    }
}
