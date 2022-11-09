package com.xiaoqu.git.log.extract.webapi.jira.worklog;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class WorkLogJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SystemConfig systemConfig = SystemConfigLoader.config;
        env.addSource(new JiraWorkLogSource(systemConfig.getDb()))
                .flatMap(new JiraWorkLogFlow(systemConfig.getJira()))
                .keyBy(item -> item.issueId)
                .addSink(new JiraWorkLogSink(systemConfig.getDb()))
                .setParallelism(10);
        env.execute("sync jira worklog to db");
    }
}
