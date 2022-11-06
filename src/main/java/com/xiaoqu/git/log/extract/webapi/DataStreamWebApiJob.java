package com.xiaoqu.git.log.extract.webapi;

import com.xiaoqu.git.log.extract.common.CommitLog;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class DataStreamWebApiJob {
    public static SystemConfig config;

    public static void main(String[] args) throws Exception {
        config = getSystemConfig();
        String repo = config.getGithub().getOwner().getRepos().get(0);
        String repoOwner = config.getGithub().getOwner().getName();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<GitResponse> stringDataStreamSource = env.addSource(new WebApiSource(repoOwner, repo, null));
        DataStream<CommitLog> map = stringDataStreamSource
                .map(new WebApiMapper())
                .map(new ProjectMapper());

        map.addSink(new SinkToMysqlForWebApi(repo));

        env.execute("Sync log to DB");
    }

    private static SystemConfig getSystemConfig() {
        Yaml yaml = new Yaml(new Constructor(SystemConfig.class));
        InputStream resourceAsStream = DataStreamWebApiJob.class.getClassLoader().getResourceAsStream("config.yaml");
        return yaml.load(resourceAsStream);
    }
}
