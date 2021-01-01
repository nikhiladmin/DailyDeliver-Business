package com.daytoday.business.dailydelivery.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtils {
    public static String getCurrentTimeStamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        return currentDateTime;
    }
}
