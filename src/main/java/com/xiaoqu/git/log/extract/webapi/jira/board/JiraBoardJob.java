package com.xiaoqu.git.log.extract.webapi.jira.board;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import com.xiaoqu.git.log.extract.webapi.jira.board.epic.JiraEpicFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.epic.JiraEpicSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssueSprintFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssueEpicFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssueSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.board.sprint.JIraSprintSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.board.sprint.JiraSprintFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.worklog.JiraWorkLogFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.worklog.JiraWorkLogSink;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JiraBoardJob {

    public static void run() throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        SystemConfig.DatabaseConfig dbConfig = config.getDb();
        SystemConfig.JiraConfig jiraConfig = config.getJira();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        SingleOutputStreamOperator<JiraBoardResponse.JiraBoard> jiraBoardFlow = getJiraBoardFlow(jiraConfig, env, dbConfig);

        jiraSprintFlow(config, dbConfig, jiraConfig, jiraBoardFlow);

        jiraEpicFlow(config, dbConfig, jiraConfig, jiraBoardFlow);

        env.execute("sync jira board to db");
    }

    private static void jiraEpicFlow(SystemConfig config, SystemConfig.DatabaseConfig dbConfig, SystemConfig.JiraConfig jiraConfig, SingleOutputStreamOperator<JiraBoardResponse.JiraBoard> jiraBoardFlow) {
        jiraBoardFlow
                .setParallelism(4)
                .keyBy(item -> item.id)
                .flatMap(new JiraEpicFlow(jiraConfig))
                .name("jira epic flat map flow")
                .setParallelism(4)
                .keyBy(value -> value.boardId)
                .map(new JiraEpicSinkMap(dbConfig))
                .name("jira epic sink map")
                .setParallelism(5)
                .flatMap(new JiraIssueEpicFlow(config.getJira(), "%s/rest/agile/1.0/board/%s/epic/%s/issue?startAt=%s&limit=50"))
                .name("jira issue flat map flow")
                .setParallelism(10)
                .keyBy(item -> item.fields.epic.id)
                .map(new JiraIssueSinkMap(dbConfig))
                .name("jira issue sink map flow")
                .setParallelism(10);
    }

    private static void jiraSprintFlow(SystemConfig config, SystemConfig.DatabaseConfig dbConfig, SystemConfig.JiraConfig jiraConfig, SingleOutputStreamOperator<JiraBoardResponse.JiraBoard> jiraBoardFlow) {
        jiraBoardFlow
                .setParallelism(10)
                .keyBy(item -> item.id)
                .flatMap(new JiraSprintFlow(jiraConfig))
                .name("flat map jira sprint")
                .setParallelism(10)
                .keyBy(item -> item.id)
                .map(new JIraSprintSinkMap(dbConfig))
                .name("sink jira sprint")
                .setParallelism(10)
                .keyBy(item -> item.id)
                .flatMap(new JiraIssueSprintFlow(jiraConfig, "%s/rest/agile/1.0/board/%s/sprint/%s/issue?startAt=%s&limit=50"))
                .name("get jira issue from board")
                .setParallelism(20)
                .keyBy(item -> item.id)
                .map(new JiraIssueSinkMap(dbConfig))
                .name("sink jira issue")
                .setParallelism(20)
                .keyBy(item -> item.id)
                .flatMap(new JiraWorkLogFlow(config.getJira()))
                .name("jira work log flat map flow")
                .setParallelism(20)
                .keyBy(item -> item.id)
                .addSink(new JiraWorkLogSink(dbConfig))
                .name("jira work log sink flow")
                .setParallelism(20);
    }

    private static SingleOutputStreamOperator<JiraBoardResponse.JiraBoard> getJiraBoardFlow(SystemConfig.JiraConfig jiraConfig, StreamExecutionEnvironment env, SystemConfig.DatabaseConfig dbConfig) {
        return env.addSource(new JiraBoardSouce(jiraConfig))
                .name("read data from remote")
                .keyBy(item -> item.id)
                .map(new JiraBoardSinkMap(dbConfig))
                .name("sink to db map");
    }
}
