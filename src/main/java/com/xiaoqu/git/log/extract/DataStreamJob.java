package com.xiaoqu.git.log.extract;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.StringUtils;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataStreamJob {

    public static final RichMapFunction<String, Tuple2<CommitLog, String>> MY_MAPPER = new RichMapFunction<String, Tuple2<CommitLog, String>>() {
        @Override
        public Tuple2<CommitLog, String> map(String value) {
            value = value.replace("\"", "");
            final String regex = "(^.*)\\ \\[(.*)?\\]\\ ?#+(([n,N]/[A,a])|(.*?\\-[0-9]+))\\ ?(.*)";

            final Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS | Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(value);

            CommitLog commitLog = new CommitLog();
            while (matcher.find()) {
                commitLog.setCommitId(matcher.group(1));
                commitLog.setUserName(matcher.group(2));
                commitLog.setJiraNo(matcher.group(3));
                commitLog.setMessage(matcher.group(6));
            }
            return Tuple2.of(commitLog, value);
        }
    };

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        URL resource = DataStreamJob.class.getClassLoader().getResource("git_log_secret.txt");
        String filePath = resource.getPath();

        DataStreamSource<String> stringDataStreamSource = env.readTextFile(filePath);
        DataStream<Tuple2<CommitLog, String>> map = stringDataStreamSource
                .filter(s -> !StringUtils.isNullOrWhitespaceOnly(s))
                .map(String::trim)
                .map(MY_MAPPER);

        map.addSink(new SinkToMysql());

        env.execute("Flink Java API Skeleton");
    }
}
