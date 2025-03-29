package com.sakura.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

/**
 * @author 刘智King
 * @date 2020年8月14日 上午11:18:42
 */
public class DateUtil {
	static Logger log = Logger.getLogger(DateUtil.class);
	
    static Calendar cale = Calendar.getInstance();;
    static Map<String,String> date = new HashMap<>();
    
    /**
     * 获取当前系统时间
     * 
     * @return
     */
    public static String getDate() {
        SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Date.format(new Date());
    }

    public static String getDateFormat(String DateFormat) {
        SimpleDateFormat Date = new SimpleDateFormat(DateFormat);
        return Date.format(new Date());
    }
    
    public static String getDateFormat(String DateFormat,String Script) throws ScriptException, Exception {
        SimpleDateFormat Date = new SimpleDateFormat(DateFormat);
        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
        int a = (int) jse.eval(SeleniumUtil.parseStringHasEls(Script));
        return Date.format(new Date().getTime()+a*1000);
    }
    
    /**
     * 获取当前系统时间戳
     * 
     * @return
     */
    public static String getTime(String type) {
    	String Time ="";
        if("毫秒".equals(type)) {
//        	Time = String.valueOf(new Date().getTime());
        	Time = String.valueOf(System.currentTimeMillis());
        }else if("秒".equals(type)) {
        	Time = String.valueOf(System.currentTimeMillis() / 1000);
        }
        return Time;
    }
    
    /**
     * 获取昨天
     * 
     * @return
     */
    public static String getYesterDay(String DateFormat) {
    	Calendar   cal   =   Calendar.getInstance();
        cal.add(Calendar.DATE,   -1);
        String yesterday = new SimpleDateFormat(DateFormat).format(cal.getTime());
//        log.info(yesterday);
        return yesterday;
    }
    
    /**
     * 获取当前月的第一天和最后一天
     * 
     * @return
     */
    public static Map<String,String> getDateCalendar(String DateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(DateFormat);
        String firstday,lastday;
        
        // 获取当前月的第一天
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        
        // 获取当前月的最后一天
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        
        date.put("firstday", firstday);
        date.put("lastday", lastday);
        return date;
    }

    /**
     * 获取当年的第一天
     */
    public static Date getCurrentFirstOfYear(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        return calendar.getTime();
    }
 
    /**
     * 获取当年的最后一天
     */
    public static Date getCurrentLastOfYear(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }
 
    /**
     * 获取某年第一天日期
     * @param date 日期
     * @return Date
     */
    public static Date getFirstOfYear(Date date){
//        int year = date.getYear(); // date.getYear 该方法被废弃了
        int year = Integer.parseInt(String.format("%tY",date));
        
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }
 
