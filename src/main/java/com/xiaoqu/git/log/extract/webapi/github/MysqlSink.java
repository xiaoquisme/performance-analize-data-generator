package com.xiaoqu.git.log.extract.webapi.github;


import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SinkBase;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.configuration.Configuration;

public class MysqlSink extends SinkBase<CommitLog> {
    private SystemConfig.DatabaseConfig dbConfig;

    public MysqlSink(SystemConfig.DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void open(Configuration parameters) throws Exception {

        String sql = "insert into git_log(id,repo_owner,project,jira_no, message, author, commit_date,commit_email)values(?,?,?,?,?,?,?,?) on duplicate key update id = id;";
        prepare(sql,dbConfig);
    }

    @Override
    public void invoke(CommitLog value, Context context) throws Exception {
        CommitLog value1 = value;
        try {
            preparedStatement.setString(1, value1.getCommitId());
            preparedStatement.setString(2, value1.getRepoOwner());
            preparedStatement.setString(3, value1.getRepoName());
            preparedStatement.setString(4, value1.getJiraNo());
            preparedStatement.setString(5, value1.getMessage());
            preparedStatement.setString(6, value1.getUserName());
            preparedStatement.setString(7, value1.getCommitDate());
            preparedStatement.setString(8, value1.getCommitEmail());
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e);
            System.out.printf("===============================================%s", value1);
        }

    }
}
