package com.xiaoqu.git.log.extract.webapi;


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
    private StringBuilder path = new StringBuilder();
    private static final String BASE_PATH = "https://api.github.com/repos";
    public WebApiSource(String repoOwner, String repo, String since) {
        if(since == null) {
            path.append(BASE_PATH)
                    .append("/" + repoOwner)
                    .append("/" + repo)
                    .append("/commits")
                    .append("?perPage=100")
                    .append("&page=%s");
        } else  {
            path.append(BASE_PATH)
                    .append("/" + repoOwner)
                    .append("/" + repo)
                    .append("/commits")
                    .append("?perPage=100")
                    .append("&page=%s")
                    .append("&since=" + since);
        }

    }
    @Override
    public void run(SourceContext<GitResponse> ctx) throws Exception {
        String path = "https://api.github.com/repos/SeaQL/sea-orm/commits?page=%s&per_page=100";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<GitResponse>>() {});
        int pageCounter = 1;
        while (true) {
            String newPath = String.format(path.toString(), pageCounter++);
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
