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

public class WebApiSource extends RichSourceFunction<GitResponseContext> {
    private final StringBuilder path = new StringBuilder();
    private static final String BASE_PATH = "https://api.github.com/repos";
    private String repoOwner;
    private List<String> repos;

    public WebApiSource(String repoOwner, List<String> repos, String since) {
        this.repoOwner = repoOwner;
        this.repos = repos;
        if (since == null) {
            path.append(BASE_PATH)
                    .append("/" + repoOwner)
                    .append("/%s")
                    .append("/commits")
                    .append("?perPage=100")
                    .append("&page=%s");
        } else {
            path.append(BASE_PATH)
                    .append("/" + repoOwner)
                    .append("/%s")
                    .append("/commits")
                    .append("?perPage=100")
                    .append("&page=%s")
                    .append("&since=" + since);
        }

    }
    @Override
    public void run(SourceContext<GitResponseContext> ctx) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<GitResponseContext>>() {});
        int pageCounter = 1;
        for (String repo : repos) {
            while (true) {
                String newPath = String.format(path.toString(), repo, pageCounter++);
                InputStream inputStream = sendRequest(newPath);
                List<GitResponseContext> response = objectReader.readValue(inputStream);
                if (response.isEmpty()) {
                    break;
                }
                response.stream().map(item -> {
                    item.repoName = repo;
                    item.repoOwner = repoOwner;
                    return item;
                }).forEach(ctx::collect);
            }
        }

    }

    private InputStream sendRequest(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
        String token = DataStreamWebApiJob.config.getGithub().getToken();
        myURLConnection.setRequestProperty("Authorization", "Bearer " + token);
        myURLConnection.setRequestMethod("GET");
        return myURLConnection.getInputStream();
    }

    @Override
    public void cancel() {

    }
}
