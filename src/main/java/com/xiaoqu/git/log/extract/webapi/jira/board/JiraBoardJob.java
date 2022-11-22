package com.xiaoqu.git.log.extract.webapi.jira.board;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import com.xiaoqu.git.log.extract.webapi.jira.epic.JiraEpicFlow;
import com.xiaoqu.git.log.extract.webapi.jira.epic.JiraEpicSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.issue.JiraIssueFlow;
import com.xiaoqu.git.log.extract.webapi.jira.issue.JiraIssueSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.worklog.JiraWorkLogFlow;
import com.xiaoqu.git.log.extract.webapi.jira.worklog.JiraWorkLogSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraBoardJob {

    public static void run() throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        SystemConfig.JiraConfig jiraConfig = config.getJira();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new JiraBoardSouce(jiraConfig))
                .keyBy(item -> item.id)
                .map(new JiraBoardSinkMap())
                .setParallelism(4)
                .keyBy(item -> item.id)
                .flatMap(new JiraEpicFlow(jiraConfig))
                .setParallelism(4)
                .keyBy(value -> value.boardId)
                .map(new JiraEpicSinkMap())
                .setParallelism(4)
                .flatMap(new JiraIssueFlow(config.getJira()))
                .setParallelism(4)
                .keyBy(item -> item.fields.epic.id)
                .map(new JiraIssueSinkMap(config.getDb()))
                .setParallelism(4)
                .flatMap(new JiraWorkLogFlow(config.getJira()))
                .setParallelism(4)
                .keyBy(item -> item.issueId)
                .addSink(new JiraWorkLogSink(config.getDb()))
                .setParallelism(4);

        env.execute("sync jira board to db");
    }
}
