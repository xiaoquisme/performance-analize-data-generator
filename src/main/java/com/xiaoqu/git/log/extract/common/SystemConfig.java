package com.xiaoqu.git.log.extract.common;

import java.io.Serializable;
import java.util.List;

public class SystemConfig {
    public DatabaseConfig getDb() {
        return db;
    }

    public void setDb(DatabaseConfig db) {
        this.db = db;
    }

    public GithubConfig getGithub() {
        return github;
    }

    public void setGithub(GithubConfig github) {
        this.github = github;
    }

    private DatabaseConfig  db;
    private GithubConfig github;

    public JiraConfig getJira() {
        return jira;
    }

    public void setJira(JiraConfig jira) {
        this.jira = jira;
    }

    private JiraConfig jira;

    public static class DatabaseConfig implements Serializable {
        private static final long serialVersionUID = -1082413991501913743L;
        private String url;
        private String username;
        private String password;
        private String driver;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }
    }

    public static class GithubConfig implements Serializable {
        private static final long serialVersionUID = 3752097280889199314L;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public GithubOwner getOwner() {
            return owner;
        }

        public void setOwner(GithubOwner owner) {
            this.owner = owner;
        }

        private String token;

        private String basePath;
        private GithubOwner owner;

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }

        public static class GithubOwner implements Serializable {

            private static final long serialVersionUID = 4457842271792961820L;
            private String name;
            private List<String> repos;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getRepos() {
                return repos;
            }

            public void setRepos(List<String> repos) {
                this.repos = repos;
            }
        }
    }

    public static class JiraConfig implements Serializable {
        private static final long serialVersionUID = 1767925853714527170L;
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        private String password;
        private String url;
        private List<String> projects;
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getProjects() {
            return projects;
        }

        public void setProjects(List<String> projects) {
            this.projects = projects;
        }
    }
}
