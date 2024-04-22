package com.xiaoqu.git.log.extract.webapi.jira.board.epic;


import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.xiaoqu.git.log.extract.common.MysqlThreadPool.getConnection;

public class JiraEpicSource extends RichSourceFunction<JiraBoardDb> {

    @Override
    public void run(SourceContext<JiraBoardDb> ctx) throws Exception {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            try(ResultSet resultSet = statement.executeQuery("select id, name, type from jira_board")) {
                while (resultSet.next()) {
                    JiraBoardDb jiraBoardDb = new JiraBoardDb();
                    jiraBoardDb.id = resultSet.getString(1);
                    jiraBoardDb.name = resultSet.getString(2);
                    jiraBoardDb.type = resultSet.getString(3);
                    ctx.collect(jiraBoardDb);
                }
            }
        }

    }

    @Override
    public void cancel() {

    }
}
