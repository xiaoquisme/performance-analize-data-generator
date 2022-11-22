package com.xiaoqu.git.log.extract.webapi.jira.worklog;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraWorkLogJob {
    public static void run() throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SystemConfig systemConfig = SystemConfigLoader.config;
        env.addSource(new JiraWorkLogSource(systemConfig.getDb()))
                .setParallelism(2)
                .flatMap(new JiraWorkLogFlow(systemConfig.getJira()))
                .setParallelism(4)
                .keyBy(item -> item.issueId)
                .addSink(new JiraWorkLogSink(systemConfig.getDb()));
        env.execute("sync jira to db");
    }
}
