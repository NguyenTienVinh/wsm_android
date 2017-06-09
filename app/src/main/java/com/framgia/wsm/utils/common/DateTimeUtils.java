package com.framgia.wsm.utils.common;

import com.framgia.wsm.utils.Constant;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by framgia on 19/05/2017.
 */
public final class DateTimeUtils {
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy/MM/dd";
    public static final String DATE_FORMAT_YYYY_MM_JAPANESE = "MM/yyyy";
    public static final String FORMAT_DATE = "dd/MM/yyyy";
    public static final String TIME_FORMAT_HH_MM = "HH:mm";
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM = "yyyy/MM/dd - HH:mm";

    private DateTimeUtils() {
        // No-op
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    public static String convertToString(Date source, String format) {
        if (source == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(source);
    }

    public static Date convertStringToDate(String date) {
        SimpleDateFormat parser = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD, Locale.getDefault());
        try {
            return parser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date();
        }
    }

    private static Date convertStringToDateTime(String date) {
        SimpleDateFormat parser =
                new SimpleDateFormat(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MM, Locale.getDefault());
        try {
            return parser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date();
        }
    }

    public static String convertDateTimeToString(String dateTime, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        return dateTime + Constant.BLANK_DASH_BLANK + convertToString(calendar.getTime(),
                TIME_FORMAT_HH_MM);
    }

    public static String convertDateToString(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return convertToString(calendar.getTime(), DATE_FORMAT_YYYY_MM_DD);
    }

    public static int getDayOfMonth(String dateTime) {
        return getDateOrTime(dateTime, Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(String dateTime) {
        return getDateOrTime(dateTime, Calendar.MONTH);
    }

    public static int getYear(String dateTime) {
        return getDateOrTime(dateTime, Calendar.YEAR);
    }

    public static int getHourOfDay(String dateTime) {
        return getDateOrTime(dateTime, Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(String dateTime) {
        return getDateOrTime(dateTime, Calendar.MINUTE);
    }

    public static int getDayOfWeek(String dateTime) {
        return getDateOrTime(dateTime, Calendar.DAY_OF_WEEK);
    }

    private static int getDateOrTime(String dateTime, int dateOrTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringToDateTime(dateTime));
        return calendar.get(dateOrTime);
    }
}
