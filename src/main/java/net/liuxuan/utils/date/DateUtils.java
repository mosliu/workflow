package net.liuxuan.utils.date;

import lombok.extern.java.Log;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2019-04-11
 **/
@Log
public class DateUtils {
    public static String commonDateTimeFormatString = "yyyy-MM-dd HH:mm:ss";


    /**
     * yyMMddHHmmss
     */
    public static FastDateFormat rowkeyFDT = FastDateFormat.getInstance("yyMMddHHmm");

    /**
     * yyMMddHHmmss
     */
    public static FastDateFormat idfdf = FastDateFormat.getInstance("yyMMddHHmmss");
    /**
     * "yyyyMMdd"
     */
    public static FastDateFormat commonFDF_DateTiny = FastDateFormat.getInstance("yyyyMMdd");
    /**
     * "yyyy-MM-dd HH:mm:ss"
     */
    public static FastDateFormat commonFDF_DateTime = FastDateFormat.getInstance(commonDateTimeFormatString);
    /**
     * "yyyy-MM-dd"
     */
    public static FastDateFormat commonFDF_Date = FastDateFormat.getInstance("yyyy-MM-dd");
    /**
     * "yyyy-MM-dd HH:mm:ss"
     */
    public static final DateTimeFormatter CommonDashDTF = DateTimeFormatter.ofPattern(commonDateTimeFormatString);
    /**
     * "yyyy/MM/dd HH:mm:ss"
     */
    public static final DateTimeFormatter CommonSlashDTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static String[] guessDatePatterns = new String[]{
            commonDateTimeFormatString, "yyyy-MM-dd", "yy-MM-dd HH:mm:ss", "yyyy-MM", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM", "yyyy-MM-dd HH:mm:ss SSS", "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd", "yy/MM/dd HH:mm:ss", "yyyy/MM", "yyyy/MM/dd HH:mm:ss.SSS", "yyyy/MM", "yyyy/MM/dd HH:mm:ss SSS", "yyyy/MM/dd HH:mm",
            "yyyyMMddHHmmss", "yyyyMMdd", "yyMMddHHmmss", "yyyyMM", "yyyyMMdd HHmmssSSS",
            //Sep 11, 2019 6:40:22 AM
            "MMM dd, yyyy hh:mm:ss aa"
    };
    public static String[] guessDatePatternsEnglishLocale = new String[]{
            //Sep 11, 2019 6:40:22 AM
            "MMM dd, yyyy hh:mm:ss aa"
    };

    public static LocalDateTime getLocalTimeCommonDateTimeFormat(String time) {

        return commonDateTimeFormatString(time, commonDateTimeFormatString);
    }

    public static LocalDateTime commonDateTimeFormatString(String time, String pattern) {

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(time, formatter);
    }

