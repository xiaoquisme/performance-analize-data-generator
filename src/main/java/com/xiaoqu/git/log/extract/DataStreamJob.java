/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoqu.git.log.extract;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSinkHelper;
import org.apache.flink.util.StringUtils;

import java.net.URL;

public class DataStreamJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        URL resource = DataStreamJob.class.getClassLoader().getResource("git_log.txt");
        String filePath = resource.getPath();

        DataStreamSource<String> stringDataStreamSource = env.setParallelism(1).readTextFile(filePath);
        StreamingFileSink<String> fileSink = StreamingFileSink.forRowFormat(new Path("/Users/lqqu/learning/git_log_extract/src/main/resources"), new SimpleStringEncoder<String>()).build();
        SingleOutputStreamOperator<String> map = stringDataStreamSource.filter(s -> !StringUtils.isNullOrWhitespaceOnly(s))
                .map(String::trim);

        map.addSink(fileSink);

        env.execute("Flink Java API Skeleton");
    }
}
