package com.xiaoqu.git.log.extract.common;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestUtils {
    public static <T> T sendRequest(String path, String token, Class<T> tClass) throws IOException {
        URL url = new URL(path);
        HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
        myURLConnection.setRequestProperty("Authorization", "Basic " + token);
        myURLConnection.setRequestMethod("GET");
        InputStream inputStream = myURLConnection.getInputStream();
        return new ObjectMapper().readValue(inputStream, tClass);
    }

    public static <T> T sendRequest(String path, String userName, String password, Class<T> tClass) throws IOException {
        URL url = new URL(path);
        HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
        String encode = Base64.getEncoder().encodeToString(String.format("%s:%s", userName, password).getBytes(StandardCharsets.UTF_8));
        myURLConnection.setRequestProperty("Authorization", "Basic " + encode);
        myURLConnection.setRequestMethod("GET");
        InputStream inputStream = myURLConnection.getInputStream();
        return new ObjectMapper().readValue(inputStream, tClass);
    }
}