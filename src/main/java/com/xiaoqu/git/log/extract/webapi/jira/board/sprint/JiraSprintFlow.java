package com.xiaoqu.git.log.extract.webapi.jira.board.sprint;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.webapi.jira.board.JiraBoardResponse;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;

import static com.xiaoqu.git.log.extract.common.RequestUtils.sendRequestBasic;

public class JiraSprintFlow extends RichFlatMapFunction<JiraBoardResponse.JiraBoard, JiraSprintResponse.JiraSprint> {
    private SystemConfig.JiraConfig jiraConfig;

    public JiraSprintFlow(SystemConfig.JiraConfig jiraConfig) {
        this.jiraConfig = jiraConfig;
    }

    @Override
    public void flatMap(JiraBoardResponse.JiraBoard value, Collector<JiraSprintResponse.JiraSprint> out) throws Exception {
        getJiraResponse(value.id)
                .items
                .stream()
                .map(item -> {
                    item.boardId = value.id;
                    return item;
                })
                .forEach(out::collect);
    }

    private JiraSprintResponse getJiraResponse(String boardId) throws IOException {
        String url = String.format("%s/rest/agile/1.0/board/%s/sprint", jiraConfig.url, boardId);
        return sendRequestBasic(url, jiraConfig.username, jiraConfig.token, JiraSprintResponse.class).orElse(new JiraSprintResponse());
    }
}
