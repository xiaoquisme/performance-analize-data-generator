package com.xiaoqu.git.log.extract;

import com.xiaoqu.git.log.extract.webapi.github.DataStreamWebApiJob;
import com.xiaoqu.git.log.extract.webapi.jira.board.JiraBoardJob;
import com.xiaoqu.git.log.extract.webapi.jira.epic.JiraEpicJob;
import com.xiaoqu.git.log.extract.webapi.jira.issue.JiraIssueJob;
import com.xiaoqu.git.log.extract.webapi.jira.worklog.JiraWorkLogJob;

public class EntryPoint {

    public static void main(String[] args) throws Exception {
//        DataStreamWebApiJob.run();
//        JiraBoardJob.run();
        JiraEpicJob.run();
//        JiraIssueJob.run();
//        JiraWorkLogJob.run();
    }
}
