package com.xiaoqu.git.log.extract.webapi.github;

import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import static com.xiaoqu.git.log.extract.common.FlinkUtils.getRemoteEnvironment;

public class GithubWebApiJob {

    public static void run() throws Exception {
        SystemConfig config = SystemConfigLoader.config;

        final StreamExecutionEnvironment env = getRemoteEnvironment();
        env.addSource(new GithubOrgSource(config.github))
                .name("github repo source")
                .keyBy(item -> item)
                .flatMap(new GithubRepoSource(config.github, null))
                .name("github repo flatmap flow ")
                .keyBy(item -> item.repoName)
                .map(new WebApiMapper())
                .name("github response mapper")
                .keyBy(CommitLog::getRepoName)
                .map(new ProjectMapper())
                .name("github project mapper")
                .keyBy(CommitLog::getRepoName)
                .addSink(new MysqlSink(config.db))
                .name("github mysql flow");
        env.execute("Sync github log to DB");
    }
}
