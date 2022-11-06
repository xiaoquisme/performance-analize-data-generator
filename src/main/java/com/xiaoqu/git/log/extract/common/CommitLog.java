package com.xiaoqu.git.log.extract.common;

public class CommitLog {
    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    private String commitId;
    private String jiraNo;
    private String userName;
    private String message;
    private String commitDate;
    private String repoOwner;

    public String getRepoOwner() {
        return repoOwner;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    private String repoName;

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getCommitEmail() {
        return commitEmail;
    }

    public void setCommitEmail(String commitEmail) {
        this.commitEmail = commitEmail;
    }

    private String commitEmail;


    public String getJiraNo() {
        return jiraNo;
    }

    public void setJiraNo(String jiraNo) {
        this.jiraNo = jiraNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CommitLog{" +
                "commitId='" + commitId + '\'' +
                ", jiraNo='" + jiraNo + '\'' +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", commitDate='" + commitDate + '\'' +
                ", repoOwner='" + repoOwner + '\'' +
                ", repoName='" + repoName + '\'' +
                ", commitEmail='" + commitEmail + '\'' +
                '}';
    }
}
