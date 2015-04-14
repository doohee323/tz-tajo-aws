package utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * <pre>
 *  Usage
 *     private void testDateUtil()
 *     throws Exception
 *     {
 *     logger.debug( DateUtil.getCurrentDateString() ) ;                                        // 20140719                print
 *     logger.debug( DateUtil.getCurrentDateString(&quot;yyyy/MM/dd&quot;) ) ;                  // 2014/07/19              print
 *     logger.debug( DateUtil.getCurrentTimeString() ) ;                                        // 094837                  print
 *     logger.debug( DateUtil.getCurrentDateString(&quot;HH:mm:ss&quot;) ) ;                    // 09:48:37                print
 *     logger.debug( DateUtil.getCurrentDateString(&quot;hh:mm:ss&quot;) ) ;                    // 09:48:37                print
 *     logger.debug( DateUtil.convertFormat(&quot;20140716&quot;) ) ;                           // 2014/07/16              print
 *     logger.debug( DateUtil.convertFormat(&quot;20140716&quot;,&quot;yyyy년MM월dd일&quot;) ) ; // 2014년07월16일           print
 *     logger.debug( DateUtil.convertToTimestamp(&quot;20140717&quot;) ) ;                      // 2014-07-17 09:48:37.459 print
 *     logger.debug( DateUtil.convertToTimestampHMS(&quot;20140717123456&quot;) ) ;             // 2014-07-17 12:34:56.459 print
 * 
 *     String fromDateDash = &quot;2014-07-18&quot; ;
 *     String fromDate = &quot;20140718&quot; ;
 * 
 *     String toDateDash = &quot;2014-05-15&quot; ;
 *     String toDate = &quot;20140515&quot; ;
 * 
 *     logger.debug( DateUtil.addDays( fromDate , 3 ) ) ;                                          // 20140721                print
 *     logger.debug( DateUtil.addDays( fromDateDash , 3  , &quot;yyyy-MM-dd&quot; ) ) ;                      // 2014-07-21              print
 * 
 *     logger.debug( DateUtil.addMonths( fromDate , 3 ) ) ;                                        // 20141018                print
 *     logger.debug( DateUtil.addMonths( fromDateDash , 3  , &quot;yyyy-MM-dd&quot; ) ) ;                    // 2014-10-18              print
 * 
 *     logger.debug( DateUtil.addYears( fromDate , 3 ) ) ;                                         // 20050717                print
 *     logger.debug( DateUtil.addYears( fromDateDash , 3  , &quot;yyyy-MM-dd&quot; ) ) ;                     // 2005-07-17              print
 * 
 *     logger.debug( DateUtil.yearsBetween( fromDate , toDate ) ) ;                                // -1                      print
 *     logger.debug( DateUtil.yearsBetween( fromDateDash , toDateDash  , &quot;yyyy-MM-dd&quot; ) ) ;        // -1                      print
 * 
 *     logger.debug( DateUtil.daysBetween( fromDate , toDate ) ) ;                                 // -429                    print
 *     logger.debug( DateUtil.daysBetween( fromDateDash , toDateDash  , &quot;yyyy-MM-dd&quot; ) ) ;         // -429                    print
 * 
 *     logger.debug( DateUtil.monthsBetween( fromDate , toDate ) ) ;                               // -14                     print
 *     logger.debug( DateUtil.monthsBetween( fromDateDash , toDateDash  , &quot;yyyy-MM-dd&quot; ) ) ;       // -14                     print
 * 
 *     logger.debug( DateUtil.whichDay( fromDate  ) ) ;                                            // 5                       print
 *     logger.debug( DateUtil.whichDay( fromDateDash  , &quot;yyyy-MM-dd&quot; ) ) ;                         // 5                       print
 * 
 *     logger.debug( DateUtil.lastDayOfMonth( fromDate  ) ) ;                                      // 20140731                print
 *     logger.debug( DateUtil.lastDayOfMonth( fromDateDash  , &quot;yyyy-MM-dd&quot; ) ) ;                   // 2014-07-31              print
 * 
 *     }
 * </pre>
 */
public class DateUtil {

	/**
	 * <pre>
	 *    current Date를 yyyyMMdd format으로 return.
	 * </pre>
	 * 
	 * @return String yyyyMMdd format의 currentDate
	 * @exception Exception
	 */
	public static String getCurrentDateString() {
		return getCurrentDateString("yyyyMMdd");
	}

	/**
	 * <pre>
	 *    current time을  HHmmss format으로 return.
	 * </pre>
	 * 
	 * @return String HHmmss format의 current time
	 * @exception Exception
	 */
	public static String getCurrentTimeString() throws Exception {
		return getCurrentDateString("HHmmss");
	}

