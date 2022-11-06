package com.xiaoqu.git.log.extract.webapi;

import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;

public class DataStreamWebApiJob {
    public static SystemConfig config;

    public static void main(String[] args) throws Exception {
        config = getSystemConfig();
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

    private static SystemConfig getSystemConfig() {
        Yaml yaml = new Yaml(new Constructor(SystemConfig.class));
        InputStream resourceAsStream = DataStreamWebApiJob.class.getClassLoader().getResourceAsStream("config.yaml");
        return yaml.load(resourceAsStream);
    }
}
