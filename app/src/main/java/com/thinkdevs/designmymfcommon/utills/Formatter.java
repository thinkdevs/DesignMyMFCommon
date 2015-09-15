package com.thinkdevs.designmymfcommon.utills;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {

    private static SimpleDateFormat toTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static SimpleDateFormat toDate = new SimpleDateFormat("dd/mmm/yyyy", Locale.getDefault());

    public static String formatDateTime(Date date){
        String dateOrTime;
        Date currentDate = new Date(System.currentTimeMillis());
        if(
                currentDate.getDay() == date.getDay() &&
                currentDate.getMonth() == date.getMonth() &&
                currentDate.getYear() == currentDate.getYear())
            dateOrTime = toTime.format(date);
        else
            dateOrTime = toDate.format(date);
        return dateOrTime;
    }
}
