package com.xiaoqu.git.log.extract;

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
                '}';
    }
}
