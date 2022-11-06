package com.xiaoqu.git.log.extract.webapi;

import com.xiaoqu.git.log.extract.common.CommitLog;
import org.apache.flink.api.common.functions.RichMapFunction;

public class WebApiMapper extends RichMapFunction<GitResponseContext, CommitLog> {
    @Override
    public CommitLog map(GitResponseContext value) {
        GitResponseContext.Commit commit = value.commit;
        CommitLog commitLog = new CommitLog();
        commitLog.setRepoOwner(value.repoOwner);
        commitLog.setRepoName(value.repoName);
        commitLog.setCommitId(value.sha);
        commitLog.setMessage(commit.message);
        commitLog.setUserName(commit.committer.name);
        commitLog.setCommitDate(commit.committer.date);
        commitLog.setCommitEmail(commit.committer.email);
        return commitLog;
    }
}