	/**
	 * <pre>
	 *    currentDate를 given pattern 에 따라 return.
	 * </pre>
	 * 
	 * @param pattern
	 *            SimpleDateFormat 에 적용할 pattern
	 * @return String pattern format의 currentDate
	 * @exception Exception
	 */
	public static String getCurrentDateString(String pattern) {
		return convertToString(getCurrentTimeStamp(), pattern);
	}

	/**
	 * <pre>
	 * java.util.Date를 given pattern의 String Type으로 convert.
	 * </pre>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(java.util.Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * <pre>
	 * yyyyMMdd format의 String을 java.util.Date Type으로 convert. convert 실패시 null을 return.
	 * </pre>
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public static java.util.Date parseDate(String yyyyMMdd) {
		if (yyyyMMdd == null || yyyyMMdd.length() != 8)
			return null;

		java.util.Date date = null;

		try {
			date = DateUtils.parseDate(yyyyMMdd, new String[] { "yyyyMMdd" });
		} catch (Exception e) {
			return null;
		}

		return date;
	}

	/**
	 * <pre>
	 *    yyyyMMdd format의 Date를 yyyy/MM/dd format으로 return.
	 * </pre>
	 * 
	 * @param dateData
	 *            yyyyMMdd format의 Date
	 * @return String yyyy/MM/dd format의 해당 Date
	 * @exception Exception
	 */
	public static String convertFormat(String dateData) throws Exception {
		return convertFormat(dateData, "yyyy/MM/dd");
	}

	/**
	 * <pre>
	 *    yyyyMMdd format의 Date를 yyyy/MM/dd format으로 return.
	 * </pre>
	 * 
	 * @param dateData
	 *            yyyyMMdd format의 Date
	 * @param format
	 *            SimpleDateFormat 에 적용할 pattern
	 * @return String pattern format의 해당 Date
	 * @exception Exception
	 */

	public static String convertFormat(String dateData, String format)
			throws Exception {

		return convertToString(convertToTimestamp(dateData), format);

	}

	/**
	 * <pre>
	 *    yyyyMMdd format의 Date를 yyyy/MM/dd format으로 return.
	 * </pre>
	 * 
	 * @return Timestamp current Timestamp 값
	 * @exception Exception
	 */

	public static Timestamp getCurrentTimeStamp() {
		try {
			Calendar cal = new GregorianCalendar();
			Timestamp result = new Timestamp(cal.getTime().getTime());
			return result;
		} catch (Exception e) {
			throw new RuntimeException("[DateUtil][getCurrentTimeStamp]"
					+ e.getMessage(), e);
		}

	}

	/**
	 * <pre>
	 *    yyyyMMdd format의 Date를 yyyy/MM/dd format으로 return.
	 * </pre>
	 * 
	 * @return Timestamp current Timestamp 값
	 * @exception Exception
	 */
	public static String getCurrentTime(String timeZone, String formant) {
		try {
			TimeZone tz;
			Date date = new Date();
			DateFormat df = new SimpleDateFormat(formant);
			tz = TimeZone.getTimeZone(timeZone);
			df.setTimeZone(tz);
			return df.format(date);
		} catch (Exception e) {
			throw new RuntimeException("[DateUtil][getCurrentTime]"
					+ e.getMessage(), e);
		}
	}

	public static String getCurrentTime(String timeZone) throws Exception {
		return getCurrentTime(timeZone, "yyyyMMddHHmmss");
	}

	public static String convertFormatU(String dateData, String toFormat)
			throws Exception {
		String yearString = dateData.substring(6, 10);
		String monthString = dateData.substring(0, 2);
		String dayString = dateData.substring(3, 5);
		String time = "000000";
		dateData = yearString + monthString + dayString + time;
		return convertToString(convertToTimestamp(dateData), toFormat);
	}

	public static String convertFormatF(String dateData, String toFormat)
			throws Exception {
	    if(dateData == null || dateData.equals("")) return "";
	    try {
	        Date myDate = new Date(dateData);
	        SimpleDateFormat sm = new SimpleDateFormat(toFormat);
	        String strDate = sm.format(myDate);
	        return strDate;
	    } catch (Exception e) {
	        return "";
	    }
	}

	/**
	 * <pre>
	 *    yyyyMMdd format의 Timestamp Date를 yyyy/MM/dd format으로 return.
	 * </pre>
	 * 
	 * @param dateData
	 *            Timestamp format의 Date
	 * @return String yyyy/MM/dd format의 Timestamp Date
	 * @exception Exception
	 */
	public static String convertToString(Timestamp dateData) throws Exception {

		return convertToString(dateData, "yyyy/MM/dd");

	}