    /**
     * 获取某年最后一天日期
     * @param date 日期
     * @return Date
     */
    public static Date getLastOfYear(Date date){
        int year = Integer.parseInt(String.format("%tY",date));
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     *  时间转时间字符串为yyyy-MM-dd HH:mm:ss 格式
     * @param date  日期
     * @return String
     */
    public static String formatDateTime(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
//        log.info("strDate:" + strDate);
        return strDate;
    }
    
    /**
     *  时间转时间字符串
     * @param date  日期
     * @param pattern  格式
     * @return String
     */
    public static String formatDateTime(Date date, String pattern) {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String strDate = sdf.format(date);
//        log.info("strDate:" + strDate);
        return strDate;
    }
 
    /**
     *  字符串转化为时间
     * @param str  日期
     * @return Date
     */
    public static Date parseDateTime(String str) {
        if (str == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = null;
        try {
        	myDate = sdf.parse(str);
        }catch (Exception e){
            e.printStackTrace();
        }
//        log.info("myDate: "+myDate);
        return myDate;
    }
 
    /**
     *  字符串转时间，自定义格式
     * @param str  字符串
     * @param dateTimePattern  格式
     * @return Date
     */
    public static Date parseDateTime(String str, String dateTimePattern) {
    	if (str == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimePattern);
        Date myDate = null;
        try {
        	myDate = sdf.parse(str);
        }catch (Exception e){
            e.printStackTrace();
        }
//        log.info("myDate: "+myDate);
        return myDate;
    }
    
    /**
     *  字符串转时间，自定义格式
     * @param str  字符串
     * @param dateTimePattern  格式
     * @return Date
     */
    public static Map<String,String> parseDateTime(int maximum_selectable_minutes, int select_interval, int need_interval) {
    	Map<String,String> time = new HashMap<>();
    			
    	int maximum_minutes = 60; //最大分钟数
//		int maximum_selectable_minutes = 50; //最大可选择分钟数
//		int select_interval = 10; //可选择间隔分钟数
//		int need_interval = 3; //需求所需间隔分钟数
//    	int special_HH = 00; //特殊小时数
		int special_mm = 00; //特殊分钟数
		DecimalFormat mor=new DecimalFormat("##00.##");
		
        String start_time = "";
        String end_time = "";
        String  start_HH =getDateFormat("HH");
        String  start_mm =getDateFormat("mm");
        int HH_max = Integer.parseInt(start_HH);
        int mm_max = Integer.parseInt(start_mm);
//        int HH_max = 00;
//        int mm_max = 59;
        if(mm_max<=maximum_selectable_minutes-need_interval&&mm_max!=special_mm) {
        	int m = Integer.parseInt(String.valueOf(mm_max+select_interval+need_interval-1).substring(0,1));
        	start_time = mor.format(HH_max)+":"+ String.valueOf(m-1) +"0"; 
        	end_time = mor.format(HH_max)+":"+String.valueOf(m)+"0"; 
        }else if(mm_max>maximum_selectable_minutes-need_interval&&mm_max<maximum_minutes-need_interval&&mm_max!=special_mm) {
        	start_time = mor.format(HH_max)+":50"; 
        	if(HH_max+1>23) {
        		HH_max=0;
            	end_time = mor.format(HH_max)+":"+"00"; 
            }else {
            	end_time = mor.format(HH_max+1)+":"+"00"; 
            }
        }else if(mm_max==maximum_minutes-need_interval&&mm_max!=special_mm) {
        	int m = Integer.parseInt(String.valueOf(mm_max).substring(0,1));
        	start_time = mor.format(HH_max)+":"+String.valueOf(m)+"0"; 
        	if(HH_max+1>23) {
        		HH_max=0;
            	end_time = mor.format(HH_max)+":"+"00"; 
            }else {
            	end_time = mor.format(HH_max+1)+":"+"00"; 
            }
        }else if(mm_max>maximum_minutes-need_interval&&mm_max!=special_mm) {
        	if(HH_max+1>23) {
        		HH_max=0;
        		start_time = mor.format(HH_max)+":"+"00"; 
            	end_time = mor.format(HH_max)+":"+"10"; 
            }else {
            	start_time = mor.format(HH_max+1)+":"+"00"; 
            	end_time = mor.format(HH_max+1)+":"+"10"; 
            }
        }else if(mm_max==special_mm) {
        	start_time = mor.format(HH_max)+":"+"00"; 
        	end_time = mor.format(HH_max)+":"+"10"; 
        }
        log.info(start_time);
        log.info(end_time);
        time.put("start_time", start_time);
        time.put("end_time", end_time);
        return time;
    }
	
    public static List<String> minuteList(int interval) {
		Date date = new Date();
		List<Date> ds = test(date,interval);
		List<String> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (Date d : ds) {
			list.add(sdf.format(d));
		}
		return list;
	}

    public static Map<String, String> parseDateTime(String need_time,int interval) throws ScriptException, Exception {
    	Date date = new Date();
		List<Date> ds = test(date,interval);
		List<Date> ds1 = new ArrayList<Date>();
		List<Date> ds2 = new ArrayList<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		String time = sdf.format(new Date());
//		String time = "19:55:00";
		String time = getDateFormat("HH:mm:ss","60*"+need_time+"");
		Map<String, String> times = new LinkedHashMap<>();
		for (Date d : ds) {
			if (compTime(time, sdf.format(d), true)) {
				ds1.add(d);
			}
			if (compTime(time, sdf.format(d), false)) {
				ds2.add(d);
			}
		}
		sdf = new SimpleDateFormat("HH:mm");
		times.put("start_time", sdf.format(getLastElement(ds1)));
		times.put("time", sdf.format(new Date()));
		times.put("need_time", formatDateTime(parseDateTime(time,"HH:mm"),"HH:mm"));
		times.put("end_time", sdf.format(getFirstElement(ds2)));
		log.info(times);
		
		return times;
	}
    
	/**
	 * 比较两个时间 时分秒 大小
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean compTime(String s1, String s2, boolean state) {
		try {
			if (s1.indexOf(":") < 0 || s1.indexOf(":") < 0) {
				System.out.println("格式不正确");
			} else {
				String[] array1 = s1.split(":");
				int total1 = Integer.valueOf(array1[0]) * 3600 + Integer.valueOf(array1[1]) * 60
						+ Integer.valueOf(array1[2]);
				String[] array2 = s2.split(":");
				int total2 = Integer.valueOf(array2[0]) * 3600 + Integer.valueOf(array2[1]) * 60
						+ Integer.valueOf(array2[2]);
				return state ? total1 > total2 ? true : false : total1 < total2 ? true : false;
			}
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

	/**
	 * 获取list中存放的最开始一个元素
	 * 
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> T getFirstElement(List<T> list) {
		return list.get(0);
	}

	/**
	 * 获取list中存放的最后一个元素
	 * 
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> T getLastElement(List<T> list) {
		return list.get(list.size() - 1);
	}

	public static List<String> findDates(String stime, String etime) throws ParseException {
		List<String> allDate = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date dBegin = sdf.parse(stime);
		Date dEnd = sdf.parse(etime);
		allDate.add(sdf.format(dBegin));
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			allDate.add(sdf.format(calBegin.getTime()));
		}
		return allDate;
	}

	private static List<Date> test(Date date,int interval) {
		Date start = dayStartDate(date);// 转换为天的起始date
		Date nextDayDate = nextDay(start);// 下一天的date

		List<Date> result = new ArrayList<Date>();
		while (start.compareTo(nextDayDate) < 0) {
			result.add(start);
			// 日期加10分钟
			start = addFiveMin(start, interval);
		}
		return result;
	}

	private static Date addFiveMin(Date start, int offset) {
		Calendar c = Calendar.getInstance();
		c.setTime(start);
		c.add(Calendar.MINUTE, offset);
		return c.getTime();
	}

	private static Date nextDay(Date start) {
		Calendar c = Calendar.getInstance();
		c.setTime(start);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	private static Date dayStartDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * @author liuzhi
	 * @version 1.0.0
	 * @Description 获取i分钟后的时间
	 * @createTime 2021年11月29日 15:58:00
	 */
	public static String getMinute(int i) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar nowTime2 = Calendar.getInstance();
		nowTime2.add(Calendar.MINUTE, i);
		return sdf.format(nowTime2.getTime());
	}
	
    public static void main(String[] arg) throws Exception{
    	log.info(getTime("毫秒"));
//    	int date = Integer.valueOf(DateUtil.getDateFormat("dd"));
//    	log.info((date-16)*50);
//        log.info(getDateFormat("HH:mm","60*3"));
        log.info(getDateFormat("yyyy-MM-dd HH:mm:ss","60*60*24*7"));
//        log.info(getYesterDay("yyyy-MM-dd"));
//        log.info(getDateCalendar("yyyy-M-dd"));
//        
//        log.info("=========current year=========");
//        Date currentYearStart = getCurrentFirstOfYear();
//        log.info(formatDateTime(currentYearStart));
// 
//        Date currentYearEnd = getCurrentLastOfYear();
//        log.info(formatDateTime(currentYearEnd));
//
//        log.info("=========before year=========");
//        Date before = parseDateTime("2012-10-10 12:00:00");
// 
//        Date beforeYearStart = getFirstOfYear(before);
//        log.info(formatDateTime(beforeYearStart));
// 
//        Date beforeEnd = getLastOfYear(before);
//        log.info(formatDateTime(beforeEnd));
//        
//        DecimalFormat mor=new DecimalFormat("##00.##");
//        int HH_max = 37;
//        int mm_max = 73;
//        int m = Integer.parseInt(mor.format(HH_max+mm_max).substring(0,2));
//        log.info(m);
//        
//        log.info(getMinute(-3));
//        String time = getDateFormat("yyyy-MM-dd","-60*60*24");
//        log.info(time);
    }
}
