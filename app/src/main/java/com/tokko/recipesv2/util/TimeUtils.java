package com.tokko.recipesv2.util;


import org.joda.time.DateTime;

public class TimeUtils {

    public DateTime getCurrentTime(){
        return new DateTime(System.currentTimeMillis());
    }

    public DateTime getCurrentDate() {
        return getCurrentTime().withTime(0, 0, 0, 0);
    }
}
