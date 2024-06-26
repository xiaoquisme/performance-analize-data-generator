package com.xiaoqu.git.log.extract.common;

import com.zaxxer.hikari.HikariConfig;

import java.io.Serializable;
import java.util.List;

public class SystemConfig {
    public GithubConfig github;
    public List<JiraConfig> jiras;
    public HikariConfig db;

    public static class DatabaseConfig implements Serializable {
        private static final long serialVersionUID = -1082413991501913743L;
        public String url;
        public String username;
        public String password;
        public String driver;
    }

    public static class GithubConfig implements Serializable {
        private static final long serialVersionUID = 3752097280889199314L;
        public String token;
        public String basePath;
        public GithubOwner owner;

        public static class GithubOwner implements Serializable {
            private static final long serialVersionUID = 4457842271792961820L;
            public String name;
            public List<String> repos;
        }
    }

    public static class JiraConfig implements Serializable {
        private static final long serialVersionUID = 1767925853714527170L;
        public String username;
        public String token;
        public String url;
        public List<String> projects;
    }
}
