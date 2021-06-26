package com.vts.clientcenter.helpers;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DateUtil implements InitializingBean {

    private static DateUtil instance;

    private static  final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static DateUtil getInstance() {
        return instance;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DateUtil.instance = this;
        logger.info("init date  util.");
    }

    /**
     * 获得当前系统时间
     * 格式yyyy-MM-dd hh:mm:ss
     * @return Date
     * @author mshi
     */
    public Date getCurrentTime(){
        return Calendar.getInstance().getTime();
    }

    /**
     * 获得当前系统时间
     * 格式 yyyy-MM-dd
     * @return Date
     * @author mshi
     */
    public Date getCurrentDate(){
        return getDateIgnoreTime(Calendar.getInstance().getTime());
    }

    /**
     * 获取时间上下间隔
     * @param baseDate
     * @return Date[]
     * @author mshi
     */
    public Date[] getDifferenceDate(Date baseDate) {
        Date[] date = new Date[2];
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date[0] = cal.getTime();
        cal.add(Calendar.DATE, 1);
        date[1] = cal.getTime();
        return date;
    }

    /**
     * 将时间的时分秒毫秒归零
     * @param baseDate
     * @return Date
     * @author mshi
     */
    public Date getDateIgnoreTime(Date baseDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 比较两个日期差,精确到天
     * @param date1
     * @param date2
     * @return int
     * @author mshi
     */
    public int compareByDate(Date date1, Date date2) {
        int num = getDaysBetweenDates(date1, date2);
        if (num > 0)
            return 1;
        else if (num < 0)
            return -1;
        else if(num == 0)
            return 0;
        return num;
    }

    /**
     * 获得两个日期差几天,精确到秒
     * @param date1
     * @param date2
     * @return int
     * @author mshi
     */
    public int compareByDateTime(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        TimeZone timeZone = TimeZone.getDefault();
        long beginOffset = timeZone.getRawOffset();
        long endOffset = beginOffset;
        if (timeZone.inDaylightTime(date1)) beginOffset += timeZone.getDSTSavings();
        if (timeZone.inDaylightTime(date2)) endOffset += timeZone.getDSTSavings();
        long milli1 = (date1.getTime() + beginOffset) / 1000;
        long milli2 = (date2.getTime() + endOffset) / 1000;
        int retVal = 0;
        if (milli1 > milli2) {
            retVal = 1;
        } else if (milli1 < milli2) {
            retVal = -1;
        }
        return retVal;
    }

    /**
     * 获得两个日期差几天,精确到毫秒
     * @param date1
     * @param date2
     * @return int
     * @author mshi
     */
    public int compareByTimestamp(Date date1, Date date2) {
        if(date1 ==null || date2 == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        TimeZone timeZone = TimeZone.getDefault();
        long beginOffset = timeZone.getRawOffset();
        long endOffset = beginOffset;
        if (timeZone.inDaylightTime(date1)) beginOffset += timeZone.getDSTSavings();
        if (timeZone.inDaylightTime(date2)) endOffset += timeZone.getDSTSavings();
        long milli1=date1.getTime() + beginOffset;
        long milli2=date2.getTime() + endOffset;
        int retVal = 0;
        if (milli1 > milli2) {
            retVal = 1;
        } else if (milli1 < milli2) {
            retVal = -1;
        }
        return retVal;
    }

    /**
     * 获得两个日期差几天
     * @param endDate
     * @param beginDate
     * @return int
     * @author mshi
     */
    public int getDaysBetweenDates(Date endDate, Date beginDate) {
        if (endDate == null || beginDate == null) {
            logger.warn("case:计算两个日期差几天失败,reason:endDate or beginDate is null");
            throw new IllegalArgumentException("Date cannot be null.");
        }
        return(getDaysBetweenDates((TimeZone)null, endDate, beginDate));
    }

    /**
     * 获得两个日期差几天
     * @param timeZone
     * @param endDate
     * @param beginDate
     * @return int
     * @author mshi
     */
    public int getDaysBetweenDates(TimeZone timeZone, Date endDate, Date beginDate) {
        if (beginDate == null || endDate == null) {
            logger.warn("case:计算两个日期差几天失败,reason:endDate or beginDate is null");
            throw new IllegalArgumentException("Date cannot be null.");
        }
        if (timeZone == null) timeZone = TimeZone.getDefault();
        long beginOffset = timeZone.getRawOffset();
        long endOffset = beginOffset;
        if (timeZone.inDaylightTime(beginDate)) beginOffset += timeZone.getDSTSavings();
        if (timeZone.inDaylightTime(endDate)) endOffset += timeZone.getDSTSavings();
        long endDays = (long)((endDate.getTime() + endOffset) / 86400000L);
        long beginDays = (long)((beginDate.getTime() + beginOffset) / 86400000L);
        return((int)(endDays - beginDays));
    }



    public String getTimesBetweenDates(Date endDate, Date beginDate){
        if (endDate == null || beginDate == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        long timestamp = endDate.getTime() - beginDate.getTime();
        if(timestamp <= 0){
            return null;
        }
        long timeseconds = timestamp / 1000;
        Integer day=(int)(timeseconds/(24*60*60));
        Integer hour=(int)(timeseconds/(60*60)-day*24);
        long min=((timeseconds/(60))-day*24*60-hour*60);
        long s=(timeseconds-day*24*60*60-hour*60*60-min*60);
        String str = new String(day + "天" + hour + "小时" + min + "分"+ s + "秒");
        return str;
    }

    public Long getSecondsBetweenDates(Date endDate, Date beginDate){
        if (endDate == null || beginDate == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        long timestamp = endDate.getTime() - beginDate.getTime();
        if(timestamp <= 0){
            return null;
        }
        long timeseconds = timestamp / 1000;
        return timeseconds;
    }


    /**
     * 获取两个日期的半小时差值
     *@author oliver
     *@time 2015年3月19日 下午7:14:56
     */
    public long getHalfHoursBetweenDates(Date endDate, Date beginDate){
        if (endDate == null || beginDate == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        long timestamp = endDate.getTime() - beginDate.getTime();
        long timeseconds = timestamp / 1000;
        long halfHour = (int) Math.floor((double)timeseconds/(30*60));
        return halfHour;
    }

    public Integer getMonthsBetweenDates(Date endDate, Date beginDate){
        if (endDate == null || beginDate == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        long timestamp = endDate.getTime() - beginDate.getTime();
        if(timestamp <= 0){
            return -1;
        }
        int monthEnd = getMonth(endDate);
        int monthBegin = getMonth(beginDate);
        int dayEnd = getDay(endDate);
        int dayStart = getDay(beginDate);
        int yearEnd = getYear(endDate);
        int yearStart = getYear(beginDate);
        int monthFlag = 0;
        monthFlag = dayEnd - dayStart >= 0 ? 1 : 0;
        int tempMonthBetween = (monthEnd - monthBegin) + (yearEnd - yearStart) * 12;
        if(1 > monthFlag){
            Date newDate = add(beginDate, Calendar.MONTH, tempMonthBetween);
            monthFlag = getDay(newDate) == getDay(endDate) ? 1 : 0;
        }

        return tempMonthBetween + monthFlag;
    }



    /**
     * 获得年份
     * @param date
     * @return int
     * @author mshi
     */
    public int getYear(Date date) {
        if (date == null) return 0;
        return getCalendarField(date, Calendar.YEAR);
    }

    /**
     * 获得月份
     * @param date
     * @return int
     * @author mshi
     */
    public int getMonth(Date date) {
        if (date == null) return 0;
        return getCalendarField(date, Calendar.MONTH) + 1;
    }

    /**
     * 获得天数
     * @param date
     * @return int
     * @author mshi
     */
    public int getDay(Date date) {
        if (date == null) return 0;
        return getCalendarField(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得小时
     * @param date
     * @return int
     * @author mshi
     */
    public int getHour(Date date) {
        if (date == null) return 0;
        return getCalendarField(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得分钟
     * @param date
     * @return int
     * @author mshi
     */
    public int getMinute(Date date) {
        if (date == null) return 0;
        return getCalendarField(date, Calendar.MINUTE);
    }

    /**
     * 获得秒
     * @param date
     * @return int
     * @author mshi
     */
    public int getSecond(Date date) {
        if (date == null) return 0;
        return getCalendarField(date, Calendar.SECOND);
    }

    /**
     * 获得毫秒
     * @param date
     * @return int
     * @author mshi
     */
    public int getMilliSecond(Date date) {
        if (date == null) return 0;
        return getCalendarField(date, Calendar.MILLISECOND);
    }

    /**
     * 返回给定日历字段的值
     * @param date
     * @param field
     * @return int
     * @author mshi
     */
    public int getCalendarField(Date date, int field){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    /**
     * 添加或减去指定的时间量,按年
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addYear(Date date, int value){
        return add(date, Calendar.YEAR, value);
    }

    /**
     * 添加或减去指定的时间量,按月
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addMonth(Date date, int value){
        return add(date, Calendar.MONTH, value);
    }

    /**
     * 添加或减去指定的时间量,按日
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addDay(Date date, int value){
        return add(date, Calendar.DAY_OF_MONTH, value);
    }


    /**
     * 添加或减去指定的时间量,按小时
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addHour(Date date, int value) {
        return add(date, Calendar.HOUR_OF_DAY, value);
    }

    /**
     * 添加或减去指定的时间量,按小时,并设置分钟和秒为00:00
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addHourAndSetMinSec(Date date, int value) {
        return addAndSetMinSec(date, Calendar.HOUR_OF_DAY, value);
    }


    /**
     * 添加或减去指定的时间量,按分钟
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addMinute(Date date, int value) {
        return add(date, Calendar.MINUTE, value);
    }

    /**
     * 添加或减去指定的时间量,按秒
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addSecond(Date date, int value) {
        return add(date, Calendar.SECOND, value);
    }

    /**
     * 添加或减去指定的时间量,按毫秒
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addMilliSecond(Date date, int value) {
        return add(date, Calendar.MILLISECOND, value);
    }

    /**
     * 添加或减去指定的时间量
     * @param date
     * @param field
     * @param value
     * @return Date
     * @author mshi
     */
    public Date add(Date date, int field, int value){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, value);
        return cal.getTime();
    }


    /**
     * 添加或减去指定的时间量,设置分秒为0
     * @param date
     * @param field
     * @param value
     * @return Date
     * @author mshi
     */
    public Date addAndSetMinSec(Date date, int field, int value){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, value);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }


    public String getNowHourString(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        String str = formatTime24(cal.getTime());
        return str;
    }

    /**
     * 设置指定的时间量,按年
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date setYear(Date date, int value){
        return set(date, Calendar.YEAR, value);
    }

    /**
     * 设置指定的时间量,按月
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date setMonth(Date date, int value){
        return set(date, Calendar.MONTH, value);
    }

    /**
     * 设置指定的时间量,按日
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date setDay(Date date, int value){
        return set(date, Calendar.DAY_OF_MONTH, value);
    }

    /**
     * 设置指定的时间量,按小时
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date setHour(Date date, int value) {
        return set(date, Calendar.HOUR_OF_DAY, value);
    }

    /**
     * 设置指定的时间量,按分钟
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date setMinute(Date date, int value) {
        return set(date, Calendar.MINUTE, value);
    }

    /**
     * 设置指定的时间量,按秒
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date setSecond(Date date, int value) {
        return set(date, Calendar.SECOND, value);
    }

    /**
     * 设置指定的时间量,按毫秒
     * @param date
     * @param value
     * @return Date
     * @author mshi
     */
    public Date setMilliSecond(Date date, int value) {
        return set(date, Calendar.MILLISECOND, value);
    }

    /**
     * 设置指定的时间量
     * @param date
     * @param field
     * @param value
     * @return Date
     * @author mshi
     */
    public Date set(Date date, int field, int value){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(field, value);
        return cal.getTime();
    }

    /**
     * 设置日期为当月的最后一天
     * @param date
     * @return Date
     * @author mshi
     */
    public Date lastDate(Date date){
        return addDay(addMonth(setDay(date, 1), 1), -1);
    }

    /**
     * 设置日期为当月的第一天
     * @param date
     * @return Date
     * @author mshi
     */
    public Date firstDate(Date date){
        return setDay(date, 1);
    }

    /**
     * 是否是当月最后一天
     * @param date
     * @return boolean
     * @author mshi
     */
    public boolean isLastDate(Date date) {
        Date _date = date;
        Date lastDate = lastDate(_date);
        if (compareByDate(date, lastDate) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否是当月第一天
     * @param date
     * @return boolean
     * @author mshi
     */
    public boolean isFirstDate(Date date) {
        Date _date = date;
        Date firstDate = firstDate(_date);
        if (compareByDate(date, firstDate) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否是同一天
     * @return boolean
     * @author mshi
     */
    public boolean isSameDate(Date date1, Date date2) {
        if (compareByDate(getDateIgnoreTime(date1), getDateIgnoreTime(date2)) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据指定格式格式化时间
     * @param date
     * @param fmtString
     * @return String
     * @author mshi
     */
    public String format(Date date, String fmtString) {
        DateFormat format = new SimpleDateFormat(fmtString);
        return format.format(date);
    }

    /**
     * 格式格式化时间("yyyy-MM")
     * @param date
     * @return String
     * @author mshi
     */
    public String formatMonth(Date date) {
        return format(date, "yyyy-MM");
    }

    /**
     * 格式格式化时间("yyyy-MM-dd")
     * @param date
     * @return String
     * @author mshi
     */
    public String formatDate(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 格式格式化时间("yyyy-MM-dd hh:mm:ss")
     * @param date
     * @return String
     * @author mshi
     */
    public String formatTime(Date date) {
        return format(date, "yyyy-MM-dd hh:mm:ss");
    }

    public String formatTime24(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }
    public String formatTimeHMS24(Date date) {
        return format(date, "yyyy-MM-ddHHmmss");
    }

    public String formatHM24(Date date) {
        return format(date, "HH:mm");
    }

    /**
     * 根据指定格式解析时间
     * @param dateString
     * @param fmtString
     * @return Date
     * @author mshi
     */
    public Date parse(String dateString, String fmtString){
        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat(fmtString);
            date = format.parse(dateString);
        } catch (ParseException e) {
            logger.error("case : 跟据指定格式解析时间失败,reason :{},para : dateString={}&fmtString={}",e.getMessage(),dateString, fmtString);
        }
        return date;
    }

    /**
     * 解析时间("yyyy-MM-dd")
     * @param dateString
     * @return Date
     * @author mshi
     */
    public Date parseDate(String dateString){
        return parse(dateString, "yyyy-MM-dd");
    }

    /**
     * 解析时间("yyyy-MM-dd hh:mm:ss")
     * @param dateString
     * @return Date
     * @author mshi
     */
    public Date parseTime(String dateString){
        return parse(dateString, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 验证传入的 dateString 是否为日期
     * fmtString 一定是一个日期格式化类型，例如: yyyy-MM-dd HH:mm:ss, yyyyMMdd...
     * @param dateString 需要格式化的日期
     * @param fmtString 类型
     * @return
     */
    public static boolean isValidDate(String dateString, String fmtString){
        try{
            DateFormat format = new SimpleDateFormat(fmtString);
            format.parse(dateString);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Date endOfDay(Date date){
        date=set(date, Calendar.HOUR_OF_DAY, 23);
        date=set(date, Calendar.MINUTE, 59);
        return date;
    }
    /**
     * 获得两个时间之间的分钟数
     * @param date1
     * @param date2
     * @return
     */
    public int getDistanceMin(Date date1,Date date2){
        Long min=0l;
        Long time1 = date1.getTime();
        Long time2 = date2.getTime();
        if(time1<time2) {  min = time2/(60*1000) - time1/(60*1000);
        } else {  min = time1/(60*1000) - time2/(60*1000); }
        return min.intValue();
    }
    /**
     * 获得两个时间之间的分钟数 正数
     * @param date1
     * @param date2
     * @return
     */
    public int getDistanceMinPositive (Date date1,Date date2){
        Long min=0l;
        Long time1 = date1.getTime();
        Long time2 = date2.getTime();
        min = time2/(60*1000) - time1/(60*1000);

        return min.intValue();
    }

    /***
     * 得到前一天的当前时刻
     * @return
     * add by jannal
     * 2014年12月15日
     */
    public String getYesterDay(String format) {
        Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -1); // 得到前一天
        String yestedayDate = new SimpleDateFormat(format).format(calendar.getTime());
        return yestedayDate;
    }
    /***
     * 得到前两天的当前时刻
     * @return
     * add by jannal
     * 2014年12月15日
     */
    public String getBeforeYesterDay(String format) {
        Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -2); // 得到前天
        String yestedayDate = new SimpleDateFormat(format).format(calendar.getTime());
        return yestedayDate;
    }
    /***
     * 得到后一天的当前时刻
     * @return
     * add by jannal
     * 2014年12月15日
     */
    public String getafterDay(String format) {
        Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, 1); // 得到前一天
        String yestedayDate = new SimpleDateFormat(format).format(calendar.getTime());
        return yestedayDate;
    }
    /**
     * 得到当前的yyyy-MM-dd
     * @return
     * add by jannal
     * 2014年12月16日
     */
    public String getafterDayDefalutFormatt() {
        return getafterDay("yyyy-MM-dd");
    }
    /**
     * 得到前一天的当前时刻 yyyy-MM-dd
     * @return
     * add by jannal
     * 2014年12月15日
     */
    public String getBeforeYesterDayDefalutFormatt(){
        return getBeforeYesterDay("yyyy-MM-dd");
    }
    /**
     * 得到前一天的当前时刻 yyyy-MM-dd
     * @return
     * add by jannal
     * 2014年12月15日
     */
    public String getYesterDayDefalutFormatt(){
        return getYesterDay("yyyy-MM-dd");
    }

    /**
     * 得到当前的yyyy-MM-dd
     * @return
     * add by jannal
     * 2014年12月16日
     */
    public String getCurrentDayDefalutFormatt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 得到时间字符串的yyyyMMdd
     * @return
     * add by songtao
     * 2016年01月22日
     */
    public String getDayDefalutFormatt(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(parseDate(dateString));
    }
    /**
     * 得到时间字符串的yyyyMMdd
     * @return
     * add by songtao
     * 2016年01月22日
     */
    public String getyyyyMMddFormatt(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    /**
     * 得到当前时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String getCurrentDayTime24Formatt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
    /**
     * 得到当前时间 通过指定格式
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String getCurrentDayTimeByFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
    /***
     * 指定的格式获取日期
     * @param formatt
     * @return
     *add by jannal
     *2014年12月25日
     */
    public String getDateForFormat(String formatt){
        SimpleDateFormat sdf = new SimpleDateFormat(formatt);
        return sdf.format(new Date());
    }

    public String getDateForFormat(Date date,String formatt){
        SimpleDateFormat sdf = new SimpleDateFormat(formatt);
        return sdf.format(date);
    }


    /***
     * 比较目标日期是否在两个日期之间
     * @param startDate
     * @param endDate
     * @param goalDate
     * @return
     * add by jannal
     * 2014年12月25日
     */
    public boolean dateInterval(Date startDate ,Date endDate,Date goalDate){
        int flag = compareByTimestamp(startDate,endDate);//startDate小于endDate 返回-1
        int start = compareByTimestamp(goalDate,startDate);// 第一个大于第二个返回1 否则返回-1 相等返回0
        int end = compareByTimestamp(goalDate,endDate);
        if(flag==-1&&start==1&&end==-1){
            return true;
        }
        return false;
    }

    /**
     * 存取所有所有的还款时间 key 为期数，value为还款时间
     * 总共期数
     * @param sumPeriod
     * 满标 日期
     * manbiaoDate
     */
    public Map<String,Date> getAllRepayTime(int sumPeriod,Date manbiaoDate){
        Map map = new HashMap<String,Date>();
        for(int i = 1 ; i<=sumPeriod;i++){
            map.put(i, addMonth(addDay(manbiaoDate, 1), i));
        }
        return map;
    }


    public Date getNextPeridDate(int currentPeriod,int sumPeriod,Date currentDate,Date manbiaoDate){
        Map map = getAllRepayTime(sumPeriod,manbiaoDate);
        if(dateInterval(manbiaoDate,addMonth(addDay(manbiaoDate, 1), sumPeriod),currentDate)){ //判断当前日期是否在满标日期与最后还款日期之间
            Date  currentPeiodDate = (Date)map.get(currentPeriod);
            Date  nextPeiodDate = (Date)map.get(currentPeriod+1);
            int flag = getDaysBetweenDates(currentPeiodDate, currentDate);
            if(flag>0){
                //下一还款日取currentPeiodDate
                return currentPeiodDate;
            }else{
                return nextPeiodDate;
            }
        }
        return null;
    }

    public String getNextRepayDate(String manbiaoDateStr,Integer period,Integer leftPeriod){
        String nextRepayDate = null;
        if(!StringUtils.isEmpty(manbiaoDateStr)){
            if(period==null){
                period = 0;
            }
            if(leftPeriod!=null){
                Integer currentPeriod = period-leftPeriod;
                if(currentPeriod<=0){
                    // return null;
                    currentPeriod = 1;
                }
                Date currentDate = new Date();
                Date manbiaoDate = parseTime(manbiaoDateStr);
                Date nextPeridDate = getNextPeridDate(currentPeriod, period, currentDate, manbiaoDate);
                nextRepayDate= getDateForFormat(nextPeridDate, "yyyy-MM-dd");
            }
        }
        return nextRepayDate;
    }

    /**
     * 返回间隔时间millioss
     *@author oliver
     *@time 2015年2月10日 下午4:39:02
     */
    public Long betweenMillions(String startTime,String endTime){
        Long startMillioss = parseTime(startTime).getTime();
        Long endMillions = parseTime(endTime).getTime();
        Long between = endMillions - startMillioss;
        return between;
    }
    /**
     * 得到本周周一
     * @return Date
     */
    public Date getMonOfWeek(Date date){

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return parseDate(formatDate(c.getTime()));
    }

    public  Date getSunOfWeek(Date date){

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return parseDate(formatDate(c.getTime()));
    }

    /**
     * Descrption:取得当前日期,格式为:YYYYMMDD
     * @return String
     * @throws Exception
     */
    public  String getNowShortDate()
    {
        String nowDate = "";
        java.sql.Date date = null;
        date = new java.sql.Date(new Date().getTime());
        SimpleDateFormat sdfShort = new SimpleDateFormat("yyyyMMdd");
        nowDate = sdfShort.format(date);
        return nowDate;
    }

    /**
     * Descrption:取得当前日期,格式为:YYYYMMDDHHmmss
     * @return String
     * @throws Exception
     */
    public String getNowDate()
    {
        String nowDate = "";
        java.sql.Date date = null;
        date = new java.sql.Date(new Date().getTime());
        SimpleDateFormat sdfShort = new SimpleDateFormat("yyyyMMddHHmmss");
        nowDate = sdfShort.format(date);
        return nowDate;
    }

    /**
     * 当前日期的前一天
     * @return
     */
    public String getTheDateBefore(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);
        int month = cal.get(2)+1;
        int date = cal.get(5)-1;
        return year+"-"+(month > 10 ? month : "0"+month)+"-"+(date > 10 ? date : "0"+date);
    }

    /**
     * 	指定月份的第一天,[若传null,则使用当前时间]
     *  参数格式:yyyy-MM
     * @return String 格式：yyyy-MM-dd
     */
    public String getFirstDayOfCurrentMonth(Date date){

        String str = "";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        if (date !=null) {
            lastDate.setTime(date);//设置指定时间
        }
        lastDate.set(Calendar.DATE,1);//设为当前月的1 号
        //lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
        str=sdf.format(lastDate.getTime());
        return str;
    }

    /**
     *  获得指定月份的最后一天,[若传null,则使用当前时间]
     *  参数格式:yyyy-MM
     * @return String 格式：yyyy-MM-dd
     */
    public String getEndDayOfCurrentMonth(Date date){
        String str = "";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        if (date != null) {
            lastDate.setTime(date);//设置指定时间
        }
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str=sdf.format(lastDate.getTime());
        return str;
    }
    /**
     *  获得指定月份的倒数第二天,[若传null,则使用当前时间]
     *  参数格式:yyyy-MM
     * @return String 格式：yyyy-MM-dd
     */
    public String getEnd2DayOfCurrentMonth(Date date){
        String str = "";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        if (date != null) {
            lastDate.setTime(date);//设置指定时间
        }
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -2);//日期回滚2天，也就是本月倒数第二天
        str=sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 	指定月份的上月第一天,[若传null,则使用当前时间]
     *  参数格式:yyyy-MM
     * @return String 格式：yyyy-MM-dd
     */
    public String getFirstDayOfPreviousMonth(Date date){

        String str = "";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        if (date !=null) {
            lastDate.setTime(date);//设置指定时间
        }
        lastDate.set(Calendar.DATE,1);//设为当前月的1 号
        lastDate.add(Calendar.MONTH,-1);//减一个月，变为下月的1 号
        //lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
        str=sdf.format(lastDate.getTime());
        return str;
    }

    /**
     *  获得指定月份中上月最后一天,[若传null,则使用当前时间]
     *  参数格式:yyyy-MM
     * @return String 格式：yyyy-MM-dd
     */
    public String getEndDayOfPreviousMonth(Date date){
        String str = "";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        if (date != null) {
            lastDate.setTime(date);//设置指定时间
        }
        lastDate.add(Calendar.MONTH,-1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str=sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 添加或减去指定的时间量,按日
     * @param date 时间
     * @param value 偏移量
     * @param format 格式 eg:yyyy-MM-dd
     * @return String
     */
    public String addDayFormat(Date date, int value, String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date addDay = addDay(date, value);
        String dateFormat = sdf.format(addDay);
        return dateFormat;
    }

    public String addTimeForString(String dateStr,String value){
        //field:取值：(可以根据需求添加其他方法)
        //Calendar.MONTH
        //Calendar.DAY_OF_MONTH
        if(StringUtils.isEmpty(dateStr)){
            return "error date str !";
        }
        return formatDate(add(parseDate(dateStr),Calendar.MONTH, Integer.parseInt(value)));
    }

    /**
     * 根据身份证号码获取年龄
     *
     * @param id 身份证号
     * @throws Exception 身份证号错误时发生
     * @return int - 年龄
     */
    public int getAge(String id) throws Exception
    {
        int age = -1;
        int length = id.length();
        String birthday = "";
        if (length == 15)
        {
            birthday = id.substring(6, 8);
            birthday = "19" + birthday;
        }
        else if (length == 18)
        {
            birthday = id.substring(6, 10);
        }
        else
        {
            throw new Exception("错误的身份证号");
        }
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        age = currentYear - (new Integer(birthday)).intValue();
        return age;
    }

    /**
     *  获取年eg:1990
     * @param birthday
     * @return
     */
    public String getBirthYear(String birthday){
        return birthday.substring(0, 4);
    };

    /**
     * 获取 月日, 格式: 10-22
     * @param birthday
     * @return
     */
    public String getBirthday(String birthday){
        StringBuilder builder = new StringBuilder();
        String month = birthday.substring(4,6);
        String day = birthday.substring(6,8);

        StringBuilder append = builder.append(month).append("-").append(day);


        return append.toString();
    };

    /**
     * 根据身份证号 获取出生年月 eg:19901022
     */
    public String getBirthDayByCardId(String cardId){
        String birthday = "";
        if(StringUtils.isNotEmpty(cardId)){
            if(cardId.length() == 15){
                birthday = "19"+cardId.substring(6, 12);
            }else if(cardId.length() == 18){
                birthday = cardId.substring(6, 14);
            }
        }

        return birthday;
    }
    /**
     * 获取 指定日期 后 指定毫秒后的 Date
     *
     * @param date
     * @param millSecond
     * @return
     */
    public Date getDateAddMillSecond(Date date, int millSecond) {
        Calendar cal = Calendar.getInstance();
        if (null != date) {// 没有 就取当前时间
            cal.setTime(date);
        }
        cal.add(Calendar.MILLISECOND, millSecond);
        return cal.getTime();
    }
}
