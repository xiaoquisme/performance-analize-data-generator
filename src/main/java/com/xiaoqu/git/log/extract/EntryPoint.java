package com.xiaoqu.git.log.extract;

import com.xiaoqu.git.log.extract.webapi.github.DataStreamWebApiJob;
import com.xiaoqu.git.log.extract.webapi.jira.board.JiraBoardJob;

public class EntryPoint {
    public static void main(String[] args) throws Exception {
        DataStreamWebApiJob.run();
        JiraBoardJob.run();
    }
}
