package net.liuxuan.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2019-04-02
 **/
public class ThreadLocalDateUtil {
    private static String date_format = "yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

    public static DateFormat getDateFormat() {
        return getDateFormat(date_format);
    }

    public static DateFormat getDateFormat(String format) {
        DateFormat df = threadLocal.get();
        if (df == null) {
            df = new SimpleDateFormat(format);
            threadLocal.set(df);
        }
        return df;
    }

    public static String formatDate(Date date) {
        return getDateFormat().format(date);
    }

    public static String formatDate(Date date, String date_format) {
        return getDateFormat(date_format).format(date);
    }

    public static Date parse(String strDate) throws ParseException {
        return getDateFormat().parse(strDate);
    }

    public static Date parse(String strDate, String date_format) throws ParseException {
        return getDateFormat(date_format).parse(strDate);
    }
}
