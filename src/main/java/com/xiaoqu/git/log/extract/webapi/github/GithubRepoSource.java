package com.xiaoqu.git.log.extract.webapi.github;


import com.xiaoqu.git.log.extract.common.RequestUtils;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.type.TypeReference;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.List;

public class GithubRepoSource extends RichFlatMapFunction<String, GitResponseContext> {
    private final StringBuilder path = new StringBuilder();
    private final SystemConfig.GithubConfig githubConfig;
    private String repoOwner;

    public GithubRepoSource(SystemConfig.GithubConfig githubConfig, String since) {
        this.githubConfig = githubConfig;
        String name = githubConfig.getOwner().getName();
        if (since == null) {
            path.append(githubConfig.getBasePath())
                    .append("/repos")
                    .append("/" + name)
                    .append("/%s")
                    .append("/commits")
                    .append("?perPage=100")
                    .append("&page=%s");
        } else {
            path.append(githubConfig.getBasePath())
                    .append("/repos")
                    .append("/" + name)
                    .append("/%s")
                    .append("/commits")
                    .append("?perPage=100")
                    .append("&page=%s")
                    .append("&since=" + since);
        }
    }
    @Override
    public void flatMap(String repo, Collector<GitResponseContext> out) throws Exception {
        int pageCounter = 1;
        while (true) {
            String newPath = String.format(path.toString(), repo, pageCounter++);
            List<GitResponseContext> response = getGitRepoReponse(newPath, SystemConfigLoader.config.getGithub().getToken());
            if (response.isEmpty()) {
                break;
            }
            response.stream().map(item -> {
                item.repoName = repo;
                item.repoOwner = repoOwner;
                return item;
            }).forEach(out::collect);
        }
    }

    private List<GitResponseContext> getGitRepoReponse(String path, String token) throws IOException {
        return RequestUtils.sendRequestBearer(path, token, new TypeReference<List<GitResponseContext>>() {});
    }
}


