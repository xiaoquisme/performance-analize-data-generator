package com.xiaoqu.git.log.extract.webapi.github;

import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;

public class GithubWebApiJob {

    public static void run() throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        String repoOwner = config.getGithub().getOwner().getName();
        List<String> repos = config.getGithub().getOwner().getRepos();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new WebApiSource(repoOwner, repos, null))
                .name("github source")
                .keyBy(item -> item.repoOwner)
                .map(new WebApiMapper())
                .name("github response mapper")
                .setParallelism(10)
                .keyBy(CommitLog::getRepoName)
                .map(new ProjectMapper())
                .name("github project mapper")
                .setParallelism(10)
                .keyBy(CommitLog::getRepoName)
                .addSink(new MysqlSink(config.getDb()))
                .name("github mysql flow")
                .setParallelism(10);

        env.execute("Sync github log to DB");
    }
}