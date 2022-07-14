package com.xiaoqu.git.log.extract;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DataStreamWebApiJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<GitResponse> stringDataStreamSource = env.addSource(new WebApiSource());
        DataStream<CommitLog> map = stringDataStreamSource
                .map(new WebApiMapper())
                .map(new ProjectMapper());

        map.addSink(new SinkToMysqlForWebApi());

        env.execute("Flink Java API Skeleton");
    }
}