	/**
	 * <pre>
	 *    yyyyMMdd format의 Timestamp Date를 pattern 에 따른 format으로 return.
	 * </pre>
	 * 
	 * @param dateData
	 *            Timestamp format의 Date
	 * @param pattern
	 *            SimpleDateFormat 에 적용할 pattern
	 * @return String yyyy/MM/dd format의 Timestamp Date
	 */
	public static String convertToString(Timestamp dateData, String pattern) {
		return convertToString(dateData, pattern, java.util.Locale.US);
	}

	/**
	 * <pre>
	 *    yyyyMMdd format의 Timestamp Date를 pattern 과 locale  에 따른 format으로 return.
	 * 
	 * </pre>
	 * 
	 * @param dateData
	 *            Timestamp format의 Date
	 * @param pattern
	 *            SimpleDateFormat 에 적용할 pattern
	 * @param locale
	 *            국가별 LOCALE
	 * @return String pattern format의 Timestamp Date
	 * @exception Exception
	 */
	public static String convertToString(Timestamp dateData, String pattern,
			java.util.Locale locale) {
		try {

			if (dateData == null) {
				return null;
			}

			SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
			// formatter.applyPattern( pattern );

			return formatter.format(dateData);
		} catch (Exception e) {
			throw new RuntimeException("[DateUtil][convertToString]"
					+ e.getMessage(), e);
		}

	}

	/**
	 * <pre>
	 *    yyyyMMdd format의  Date를 Timestamp 로  return.
	 * </pre>
	 * 
	 * @param dateData
	 *            yyyyMMdd format의 Date
	 * @return Timestamp format의 해당 Date
	 * @exception Exception
	 */
	public static Timestamp convertToTimestamp(String dateData) {

		try {

			if (dateData == null)
				return null;
			if (dateData.trim().equals(""))
				return null;

			int dateObjLength = dateData.length();

			String yearString = "2014";
			String monthString = "01";
			String dayString = "01";

			if (dateObjLength >= 4) {
				yearString = dateData.substring(0, 4);
			}
			if (dateObjLength >= 6) {
				monthString = dateData.substring(4, 6);
			}
			if (dateObjLength >= 8) {
				dayString = dateData.substring(6, 8);
			}

			int year = Integer.parseInt(yearString);
			int month = Integer.parseInt(monthString) - 1;
			int day = Integer.parseInt(dayString);

			Calendar cal = new GregorianCalendar();
			cal.set(year, month, day);
			// cal.getTime();
			return new Timestamp(cal.getTime().getTime());

		} catch (Exception e) {
			throw new RuntimeException("[DateUtil][convertToTimestamp]"
					+ e.getMessage(), e);
		}

	}

	/**
	 * <pre>
	 *    yyyyMMddHHmmss format의  Datetime을 Timestamp 로  return.
	 * </pre>
	 * 
	 * @param dateData
	 *            yyyyMMddHHmmss format의 Datetime
	 * @return Timestamp format의 해당 Datetime
	 * @exception Exception
	 */
	public static Timestamp convertToTimestampHMS(String dateData)
			throws Exception {
		try {

			if (dateData == null)
				return null;
			if (dateData.trim().equals(""))
				return null;

			String yearString = dateData.substring(0, 4);
			String monthString = dateData.substring(4, 6);
			String dayString = dateData.substring(6, 8);
			String hourString = dateData.substring(8, 10);
			String minString = dateData.substring(10, 12);
			String secString = dateData.substring(12, 14);

			int year = Integer.parseInt(yearString);
			int month = Integer.parseInt(monthString) - 1;
			int day = Integer.parseInt(dayString);
			int hour = Integer.parseInt(hourString);
			int min = Integer.parseInt(minString);
			int sec = Integer.parseInt(secString);

			Calendar cal = new GregorianCalendar();
			cal.set(year, month, day, hour, min, sec);

			return new Timestamp(cal.getTime().getTime());

		} catch (Exception e) {
			throw new Exception("[DateUtil][convertToTimestampHMS]"
					+ e.getMessage(), e);
		}

	}

	/**
	 * <pre>
	 * check date string validation with an user defined format.
	 * </pre>
	 * 
	 * @param s
	 *            date string you want to check.
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return date java.util.Date
	 */
	private static java.util.Date check(String s, String format)
			throws java.text.ParseException {
		if (s == null)
			throw new java.text.ParseException("date string to check is null",
					0);
		if (format == null)
			throw new java.text.ParseException(
					"format string to check date is null", 0);

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				format, java.util.Locale.US);
		java.util.Date date = null;
		try {
			date = formatter.parse(s);
		} catch (java.text.ParseException e) {
			/*
			 * throw new java.text.ParseException( e.getMessage() +
			 * " with format \"" + format + "\"", e.getErrorOffset() );
			 */
			throw new java.text.ParseException(" wrong date:\"" + s
					+ "\" with format \"" + format + "\"", 0);
		}

