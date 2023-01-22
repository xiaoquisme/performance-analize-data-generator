package com.xiaoqu.git.log.extract.webapi.jira.board.epic;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraEpicJob {

    public static void run() throws Exception {
        SystemConfig config = SystemConfigLoader.config;
//        SystemConfig.JiraConfig jiraConfig = config.jiras;

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<JiraBoardDb> stringDataStreamSource = env.addSource(new JiraEpicSource(config.db));
//        stringDataStreamSource
//                .flatMap(new JiraEpicFlow(jiraConfig))
//                .keyBy(value -> value.boardId)
//                .addSink(new JiraEpicSink());

        env.execute("sync jira epic to db");
    }
}
