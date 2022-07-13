package com.xiaoqu.git.log.extract;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapper extends RichMapFunction<String, Tuple2<CommitLog, String>> {
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
}
