package com.xiaoqu.git.log.extract.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeUtils {
    public static final String currentDay;
    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        currentDay = simpleDateFormat.format(new Date());
    }
}
