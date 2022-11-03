package com.xiaoqu.git.log.extract.webapi;

`
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.type.TypeReference;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectReader;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WebApiSource extends RichSourceFunction<GitResponse> {
    @Override
    public void run(SourceContext<GitResponse> ctx) throws Exception {
        String path = "https://api.github.com/repos/SeaQL/sea-orm/commits?page=%s&per_page=100";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<GitResponse>>() {});
        int pageCounter = 1;
        while (true) {
            String newPath = String.format(path, pageCounter++);
            InputStream inputStream = sendRequest(newPath);
            List<GitResponse> response = objectReader.readValue(inputStream);
            if (response.isEmpty()) {
                break;
            }
            response.forEach(ctx::collect);
        }
    }

    private InputStream sendRequest(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
        myURLConnection.setRequestProperty("Authorization", "Bearer <TOKEN>");
        myURLConnection.setRequestMethod("GET");
        return myURLConnection.getInputStream();
    }

    @Override
    public void cancel() {

    }
}
