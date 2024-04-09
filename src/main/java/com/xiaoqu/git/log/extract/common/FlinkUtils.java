package com.xiaoqu.git.log.extract.common;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkUtils {
    public static StreamExecutionEnvironment getRemoteEnvironment() {
        return StreamExecutionEnvironment.getExecutionEnvironment();
    }
}
