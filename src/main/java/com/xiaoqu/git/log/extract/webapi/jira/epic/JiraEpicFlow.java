package com.xiaoqu.git.log.extract.webapi.jira.epic;

import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JiraEpicFlow extends RichFlatMapFunction<JiraBoardDb, JiraEpic> {
    private SystemConfig.JiraConfig jiraConfig;
    private ObjectMapper objectMapper = new ObjectMapper();

    public JiraEpicFlow(SystemConfig.JiraConfig jiraConfig) {
        this.jiraConfig = jiraConfig;
    }


    @Override
    public void flatMap(JiraBoardDb value, Collector<JiraEpic> out) throws Exception {
        objectMapper.readValue(getJiraEpics(value.id), JiraEpicResponse.class)
                .values
                .stream()
                .map(item -> {
                    item.boardId = value.id;
                    return item;
                })
                .forEach(out::collect);
    }

    private InputStream getJiraEpics(String boardId) throws IOException {
        String url = String.format("%s/rest/agile/1.0/board/%s/epic", jiraConfig.getUrl(), boardId);
        return sendRequest(url);
    }

    private InputStream sendRequest(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
        String token = jiraConfig.getToken();
        myURLConnection.setRequestProperty("Authorization", "Basic " + token);
        myURLConnection.setRequestMethod("GET");
        return myURLConnection.getInputStream();
    }
}
