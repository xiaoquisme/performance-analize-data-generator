package com.xiaoqu.git.log.extract;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectMapper extends RichMapFunction<CommitLog, CommitLog> {

    @Override
    public CommitLog map(CommitLog input) {
        String value = input.getMessage().replace("\"", "");
        final String regex = "\\[(.*?)\\]\\ ?#{1}(([n,N]/[a,A])|(.*?\\-[0-9]+))\\ ?(.*)?";

        final Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS | Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(value);

        while (matcher.find()) {
            input.setUserName(matcher.group(1));
            input.setJiraNo(matcher.group(2));
            input.setMessage(matcher.group(5));
        }
        return input;

    }

}