		// if (!formatter.format(date).equals(s))
		// throw new java.text.ParseException("Out of bound date:\"" + s +
		// "\" with format \"" + format + "\"", 0);
		return date;
	}

	/**
	 * <pre>
	 * check date string validation with the default format "yyyyMMdd".
	 * </pre>
	 * 
	 * @param s
	 *            date string you want to check with default format "yyyyMMdd"
	 * @return boolean true Date format이 맞고, existing Date일 때 false Date format이
	 *         맞지 않거나, 존재하지 않는 Date일 때
	 * @exception Exception
	 */
	public static boolean isValid(String s) throws Exception {
		return DateUtil.isValid(s, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * check date string validation with an user defined format.
	 * </pre>
	 * 
	 * @param s
	 *            date string you want to check.
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return boolean true Date format이 맞고, existing Date일 때 false Date format이
	 *         맞지 않거나, 존재하지 않는 Date일 때
	 * @exception Exception
	 */
	public static boolean isValid(String s, String format) throws Exception {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = null;
			try {
				date = formatter.parse(s);
			} catch (java.text.ParseException e) {
				return false;
			}

			if (!formatter.format(date).equals(s))
				return false;

			return true;

		} catch (Exception e) {
			throw new Exception("[DateUtil][isValid]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * return days between two date strings with default defined
	 * format.(yyyyMMdd)
	 * </pre>
	 * 
	 * @param s
	 *            date string you want to check.
	 * @return int Date format이 맞고, existing Date일 때 요일을 return format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생 0: 일요일
	 *         (java.util.Calendar.SUNDAY 와 비교) 1: 월요일
	 *         (java.util.Calendar.MONDAY 와 비교) 2: 화요일
	 *         (java.util.Calendar.TUESDAY 와 비교) 3: 수요일
	 *         (java.util.Calendar.WENDESDAY 와 비교) 4: 목요일
	 *         (java.util.Calendar.THURSDAY 와 비교) 5: 금요일
	 *         (java.util.Calendar.FRIDAY 와 비교) 6: 토요일
	 *         (java.util.Calendar.SATURDAY 와 비교) 예) String s = "20000529"; int
	 *         dayOfWeek = whichDay(s, format); if (dayOfWeek ==
	 *         java.util.Calendar.MONDAY) logger.debug(" 월요일: " + dayOfWeek); if
	 *         (dayOfWeek == java.util.Calendar.TUESDAY) logger.debug(" 화요일: " +
	 *         dayOfWeek);
	 * @exception Exception
	 */
	public static int whichDay(String s) throws Exception {
		return whichDay(s, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * return days between two date strings with user defined format.
	 * </pre>
	 * 
	 * @param s
	 *            date string you want to check.
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return int Date format이 맞고, existing Date일 때 요일을 return format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생 0: 일요일
	 *         (java.util.Calendar.SUNDAY 와 비교) 1: 월요일
	 *         (java.util.Calendar.MONDAY 와 비교) 2: 화요일
	 *         (java.util.Calendar.TUESDAY 와 비교) 3: 수요일
	 *         (java.util.Calendar.WENDESDAY 와 비교) 4: 목요일
	 *         (java.util.Calendar.THURSDAY 와 비교) 5: 금요일
	 *         (java.util.Calendar.FRIDAY 와 비교) 6: 토요일
	 *         (java.util.Calendar.SATURDAY 와 비교) 예) String s = "2000-05-29";
	 *         int dayOfWeek = whichDay(s, "yyyy-MM-dd"); if (dayOfWeek ==
	 *         java.util.Calendar.MONDAY) logger.debug(" 월요일: " + dayOfWeek); if
	 *         (dayOfWeek == java.util.Calendar.TUESDAY) logger.debug(" 화요일: " +
	 *         dayOfWeek);
	 * @exception Exception
	 */
	public static int whichDay(String s, String format) throws Exception {
		return whichDay(s, format, java.util.Locale.US);
	}

	/**
	 * <pre>
	 * whichDay
	 * </pre>
	 * 
	 * @param s
	 * @param format
	 * @param locale
	 */
	public static int whichDay(String s, String format, Locale locale)
			throws Exception {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, locale);
			java.util.Date date = check(s, format);

			java.util.Calendar calendar = formatter.getCalendar();
			calendar.setTime(date);
			return calendar.get(java.util.Calendar.DAY_OF_WEEK);
		} catch (Exception e) {
			throw new Exception("[DateUtil][whichDay]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * return days between two date strings with default defined
	 * format.("yyyyMMdd")
	 * </pre>
	 * 
	 * @param String
	 *            from date string
	 * @param String
	 *            to date string
	 * @return int Date format이 맞고, existing Date일 때 2개 일자 사이의 나이 return format이
	 *         잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static int daysBetween(String from, String to) throws Exception {
		return daysBetween(from, to, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * return days between two date strings with user defined format.
	 * </pre>
	 * 
	 * @param String
	 *            from date string
	 * @param String
	 *            to date string
	 * @return int Date format이 맞고, existing Date일 때 2개 일자 사이의 일자 return format이
	 *         잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static int daysBetween(String from, String to, String format)
			throws Exception {
		try {

			java.util.Date d1 = check(from, format);
			java.util.Date d2 = check(to, format);

			long duration = d2.getTime() - d1.getTime();

			return (int) (duration / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			throw new Exception("[DateUtil][daysBetween]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * return years between two date strings with default defined
	 * format.("yyyyMMdd")
	 * </pre>
	 * 
	 * @param String
	 *            from date string
	 * @param String
	 *            to date string
	 * @return int Date format이 맞고, existing Date일 때 2개 일자 사이의 나이 return format이
	 *         잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static int yearsBetween(String from, String to) throws Exception {
		return yearsBetween(from, to, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * return years between two date strings with user defined format.
	 * </pre>
	 * 
	 * @param String
	 *            from date string
	 * @param String
	 *            to date string
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return int Date format이 맞고, existing Date일 때 2개 일자 사이의 나이 return format이
	 *         잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static int yearsBetween(String from, String to, String format)
			throws Exception {
		return (int) (daysBetween(from, to, format) / 365);
	}

	/**
	 * <pre>
	 * addHour
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param int 더할 분수
	 * @return String Date format이 맞고, existing Date일 때 분수 더하기 format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addHour(String s, int hour) throws Exception {
		return addHour(s, hour, "yyyyMMddHH");
	}

	/**
	 * <pre>
	 * addHour
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param String
	 *            더할 분수
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return int Date format이 맞고, existing Date일 때 분수 더하기 format이 잘못 되었거나 존재하지
	 *         않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addHour(String s, int hour, String format)
			throws Exception {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = check(s, format);

			date.setTime(date.getTime() + ((long) hour * 1000 * 60 * 60));
			return formatter.format(date);
		} catch (Exception e) {
			throw new Exception("[DateUtil][addHour]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * addMinute
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param int 더할 분수
	 * @return String Date format이 맞고, existing Date일 때 분수 더하기 format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addMinute(String s, int minute) throws Exception {
		return addMinute(s, minute, "yyyyMMddHHmm");
	}

	/**
	 * <pre>
	 * addMinute
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param String
	 *            더할 분수
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return int Date format이 맞고, existing Date일 때 분수 더하기 format이 잘못 되었거나 존재하지
	 *         않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addMinute(String s, int minute, String format)
			throws Exception {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = check(s, format);

			date.setTime(date.getTime() + ((long) minute * 1000 * 60));
			return formatter.format(date);
		} catch (Exception e) {
			throw new Exception("[DateUtil][addMinute]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * addSecond
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param int 더할 초수
	 * @return String Date format이 맞고, existing Date일 때 초수 더하기 format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addSecond(String s, int second) throws Exception {
		return addSecond(s, second, "yyyyMMddHHmmss");
	}

	/**
	 * <pre>
	 * addSecond
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param String
	 *            더할 초수
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return int Date format이 맞고, existing Date일 때 초수 더하기 format이 잘못 되었거나 존재하지
	 *         않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addSecond(String s, int second, String format)
			throws Exception {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = check(s, format);

			date.setTime(date.getTime() + ((long) second * 1000));
			return formatter.format(date);
		} catch (Exception e) {
			throw new Exception("[DateUtil][addSecond]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * addMilliSecond
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param int 더할 millisecond
	 * @return String Date format이 맞고, existing Date일 때 초수 더하기 format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addMilliSecond(String s, int milliSecond)
			throws Exception {
		return addMilliSecond(s, milliSecond, "yyyyMMddHHmmss");
	}

	/**
	 * <pre>
	 * addMilliSecond
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param String
	 *            더할 millisecond
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return int Date format이 맞고, existing Date일 때 초수 더하기 format이 잘못 되었거나 존재하지
	 *         않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addMilliSecond(String s, int milliSecond, String format)
			throws Exception {
		try {

			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = check(s, format);

			date.setTime(date.getTime() + ((long) milliSecond));
			return formatter.format(date);
		} catch (Exception e) {
			throw new Exception("[DateUtil][addMilliSecond]" + e.getMessage(),
					e);
		}
	}

	/**
	 * <pre>
	 * return add day to date strings
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param int 더할 date number
	 * @return String Date format이 맞고, existing Date일 때 date number 더하기 format이
	 *         잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addDays(String s, int day) throws Exception {
		return addDays(s, day, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * return add day to date strings with user defined format.
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param String
	 *            더할 date number
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return int Date format이 맞고, existing Date일 때 date number 더하기 format이 잘못
	 *         되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addDays(String s, int day, String format)
			throws Exception {
		try {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = check(s, format);

			date.setTime(date.getTime() + ((long) day * 1000 * 60 * 60 * 24));
			return formatter.format(date);
		} catch (Exception e) {
			throw new Exception("[DateUtil][addDays]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * return add month to date strings
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param int 더할 월수
	 * @return String Date format이 맞고, existing Date일 때 월수 더하기 format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addMonths(String s, int month) throws Exception {
		return addMonths(s, month, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * return add month to date strings with user defined format.
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param int 더할 월수
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return String Date format이 맞고, existing Date일 때 월수 더하기 format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addMonths(String s, int addMonth, String format)
			throws Exception {
		try {

			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = check(s, format);

			java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat(
					"yyyy", java.util.Locale.US);
			java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat(
					"MM", java.util.Locale.US);
			java.text.SimpleDateFormat dayFormat = new java.text.SimpleDateFormat(
					"dd", java.util.Locale.US);
			int year = Integer.parseInt(yearFormat.format(date));
			int month = Integer.parseInt(monthFormat.format(date));
			int day = Integer.parseInt(dayFormat.format(date));

			month += addMonth;
			if (addMonth > 0) {
				while (month > 12) {
					month -= 12;
					year += 1;
				}
			} else {
				while (month <= 0) {
					month += 12;
					year -= 1;
				}
			}
			java.text.DecimalFormat fourDf = new java.text.DecimalFormat("0000");
			java.text.DecimalFormat twoDf = new java.text.DecimalFormat("00");
			String tempDate = String.valueOf(fourDf.format(year))
					+ String.valueOf(twoDf.format(month))
					+ String.valueOf(twoDf.format(day));
			java.util.Date targetDate = null;

			try {
				targetDate = check(tempDate, "yyyyMMdd");
			} catch (java.text.ParseException pe) {
				day = lastDay(year, month);
				tempDate = String.valueOf(fourDf.format(year))
						+ String.valueOf(twoDf.format(month))
						+ String.valueOf(twoDf.format(day));
				targetDate = check(tempDate, "yyyyMMdd");
			}

			return formatter.format(targetDate);
		} catch (Exception e) {
			throw new Exception("[DateUtil][addMonths]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * return add year to date strings
	 * </pre>
	 * 
	 * @param String
	 *            s string
	 * @param int 더할 년수
	 * @return String Date format이 맞고, existing Date일 때 년수 더하기 format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */

	public static String addYears(String s, int year) throws Exception {
		return addYears(s, year, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * return add year to date strings with user defined format.
	 * </pre>
	 * 
	 * @param String
	 *            date string
	 * @param int 더할 년수
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return String Date format이 맞고, existing Date일 때 년수 더하기 format이 잘못 되었거나
	 *         존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String addYears(String s, int year, String format)
			throws Exception {
		try {

			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = check(s, format);
			date.setTime(date.getTime()
					+ ((long) year * 1000 * 60 * 60 * 24 * (365)));
			return formatter.format(date);
		} catch (Exception e) {
			throw new Exception("[DateUtil][addYears]" + e.getMessage(), e);
		}
	}

	// 입력된 시간으로부터 추가된 시간 계산 (day, hour, minute)
	// getAddedTime("200706290225", "minut", 2);
	// getAddedTime("2007062902", "hour", 1);
	public static String getAddedTime(String currentTime, String tTime, int nAdd) {
		java.util.Date current = new java.util.Date();
		java.text.SimpleDateFormat formatter = null;
		try {
			// 1일 추가 => 1일 : 24시간, 1시간 : 60분, 1분 : 60초 => ((long) nAdd * 1000 *
			// 60 * 60 * 24)
			// 1분 추가 => ((long) nAdd * 1000 * 60)
			if (tTime.equals("day")) {
				formatter = new java.text.SimpleDateFormat("yyyyMMdd",
						java.util.Locale.US);
				current = formatter.parse(currentTime);
				current.setTime(current.getTime()
						+ ((long) nAdd * 1000 * 60 * 60 * 24));
			} else if (tTime.equals("hour")) {
				formatter = new java.text.SimpleDateFormat("yyyyMMddHH",
						java.util.Locale.US);
				current = formatter.parse(currentTime);
				current.setTime(current.getTime()
						+ ((long) nAdd * 1000 * 60 * 60));
			} else if (tTime.equals("minute")) {
				formatter = new java.text.SimpleDateFormat("yyyyMMddHHmm",
						java.util.Locale.US);
				current = formatter.parse(currentTime);
				current.setTime(current.getTime() + ((long) nAdd * 1000 * 60));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatter.format(current);
	}

	/**
	 * <pre>
	 * return months between two date strings
	 * </pre>
	 * 
	 * @param String
	 *            from date string
	 * @param String
	 *            to date string
	 * @return int Date format이 맞고, existing Date일 때 2개 일자 사이의 개월수 return
	 *         format이 잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static int monthsBetween(String from, String to) throws Exception {
		return monthsBetween(from, to, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * return months between two date strings with user defined format.
	 * </pre>
	 * 
	 * @param String
	 *            from date string
	 * @param String
	 *            to date string
	 * @return int Date format이 맞고, existing Date일 때 2개 일자 사이의개월수 return format이
	 *         잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static int monthsBetween(String from, String to, String format)
			throws Exception {
		try {

			java.util.Date fromDate = check(from, format);
			java.util.Date toDate = check(to, format);

			if (fromDate.compareTo(toDate) == 0)
				return 0;

			java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat(
					"yyyy", java.util.Locale.US);
			java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat(
					"MM", java.util.Locale.US);
			java.text.SimpleDateFormat dayFormat = new java.text.SimpleDateFormat(
					"dd", java.util.Locale.US);

			int fromYear = Integer.parseInt(yearFormat.format(fromDate));
			int toYear = Integer.parseInt(yearFormat.format(toDate));
			int fromMonth = Integer.parseInt(monthFormat.format(fromDate));
			int toMonth = Integer.parseInt(monthFormat.format(toDate));
			int fromDay = Integer.parseInt(dayFormat.format(fromDate));
			int toDay = Integer.parseInt(dayFormat.format(toDate));

			int result = 0;
			result += ((toYear - fromYear) * 12);
			result += (toMonth - fromMonth);

			if (((toDay - fromDay) > 0))
				result += toDate.compareTo(fromDate);

			return result;
		} catch (Exception e) {
			throw new Exception("[DateUtil][monthsBetween]" + e.getMessage(), e);
		}
	}

	/**
	 * <pre>
	 * the month's last date을 return
	 * </pre>
	 * 
	 * @param String
	 *            src string
	 * @return String Date format이 맞고, existing Date일 때 the month's last date을
	 *         return format이 잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String lastDayOfMonth(String src) throws Exception {
		return lastDayOfMonth(src, "yyyyMMdd");
	}

	/**
	 * <pre>
	 * the month's last date을 return
	 * </pre>
	 * 
	 * @param format
	 *            string representation of the date format. For example,
	 *            "yyyy-MM-dd".
	 * @return String Date format이 맞고, existing Date일 때 the month's last date을
	 *         return format이 잘못 되었거나 존재하지 않는 Date: java.text.ParseException 발생
	 * @exception Exception
	 */
	public static String lastDayOfMonth(String src, String format)
			throws Exception {
		try {

			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					format, java.util.Locale.US);
			java.util.Date date = check(src, format);

			java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat(
					"yyyy", java.util.Locale.US);
			java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat(
					"MM", java.util.Locale.US);

			int year = Integer.parseInt(yearFormat.format(date));
			int month = Integer.parseInt(monthFormat.format(date));
			int day = lastDay(year, month);

			java.text.DecimalFormat fourDf = new java.text.DecimalFormat("0000");
			java.text.DecimalFormat twoDf = new java.text.DecimalFormat("00");
			String tempDate = String.valueOf(fourDf.format(year))
					+ String.valueOf(twoDf.format(month))
					+ String.valueOf(twoDf.format(day));

			java.util.Date targetDate = check(tempDate, "yyyyMMdd");

			return formatter.format(targetDate);
		} catch (Exception e) {
			throw new Exception("[DateUtil][lastDayOfMonth]" + e.getMessage(),
					e);
		}
	}

	/**
	 * <pre>
	 * int lastDay
	 * </pre>
	 * 
	 * @param year
	 * @param month
	 * @throws java.text.ParseException
	 */
	private static int lastDay(int year, int month)
			throws java.text.ParseException {
		int day = 0;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			if ((year % 4) == 0) {
				if ((year % 100) == 0 && (year % 400) != 0) {
					day = 28;
				} else {
					day = 29;
				}
			} else {
				day = 28;
			}
			break;
		default:
			day = 30;
		}
		return day;
	}

	/**
	 * <pre>
	 * first parameter와 second parameter를 비교하여 first parameter가 second parameter 보다 이전 Date인지 비교하는 메소드
	 * </pre>
	 * 
	 * ex) f = 20140203 s=20140205 -> true return
	 * 
	 * @param f
	 *            yyyyMMdd format의 Date
	 * @param s
	 *            yyyyMMdd format의 Date
	 * @return boolean
	 */
	public static boolean isPreviousDate(Timestamp f, Timestamp s) {
		return s.getTime() - f.getTime() >= 0;
	}

	/**
	 * <pre>
	 * first parameter와 second parameter를 비교하여 first parameter가 second parameter 보다 이전 Date인지 비교하는 메소드
	 * </pre>
	 * 
	 * ex) f = 20140203 s=20140205 -> true return
	 * 
	 * @param f
	 *            yyyyMMdd format의 Date
	 * @param s
	 *            yyyyMMdd format의 Date
	 * @return boolean
	 */
	public static boolean isPreviousDate(String f, String s)
			throws java.text.ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date firstDate = formatter.parse(f);
		Date secondDate = formatter.parse(s);

		return secondDate.getTime() - firstDate.getTime() >= 0;
	}

	/**
	 * <pre>
	 * given format Date로 원하는 format의 Date를 return.
	 * </pre>
	 * 
	 * @param dateStr
	 *            - formated Date
	 * @param dateFormat
	 *            - format
	 * @param returnFormat
	 *            - return받고자하는 format
	 * @return
	 */
	public static String getDateFormatedString(String dateStr,
			String dateFormat, String returnFormat) throws Exception {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat,
				java.util.Locale.US);
		SimpleDateFormat returnFormatter = new SimpleDateFormat(returnFormat,
				java.util.Locale.US);
		date = formatter.parse(dateStr);
		return returnFormatter.format(date);
	}

	/**
	 * <pre>
	 * 두 시간의 갭을 return
	 * </pre>
	 * 
	 * @param f
	 *            yyyyMMdd format의 Date
	 * @param s
	 *            yyyyMMdd format의 Date
	 * @return boolean
	 */
	public static float getTimeGap(String strStartTime, String strEndTime) {
		String sPad = "0000000000000000000";
		if (strStartTime.length() > strEndTime.length()) {
			strEndTime = strEndTime
					+ sPad.substring(0,
							strStartTime.length() - strEndTime.length());
		}
		if (strStartTime.length() < strEndTime.length()) {
			strStartTime = strStartTime
					+ sPad.substring(0,
							strEndTime.length() - strStartTime.length());
		}
		long startTime = Long.parseLong(strStartTime);
		long endTime = Long.parseLong(strEndTime);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(endTime);
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(startTime);
		float frslt = (endTime - startTime) / 1000.0f;
		return frslt;
	}

	// CRONTAB 시간 표현으로 변경 (day, hour, minute)
	public static String getCrontabTime(String timeType, String toTime) {
		String convertTime = "";

		if (timeType.equals("minute")) {
			convertTime = "*/" + toTime + " *" + " *" + " *";
		} else if (timeType.equals("hour")) {
			convertTime = "*" + " " + "*/" + toTime + " *" + " *";
		} else if (timeType.equals("day")) {
			convertTime = "*" + " *" + " " + "*/" + toTime + " *";
		}
		convertTime += " *";
		return convertTime;
	}

	/**
	 * <pre>
	 * given format을 SimpleDateFormat의 문법에 입각하여 formatting한다.<BR>
	 * </pre>
	 * 
	 * @param String
	 *            format SimpleDateFormat의 문법에 맞는 format string
	 * @return given format이 적용된 date string
	 */
	public static String getDateFormat(String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format,
				java.util.Locale.US);
		return dateFormat.format(new java.util.Date());
	}

	/**
	 * input date를 Date object return.
	 * 
	 * @param year
	 *            - 년
	 * @param month
	 *            - 월
	 * @param date
	 *            - 일
	 * @return Date - the date에 해당하는 Date
	 */
	public static Date createDate(int year, int month, int date) {
		return createCalendar(year, month, date).getTime();
	}

	/**
	 * input date를 Calendar object return.
	 * 
	 * @param year
	 *            - 년
	 * @param month
	 *            - 월
	 * @param date
	 *            - 일
	 * @return Calendar - the date에 해당하는 Calendar
	 */
	public static Calendar createCalendar(int year, int month, int date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(year, month - 1, date);
		return calendar;
	}

}