    /**
     * 使用"yyyy-MM-dd HH:mm:ss"处理
     *
     * @param str
     * @return
     */
    public static Date parseCommonDate(final String str) {
        try {
            return commonFDF_DateTime.parse(str);
        } catch (ParseException e) {
            log.severe("日期{" + str + "}转换错误！！ " + e.getMessage());
//            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用常用日期格式猜测日期,失败返回null
     *
     * @param str
     * @return 推测得到的日期，失败返回null
     */
    public static Date guessDate(final String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            return parseDate(str, null, guessDatePatterns);
//            return parseDate(str, Locale.ENGLISH, guessDatePatterns);
        } catch (ParseException e) {
            try {
                return parseDate(str, Locale.ENGLISH, guessDatePatternsEnglishLocale);
            } catch (ParseException ex) {

            }
            log.severe("日期{" + str + "}转换错误！！ " + e.getMessage());

        }
        return null;
    }

    public static Date parseDate(final String str, final String... parsePatterns) throws ParseException {
        return parseDate(str, null, parsePatterns);
    }

    public static Date parseDate(final String str, final Locale locale, final String... parsePatterns) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str, locale, parsePatterns);
    }


    private static FastDateFormat monthlyFDF = FastDateFormat.getInstance("yyyyMM");

    /**
     * 获取两个日期间共经历了哪几个月份
     *
     * @param from
     * @param to
     * @return
     */
    public static String[] getPastMonths(String from, String to) {
        Date fromDate = net.liuxuan.utils.date.DateUtils.guessDate(from);
        Date toDate = net.liuxuan.utils.date.DateUtils.guessDate(to);
        return getPastMonths(fromDate, toDate);
    }

    /**
     * 获取两个日期间共经历了哪几个月份
     *
     * @param from
     * @param to
     * @return
     */
    public static String[] getPastMonths(Date from, Date to) {

        if (from == null || to == null) {
            return new String[0];
        }
//        现在逻辑是取近两个月
//        Date now = new Date();
//        先比较两个日期大小
        if (from.getTime() - to.getTime() > 0) {
            Date tmpdate = from;
            from = to;
            to = tmpdate;
        }

        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();
        fromCal.setTime(from);
        toCal.setTime(to);

        List<String> monthlist = new ArrayList<>();
        while (!(org.apache.commons.lang3.time.DateUtils.truncatedEquals(from, to, Calendar.YEAR) && org.apache.commons.lang3.time.DateUtils.truncatedEquals(from, to, Calendar.MONTH))) {
            //年月不相等时候
            monthlist.add(monthlyFDF.format(to));
            to = org.apache.commons.lang3.time.DateUtils.addMonths(to, -1);
        }
        monthlist.add(monthlyFDF.format(from));

        String[] strings = monthlist.toArray(new String[monthlist.size()]);
        return strings;

    }

    /**
     * 获取两个日期间共经历了哪几个月份
     *
     * @param from
     * @param to
     * @return
     */
    public static String[] getPastMonths(LocalDateTime from, LocalDateTime to) {

        if (from == null || to == null) {
            return new String[0];
        }
//        现在逻辑是取近两个月
//        Date now = new Date();
//        先比较两个日期大小
        if (from.isAfter(to)) {
            LocalDateTime tmpdate = from;
            from = to;
            to = tmpdate;
        }

        List<String> monthlist = new LinkedList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        while (from.isBefore(to) || from.getMonth().equals(to.getMonth())) {
            monthlist.add(from.format(formatter));

            from = from.plusMonths(1);
        }

        String[] strings = monthlist.stream().distinct().toArray(String[]::new);
        return strings;

    }

    public static void main(String[] args) {
        String a = "Sep 11, 2019 6:40:22 AM";
        Date date = guessDate(a);
        System.out.println(date);

    }

    /**
     * 上月首日
     * @return
     */
    public static Date getLastMonthFirstDayStartDate() {//可以用new Date().toLocalString()传递参数
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date time = calendar.getTime();
        return time;
    }

    /**
     * 上月首日
     * @return
     */
    public static String getLastMonthFirstDay() {//可以用new Date().toLocalString()传递参数
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        Date time = cal_1.getTime();
        String day = commonFDF_Date.format(time);

//        String day = format.format(time);
//        System.out.println(day);
        return day;
    }

    /**
     * 上月末日
     * @return
     */
    public static Date getLastMonthLastDayEndDate() {//前月末日
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        cale.set(Calendar.HOUR_OF_DAY,23);
        cale.set(Calendar.MINUTE,59);
        cale.set(Calendar.SECOND,59);
        cale.set(Calendar.MILLISECOND,999);
        Date time = cale.getTime();
//        String lastDay = format.format(cale.getTime());
//        System.out.println(lastDay);
        return time;
    }


    /**
     * 上月末日
     * @return
     */
    public static String getLastMonthLastDay() {//前月末日
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        Date time = cale.getTime();
        String day = commonFDF_Date.format(time);
//        String lastDay = format.format(cale.getTime());
//        System.out.println(lastDay);
        return day;
    }

}
