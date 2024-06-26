package com.xiaoqu.git.log.extract.webapi.github;


import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SinkBase;
import org.apache.flink.configuration.Configuration;

public class MysqlSink extends SinkBase<CommitLog> {
    @Override
    public void open(Configuration parameters) throws Exception {
        String sql = "insert into git_log(id,repo_owner,project,jira_no, message, author, commit_date,commit_email)values(?,?,?,?,?,?,?,?) on duplicate key update id = id;";
        prepare(sql);
    }

    @Override
    public void invoke(CommitLog value, Context context) throws Exception {
        try {
            preparedStatement.setString(1, value.getCommitId());
            preparedStatement.setString(2, value.getRepoOwner());
            preparedStatement.setString(3, value.getRepoName());
            preparedStatement.setString(4, value.getJiraNo());
            preparedStatement.setString(5, value.getMessage());
            preparedStatement.setString(6, value.getUserName());
            preparedStatement.setString(7, value.getCommitDate());
            preparedStatement.setString(8, value.getCommitEmail());
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e);
            System.out.printf("===============================================%s", value);
        }

    }
}
