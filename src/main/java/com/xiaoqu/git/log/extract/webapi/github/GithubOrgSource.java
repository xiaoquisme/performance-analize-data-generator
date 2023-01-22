package com.xiaoqu.git.log.extract.webapi.github;

import com.xiaoqu.git.log.extract.common.RequestUtils;
import com.xiaoqu.git.log.extract.common.SystemConfig;
import com.xiaoqu.git.log.extract.common.SystemConfigLoader;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.type.TypeReference;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GithubOrgSource extends RichSourceFunction<String> {

    private final SystemConfig.GithubConfig githubConfig;

    public GithubOrgSource(SystemConfig.GithubConfig githubConfig) {
        this.githubConfig = githubConfig;
    }


    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        getGithubRepos()
                .forEach(ctx::collect);
    }

    private List<String> getGithubRepos() throws IOException {
        List<String> repos = this.githubConfig.owner.repos;
        if (Objects.isNull(repos) || repos.isEmpty()) {
            List<GithubRepo> repoList = getRepos(githubConfig.owner.name);
            repos = repoList.stream().map(item -> item.name).collect(Collectors.toList());
        }
        return repos;
    }

    private List<GithubRepo> getRepos(String orgName) throws IOException {
        String url = String.format(githubConfig.basePath + "/orgs/%s/repos?per_page=100", orgName);
        return RequestUtils.sendRequestBearer(url, SystemConfigLoader.config.github.token, new TypeReference<List<GithubRepo>>() {});
    }

    @Override
    public void cancel() {

    }
}
