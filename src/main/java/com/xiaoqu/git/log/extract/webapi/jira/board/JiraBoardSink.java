package com.xiaoqu.git.log.extract.webapi.jira.board;

import com.xiaoqu.git.log.extract.common.SinkBase;
import org.apache.flink.configuration.Configuration;

public class JiraBoardSink extends SinkBase<JiraBoardResponse.JiraBoard> {
    @Override
    public void open(Configuration parameters) throws Exception {
        String tableName = getTableName("jira_board");
        String sql = String.format("insert into %s(id,name, type)values(?,?,?) on duplicate key update id = id;", tableName);
        prepare(sql);
    }

    @Override
    public void invoke(JiraBoardResponse.JiraBoard value, Context context) throws Exception {
        preparedStatement.setString(1, value.id);
        preparedStatement.setString(2, value.name);
        preparedStatement.setString(3, value.type);
        preparedStatement.execute();
    }
}
