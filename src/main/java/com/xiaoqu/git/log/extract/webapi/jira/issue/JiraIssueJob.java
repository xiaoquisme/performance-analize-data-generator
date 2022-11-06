package com.xiaoqu.git.log.extract.webapi.jira.issue;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraIssueJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SystemConfig config = SystemConfigLoader.config;
        env.addSource(new JiraIssueSource(config.getDb()))
                .flatMap(new JiraIssueFlow(config.getJira()))
                .keyBy(item -> item.fields.epic.id)
                .addSink(new JiraIssueSink(config.getDb()))
                .setParallelism(10);
        env.execute("sync jira issue to db");
    }
}
