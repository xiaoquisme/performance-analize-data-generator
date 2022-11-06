package com.xiaoqu.git.log.extract.webapi.jira.board;


import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JiraBoardSouce extends RichSourceFunction<JiraBoardResponse.JiraBoard> {
    private final SystemConfig.JiraConfig jiraConfig;
    public JiraBoardSouce(SystemConfig.JiraConfig jiraConfig) {
        this.jiraConfig = jiraConfig;
    }

    @Override
    public void run(SourceContext<JiraBoardResponse.JiraBoard> ctx) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        JiraBoardResponse jiraBoardResponse = objectMapper.readValue(getBoard(), JiraBoardResponse.class);
        jiraBoardResponse.values.forEach(ctx::collect);
    }

    @Override
    public void cancel() {

    }

    private InputStream getBoard() throws IOException {
        String url = jiraConfig.getUrl() + "/rest/agile/1.0/board";
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
