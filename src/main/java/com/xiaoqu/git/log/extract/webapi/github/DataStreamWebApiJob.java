package com.xiaoqu.git.log.extract.webapi.github;

import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;

public class DataStreamWebApiJob {

    public static void main(String[] args) throws Exception {
        SystemConfig config = SystemConfigLoader.config;
        String repoOwner = config.getGithub().getOwner().getName();
        List<String> repos = config.getGithub().getOwner().getRepos();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<GitResponseContext> stringDataStreamSource = env.addSource(new WebApiSource(repoOwner, repos, null));
        DataStream<CommitLog> map = stringDataStreamSource
                .map(new WebApiMapper())
                .map(new ProjectMapper());

        map.addSink(new MysqlSink());

        env.execute("Sync log to DB");
    }
}
