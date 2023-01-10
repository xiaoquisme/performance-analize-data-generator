package com.xiaoqu.git.log.extract.webapi.jira.board.issue;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraIssueJob {
    public static void run() throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SystemConfig config = SystemConfigLoader.config;
        env.addSource(new JiraIssueSource(config.getDb()))
                .flatMap(new JiraIssueEpicFlow(config.getJira(), "%s/rest/agile/1.0/board/%s/epic/%s/issue?startAt=%s&limit=50"))
                .keyBy(item -> item.fields.epic.id)
                .addSink(new JiraIssueSink(config.getDb()));
        env.execute("sync jira issue to db");
    }
}
