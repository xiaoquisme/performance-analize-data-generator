package com.xiaoqu.git.log.extract.common;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.type.TypeReference;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class RequestUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static <T> T sendRequestBasic(String path, String token, Class<T> tClass) {
        try {
            URL url = new URL(path);
            HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
            myURLConnection.setRequestProperty("Authorization", "Basic " + token);
            myURLConnection.setRequestMethod("GET");
            InputStream inputStream = myURLConnection.getInputStream();
            return new ObjectMapper().readValue(inputStream, tClass);
        } catch (Exception e) {
            logger.error("send request:{}, error.", path, e);
            return null;
        }
    }

    public static <T> T sendRequestBearer(String path, String token, TypeReference<T> typeReference) throws IOException {
        URL url = new URL(path);
        HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
        myURLConnection.setRequestProperty("Authorization", "Bearer " + token);
        myURLConnection.setRequestMethod("GET");
        return new ObjectMapper().readValue(myURLConnection.getInputStream(), typeReference);
    }


    public static <T> Optional<T> sendRequestBasic(String path, String userName, String password, Class<T> tClass) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
            String encode = Base64.getEncoder().encodeToString(String.format("%s:%s", userName, password).getBytes(StandardCharsets.UTF_8));
            myURLConnection.setRequestProperty("Authorization", "Basic " + encode);
            myURLConnection.setRequestMethod("GET");
            InputStream inputStream = myURLConnection.getInputStream();
            return Optional.of(new ObjectMapper().readValue(inputStream, tClass));
        } catch (Exception e) {
            logger.error("send request:{}", path, e);
            return Optional.empty();
        }

    }
}
