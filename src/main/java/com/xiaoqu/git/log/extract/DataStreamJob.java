package com.xiaoqu.git.log.extract;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.StringUtils;

import java.net.URL;

public class DataStreamJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        URL resource = DataStreamJob.class.getClassLoader().getResource("git_log_secret.txt");
        String filePath = resource.getPath();

        DataStreamSource<String> stringDataStreamSource = env.readTextFile(filePath);
        DataStream<Tuple2<CommitLog, String>> map = stringDataStreamSource
                .filter(s -> !StringUtils.isNullOrWhitespaceOnly(s))
                .map(String::trim)
                .map(new Mapper());

        map.addSink(new SinkToMysql());

        env.execute("Flink Java API Skeleton");
    }
}
