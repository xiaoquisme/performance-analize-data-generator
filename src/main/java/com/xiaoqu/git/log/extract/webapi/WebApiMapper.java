package com.xiaoqu.git.log.extract.webapi;

import com.xiaoqu.git.log.extract.CommitLog;
import com.xiaoqu.git.log.extract.GitResponse;
import org.apache.flink.api.common.functions.RichMapFunction;

public class WebApiMapper extends RichMapFunction<GitResponse, CommitLog> {
    @Override
    public CommitLog map(GitResponse value) {
        GitResponse.Commit commit = value.commit;
        CommitLog commitLog = new CommitLog();
        commitLog.setCommitId(value.sha);
        commitLog.setMessage(commit.message);
        commitLog.setUserName(commit.committer.name);
        commitLog.setCommitDate(commit.committer.date);
        commitLog.setCommitEmail(commit.committer.email);
        return commitLog;
    }
}
