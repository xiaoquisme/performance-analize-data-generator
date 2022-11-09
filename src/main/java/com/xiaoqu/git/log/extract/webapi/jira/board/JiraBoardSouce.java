package com.xiaoqu.git.log.extract.webapi.jira.board;


import com.xiaoqu.git.log.extract.common.RequestUtils;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.IOException;

public class JiraBoardSouce extends RichSourceFunction<JiraBoardResponse.JiraBoard> {
    private final SystemConfig.JiraConfig jiraConfig;
    public JiraBoardSouce(SystemConfig.JiraConfig jiraConfig) {
        this.jiraConfig = jiraConfig;
    }

    @Override
    public void run(SourceContext<JiraBoardResponse.JiraBoard> ctx) throws Exception {
        JiraBoardResponse jiraBoardResponse = getBoard();
        jiraBoardResponse.values.forEach(ctx::collect);
    }

    @Override
    public void cancel() {

    }

    private JiraBoardResponse getBoard() throws IOException {
        String path = jiraConfig.getUrl() + "/rest/agile/1.0/board";
        String username = jiraConfig.getUsername();
        String password = jiraConfig.getPassword();
        return RequestUtils.sendRequest(path, username, password, JiraBoardResponse.class);
    }
}
