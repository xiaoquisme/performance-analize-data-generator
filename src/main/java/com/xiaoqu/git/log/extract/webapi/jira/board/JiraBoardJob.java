package com.xiaoqu.git.log.extract.webapi.jira.board;

import com.xiaoqu.git.log.extract.common.FlinkUtils;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import com.xiaoqu.git.log.extract.webapi.jira.board.epic.JiraEpicFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.epic.JiraEpicSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssueEpicFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssueSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.JiraIssueSprintFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.fixversions.JiraIssueFixVersionSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.lables.JiraIssueLabelSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.worklog.JiraWorkLogFlow;
import com.xiaoqu.git.log.extract.webapi.jira.board.issue.worklog.JiraWorkLogSink;
import com.xiaoqu.git.log.extract.webapi.jira.board.sprint.JIraSprintSinkMap;
import com.xiaoqu.git.log.extract.webapi.jira.board.sprint.JiraSprintFlow;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;

public class JiraBoardJob {

    public static void run() throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        List<SystemConfig.JiraConfig> jiraConfigs = config.jiras;

        final StreamExecutionEnvironment env = FlinkUtils.getRemoteEnvironment();
        for (SystemConfig.JiraConfig jiraConfig: jiraConfigs) {
            SingleOutputStreamOperator<JiraBoardResponse.JiraBoard> jiraBoardFlow = getJiraBoardFlow(jiraConfig, env);
            jiraSprintFlow(jiraConfig, jiraBoardFlow);
            jiraEpicFlow(jiraConfig, jiraBoardFlow);
        }

        env.execute("sync jira board to db");
    }

    private static void jiraEpicFlow(SystemConfig.JiraConfig jiraConfig, SingleOutputStreamOperator<JiraBoardResponse.JiraBoard> jiraBoardFlow) {
        jiraBoardFlow
                .keyBy(item -> item.id)
                .flatMap(new JiraEpicFlow(jiraConfig))
                .name("jira epic flat map flow")
                .keyBy(value -> value.boardId)
                .map(new JiraEpicSinkMap())
                .name("jira epic sink map")
                .flatMap(new JiraIssueEpicFlow(jiraConfig, "%s/rest/agile/1.0/board/%s/epic/%s/issue?startAt=%s&limit=50"))
                .name("jira issue flat map flow")
                .keyBy(item -> item.fields.epic.id)
                .map(new JiraIssueSinkMap())
                .name("jira issue sink map flow");
    }

    private static void jiraSprintFlow(SystemConfig.JiraConfig jiraConfig, SingleOutputStreamOperator<JiraBoardResponse.JiraBoard> jiraBoardFlow) {
        jiraBoardFlow
                .keyBy(item -> item.id)
                .flatMap(new JiraSprintFlow(jiraConfig))
                .name("flat map jira sprint")
                .keyBy(item -> item.id)
                .map(new JIraSprintSinkMap())
                .name("sink jira sprint")
                .keyBy(item -> item.id)
                .flatMap(new JiraIssueSprintFlow(jiraConfig, "%s/rest/agile/1.0/board/%s/sprint/%s/issue?startAt=%s&limit=50"))
                .name("get jira issue from board")
                .keyBy(item -> item.id)
                .map(new JiraIssueSinkMap())
                .name("sink jira issue")
                .keyBy(item -> item.id)
                .map(new JiraIssueFixVersionSinkMap())
                .name("jira issue fix version sink map")
                .keyBy(item -> item.id)
                .map(new JiraIssueLabelSinkMap())
                .name("jira issue label sink map")
                .keyBy(item -> item.id)
                .flatMap(new JiraWorkLogFlow(jiraConfig))
                .name("jira work log flat map flow")
                .keyBy(item -> item.id)
                .addSink(new JiraWorkLogSink())
                .name("jira work log sink flow");
    }

    private static SingleOutputStreamOperator<JiraBoardResponse.JiraBoard> getJiraBoardFlow(SystemConfig.JiraConfig jiraConfig, StreamExecutionEnvironment env) {
        return env
                .setParallelism(10)
                .addSource(new JiraBoardSource(jiraConfig))
                .name("read data from remote")
                .keyBy(item -> item.id)
                .map(new JiraBoardSinkMap())
                .name("sink to db map");
    }
}
