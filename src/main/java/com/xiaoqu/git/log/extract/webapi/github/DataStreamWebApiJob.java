package com.xiaoqu.git.log.extract.webapi.github;

import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;

public class DataStreamWebApiJob {

    public static void run() throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        String repoOwner = config.getGithub().getOwner().getName();
        List<String> repos = config.getGithub().getOwner().getRepos();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new WebApiSource(repoOwner, repos, null))
                .map(new WebApiMapper())
                .map(new ProjectMapper())
                .keyBy(CommitLog::getRepoName)
                .addSink(new MysqlSink(config.getDb()));

        env.execute("Sync github log to DB");
    }
}
