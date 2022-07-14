package com.xiaoqu.git.log.extract;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.type.TypeReference;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.net.URL;
import java.util.List;

public class WebApiSource extends RichSourceFunction<GitResponse> {
    @Override
    public void run(SourceContext<GitResponse> ctx) throws Exception {
        URL url = new URL("https://api.github.com/repos/SeaQL/sea-orm/commits");
        ObjectMapper objectMapper = new ObjectMapper();
        List<GitResponse> response =
                objectMapper.readerFor(new TypeReference<List<GitResponse>>() {})
                        .readValue(url);
        response.stream().parallel().forEach(ctx::collect);
    }

    @Override
    public void cancel() {

    }
}
