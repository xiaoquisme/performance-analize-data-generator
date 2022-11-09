package com.xiaoqu.git.log.extract.webapi.jira.epic;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.xiaoqu.git.log.extract.common.RequestUtils.sendRequest;

public class JiraEpicFlow extends RichFlatMapFunction<JiraBoardDb, JiraEpic> {
    private SystemConfig.JiraConfig jiraConfig;
    private ObjectMapper objectMapper = new ObjectMapper();

    public JiraEpicFlow(SystemConfig.JiraConfig jiraConfig) {
        this.jiraConfig = jiraConfig;
    }


    @Override
    public void flatMap(JiraBoardDb value, Collector<JiraEpic> out) throws Exception {
        getJiraEpics(value.id)
                .values
                .stream()
                .map(item -> {
                    item.boardId = value.id;
                    return item;
                })
                .forEach(out::collect);
    }

    private JiraEpicResponse getJiraEpics(String boardId) throws IOException {
        String url = String.format("%s/rest/agile/1.0/board/%s/epic", jiraConfig.getUrl(), boardId);
        return sendRequest(url, jiraConfig.getUsername(), jiraConfig.getPassword(), JiraEpicResponse.class);
    }
}
