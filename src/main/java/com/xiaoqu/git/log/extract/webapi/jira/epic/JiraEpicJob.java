package com.xiaoqu.git.log.extract.webapi.jira.epic;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraEpicJob {

    public static void main(String[] args) throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        SystemConfig.JiraConfig jiraConfig = config.getJira();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<JiraBoardDb> stringDataStreamSource = env.addSource(new JiraEpicSource(config.getDb()));
        stringDataStreamSource
                .flatMap(new JiraEpicFlow(jiraConfig))
                .keyBy(value -> value.boardId)
                .addSink(new JiraEpicSink())
                .setParallelism(10);

        env.execute("sync jira epic to db");
    }
}
