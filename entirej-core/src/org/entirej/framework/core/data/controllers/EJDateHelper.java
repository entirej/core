/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Contains helper methods which simplify the usage of the Java
 * <code>Timestamp</code>
 * <p>
 * This class is based on the <code>Timestamp</code> instead of the
 * <code>Date</code> because most applications using <code>EntireJ</code> will
 * be using a database and <code>Timestamp</code> is the class that enables the
 * JDBC API to identify a <code>Date</code> as an SQL TIMESTAMP and Calendar
 * classes
 */
public class EJDateHelper implements Serializable
{
    private Calendar _calendar = null;
    
    /**
     * Indicates that this helper should use the given <code>Locale</code> for
     * all date operations
     * 
     * @param locale
     *            The <code>Locale</code> to use
     */
    EJDateHelper(final Locale locale)
    {
        setCalendar(new GregorianCalendar(locale));
    }
    
    /**
     * Returns a new <code>Timestamp</code> that is a specified number of days
     * from a given date
     * 
     * @param inputDate
     *            The original date
     * @param amount
     *            A positive, zero or negative number of days to add to the
     *            original date
     * 
     * @return A new <code>Timestamp</code> that is a specified number of days
     *         from a given date
     */
    public Timestamp addDays(Timestamp inputDate, int amount)
    {
        _calendar.setTime(inputDate);
        _calendar.add(Calendar.DAY_OF_MONTH, amount);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Returns a new <code>Timestamp</code> that is a specified number of hours
     * from a given date
     * 
     * @param inputDate
     *            The original date to add hours to.
     * @param amount
     *            A positive, zero or negative number of hours to add to the
     *            original date.
     * 
     * @return A new date that is a specified number of hours from a given date.
     */
    public Timestamp addHours(Timestamp inputDate, int amount)
    {
        _calendar.setTime(inputDate);
        _calendar.add(Calendar.HOUR, amount);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Returns a new <code>Timestamp</code> that is a specified number of
     * milliseconds from a given date
     * 
     * @param inputDate
     *            The original date to add milliseconds to
     * @param amount
     *            A positive, zero or negative number of milliseconds to add to
     *            the original date
     * 
     * @return A new date that is a specified number of milliseconds from a
     *         given date
     */
    public Timestamp addMilliseconds(Timestamp inputDate, int amount)
    {
        _calendar.setTime(inputDate);
        _calendar.add(Calendar.MILLISECOND, amount);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Returns a new <code>Timestamp</code> that is a specified number of
     * minutes from a given date
     * 
     * @param inputDate
     *            The original date to add minutes to
     * @param amount
     *            A positive, zero or negative number of minutes to add to the
     *            original date
     * 
     * @return A new date that is a specified number of minutes from a given
     *         date
     */
    public Timestamp addMinutes(Timestamp inputDate, int amount)
    {
        _calendar.setTime(inputDate);
        _calendar.add(Calendar.MINUTE, amount);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Returns a new <code>Timestamp</code> that is a specified number of months
     * from a given date
     * 
     * @param inputDate
     *            The original date to add months to
     * @param amount
     *            A positive, zero or negative number of months to add to the
     *            original date
     * 
     * @return A new date that is a specified number of months from a given date
     */
    public Timestamp addMonths(Timestamp inputDate, int amount)
    {
        _calendar.setTime(inputDate);
        _calendar.add(Calendar.MONTH, amount);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Returns a new <code>Timestamp</code> that is a specified number of
     * seconds from a given date
     * 
     * @param inputDate
     *            The original date to add seconds to.
     * @param amount
     *            A positive, zero or negative number of seconds to add to the
     *            original date
     * 
     * @return A new date that is a specified number of seconds from a given
     *         date
     */
    public Timestamp addSecond(Timestamp inputDate, int amount)
    {
        _calendar.setTime(inputDate);
        _calendar.add(Calendar.SECOND, amount);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Returns a new <code>Timestamp</code> that is a specified number of years
     * from a given date.
     * 
     * @param inputDate
     *            The original date to add years to
     * @param amount
     *            A positive, zero or negative number of years to add to the
     *            original date
     * 
     * @return A new date that is a specified number of years from a given date
     */
    public Timestamp addYear(Date inputDate, int amount)
    {
        _calendar.setTime(inputDate);
        _calendar.add(Calendar.YEAR, amount);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Converts a <code>Timestamp</code> to a string of a specified format
     * 
     * @param inputDate
     *            The original date to convert to a string.
     * @param dateFormat
     *            The format to convert the date to. See the official Javadoc
     *            for the Java class <code>{@link SimpleDateFormat}</code> for a
     *            description of format syntax
     * 
     * @return A string representing the original date in the specified format
     */
    public String convertDateToString(Timestamp inputDate, SimpleDateFormat dateFormat)
    {
        String outputDate = null;
        if (inputDate != null && dateFormat != null)
        {
            outputDate = dateFormat.format(inputDate);
        }
        
        return outputDate;
    }
    
    /**
     * Converts a <code>Timestamp</code> to a string of a specified format
     * 
     * @param inputDate
     *            The original date to convert to a string.
     * @param dateFormat
     *            The format to convert the date to. See the official Javadoc
     *            for the Java class <code>{@link SimpleDateFormat}</code> for a
     *            description of format syntax
     * 
     * @return A string representing the original date in the specified format
     */
    public String convertDateToString(Timestamp inputDate, String dateFormat)
    {
        final SimpleDateFormat realDateFormat = new SimpleDateFormat(dateFormat);
        
        String outputDate = null;
        if (inputDate != null && realDateFormat != null)
        {
            outputDate = realDateFormat.format(inputDate);
        }
        
        return outputDate;
    }
    
    /**
     * Converts a string to a <code>Timestamp</code>
     * 
     * @param inputDate
     *            The original date in string format
     * @param dateFormat
     *            The current format of the string date. See the official
     *            Javadoc for the Java class SimpleDateFormat for a description
     *            of format syntax
     * 
     * @return A date created from the input string.
     * 
     * @exception IllegalArgumentException
     *                Input data is invalid.
     */
    public Timestamp convertStringToDate(String inputDate, String dateFormat)
    {
        final SimpleDateFormat realDateFormat = new SimpleDateFormat(dateFormat);
        Date outputDate = null;
        String newString = null;
        
        if (inputDate != null)
        {
            outputDate = realDateFormat.parse(inputDate, new ParsePosition(0));
            newString = convertDateToString(new Timestamp(outputDate.getTime()), realDateFormat);
            
            if (!newString.equals(inputDate))
            {
                IllegalArgumentException exception = new IllegalArgumentException(inputDate + "is not a valid date.");
                
                throw exception;
            }
        }
        
        return new Timestamp(outputDate.getTime());
    }
    
    /**
     * Creates a new <code>Timestamp</code> from a year, month and day
     * 
     * @param inputYear
     *            The input year
     * @param inputMonth
     *            The input month
     * @param inputDay
     *            The input day
     * 
     * @return The new date created from the input year, month and day.
     */
    public Timestamp createDate(int inputYear, int inputMonth, int inputDay)
    {
        _calendar.clear();
        _calendar.set(inputYear, inputMonth, inputDay);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Creates a new <code>Timestamp</code> from a year, month, day, hour,
     * minute and second
     * 
     * @param inputYear
     *            The input year
     * @param inputMonth
     *            The input month
     * @param inputDay
     *            The input day
     * @param inputHour
     *            The input hour of the day. This should be from 0 to 23
     * @param inputMinute
     *            The input minute
     * @param inputSecond
     *            The input second
     * 
     * @return The new date created from the input year, month, day, hour,
     *         minute and second
     */
    public Timestamp createDate(int inputYear, int inputMonth, int inputDay, int inputHour, int inputMinute, int inputSecond)
    {
        _calendar.clear();
        _calendar.set(inputYear, inputMonth, inputDay, inputHour, inputMinute, inputSecond);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Creates a new <code>Timestamp</code> from a year, month, day, hour,
     * minute, second and millisecond
     * 
     * @param inputYear
     *            The input year
     * @param inputMonth
     *            The input month
     * @param inputDay
     *            The input day
     * @param inputHour
     *            The input hour of the day. This should be from 0 to 23
     * @param inputMinute
     *            The input minute
     * @param inputSecond
     *            The input second
     * 
     * @return The new date created from the input year, month, day, hour,
     *         minute, second and millisecond
     */
    public Timestamp createDate(int inputYear, int inputMonth, int inputDay, int inputHour, int inputMinute, int inputSecond, int inputMillisecond)
    {
        _calendar.clear();
        _calendar.set(inputYear, inputMonth, inputDay, inputHour, inputMinute, inputSecond);
        _calendar.set(Calendar.MILLISECOND, inputMillisecond);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Returns the number of calendar days date2 is after date1
     * <p>
     * If date2 is before date1, then a negative number is returned. This method
     * does not take into account time. Therefore, the difference between
     * 2001-04-31 11:23:54 PM and 2001-05-01 00:01:12 AM is one day
     * 
     * @param date1
     *            The first date in the range
     * @param date2
     *            The second date in the range
     * 
     * @return The number of days date2 is after date1
     */
    public int getCalendarDaysAfter(Timestamp date1, Timestamp date2)
    {
        Timestamp newDate1 = createDate(getYear(date1), getMonth(date1), getDayOfMonth(date1));
        Timestamp newDate2 = createDate(getYear(date2), getMonth(date2), getDayOfMonth(date2));
        int daysAfter = (new Double(getDaysAfter(newDate1, newDate2))).intValue();
        
        return daysAfter;
    }
    
    /**
     * Returns the number of calendar months date2 is after date1
     * <p>
     * If date2 is before date1, then a negative number is returned. This method
     * does not take into account time or days. Therefore, the difference
     * between 2001-04-31 11:23:54 PM and 2001-05-01 00:01:12 AM is one month
     * 
     * @param date1
     *            The first date in the range
     * @param date2
     *            The second date in the range
     * 
     * @return The number of months date2 is after date1
     */
    public int getCalendarMonthsAfter(Timestamp date1, final Timestamp date2)
    {
        int month1 = getMonth(date1);
        int month2 = getMonth(date2);
        int year1 = getYear(date1);
        int year2 = getYear(date2);
        int difference = 0;
        
        if (year1 == year2)
        {
            difference = month2 - month1;
        }
        else if (year1 < year2)
        {
            int yearCtr = year2 - 1;
            
            difference = month2;
            
            while (yearCtr > year1)
            {
                difference = difference + 12;
                
                yearCtr--;
            }
            
            difference = difference + 12 - month1;
        }
        else if (year1 > year2)
        {
            int yearCtr = year1 - 1;
            
            difference = month1;
            
            while (yearCtr > year2)
            {
                difference = difference + 12;
                
                yearCtr--;
            }
            
            difference = difference + 12 - month2;
            
            difference = difference * -1;
        }
        
        return difference;
    }
    
    /**
     * Returns the number of calendar years date2 is after date1
     * <p>
     * If date2 is before date1, then a negative number is returned. This method
     * does not take into account time, days or months. Therefore, the
     * difference between 2000-12-31 11:23:54 PM and 2001-01-01 00:01:12 AM is
     * one year
     * 
     * @param date1
     *            The first date in the range
     * @param date2
     *            The second date in the range
     * 
     * @return The number of years date2 is after date1
     */
    public int getCalendarYearsAfter(Timestamp date1, Timestamp date2)
    {
        int year1 = getYear(date1);
        int year2 = getYear(date2);
        int difference = year2 - year1;
        return difference;
    }
    
    /**
     * Returns the current date and time
     * 
     * @return A date representing the current date and time
     */
    public Timestamp getCurrentDate()
    {
        final GregorianCalendar _calendar = new GregorianCalendar();
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Returns the abbreviation of the day from the input date. Abbreviations
     * are three letters in length. For example the abbreviation for Monday is
     * "Mon" and Tuesday is "Tue"
     * 
     * @param inputDate
     *            The <code>Timestamp</code> for which the day abbreviation is
     *            requested
     * 
     * @return The day abbreviation for the input date
     */
    public String getDayAbreviation(Timestamp inputDate)
    {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
        final String dayAbrv = dateFormat.format(inputDate);
        return dayAbrv;
    }
    
    /**
     * Returns the day name from the input date, such as "Monday" or "Tuesday"
     * 
     * @param inputDate
     *            The date for which the day name is requested
     * 
     * @return The day name for the input date
     */
    public String getNameOfDay(Timestamp inputDate)
    {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        final String dayName = dateFormat.format(inputDate);
        return dayName;
    }
    
    /**
     * Returns the day of month from the input date
     * 
     * @param inputDate
     *            The date for which the day of month is requested
     * 
     * @return The day of month for the input date
     */
    public int getDayOfMonth(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * Returns the day of week from the input date
     * <p>
     * The days of the week begin with 1 which represents Sunday. Monday is
     * represented by 2, Tuesday is represented by 3 and so on
     * 
     * @param inputDate
     *            The date for which the day of week is requested.
     * 
     * @return The day of week for the input date
     */
    public int getDayOfWeek(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.DAY_OF_WEEK);
    }
    
    /**
     * Returns the day of year from the input date
     * 
     * @param inputDate
     *            The date for which the day of year is requested
     * 
     * @return The day of year for the input date
     */
    public int getDayOfYear(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * Returns the number of days date2 is after date1
     * <p>
     * If date2 is before date1, then a negative number is returned. The number
     * returned is based on the number of milliseconds each date is from the
     * epoch (January 1, 1970, 00:00:00 GMT)
     * 
     * @param date1
     *            The first date in the range
     * @param date2
     *            The second date in the range
     * 
     * @return The number of days date2 is after date1
     */
    public double getDaysAfter(Timestamp date1, Timestamp date2)
    {
        final double amount1 = date1.getTime();
        final double amount2 = date2.getTime();
        final double difference = (amount2 - amount1) / 86400000;
        return difference;
    }
    
    /**
     * Returns the first day of the week. In the US, the first day of the week
     * is Sunday and in England it is Monday
     * 
     * @param inputDate
     *            The date for which the first day of the week is requested
     * 
     * @return The first day of the week
     */
    public int getFirstDayOfWeek(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.getFirstDayOfWeek();
    }
    
    /**
     * Returns the hour from the input date
     * <p>
     * This hour represents the hour of the morning or afternoon
     * 
     * @param inputDate
     *            The date for which the hour is requested
     * 
     * @return The hour from the input date
     */
    public int getHour(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.HOUR);
    }
    
    /**
     * Returns the hour of day from the input date
     * <p>
     * This hour is the same as a military time hour
     * 
     * @param inputDate
     *            The date for which the hour of day is requested
     * 
     * @return The hour of day from the input date
     */
    public int getHourOfDay(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.HOUR_OF_DAY);
    }
    
    /**
     * Returns the number of hours date2 is after date1
     * <p>
     * If date2 is before date1, then a negative number is returned. The number
     * returned is based on the number of milliseconds each date is from the
     * epoch (January 1, 1970, 00:00:00 GMT)
     * 
     * @param date1
     *            The first date in the range
     * @param date2
     *            The second date in the range
     * 
     * @return The number of hours date2 is after date1
     */
    public double getHoursAfter(Timestamp date1, Timestamp date2)
    {
        final double amount1 = date1.getTime();
        final double amount2 = date2.getTime();
        final double difference = (amount2 - amount1) / 3600000;
        return difference;
    }
    
    /**
     * This method will return a <code>Timestamp</code> which represents the
     * last date of the month in the input date
     * 
     * @param inputDate
     *            The inputDate the date to obtain the last date of month for
     * 
     * @return A <code>Timestamp</code> which represents the last date of the
     *         month
     */
    public Timestamp getLastDateOfMonth(Timestamp inputDate)
    {
        // Advance the input date to the first day of the next month.
        final Date nextMonthDate = addMonths(inputDate, 1);
        _calendar.setTime(nextMonthDate);
        _calendar.set(Calendar.DAY_OF_MONTH, 1);
        
        // Subtract one day from the first day of the next month.
        // This will get us the last date of the current month.
        final Date lastDayOfMonth = addDays(new Timestamp(_calendar.getTimeInMillis()), -1);
        
        return new Timestamp(lastDayOfMonth.getTime());
    }
    
    /**
     * Returns the millisecond from the input date.
     * 
     * @param inputDate
     *            The date for which the millisecond is requested.
     * 
     * @return The millisecond from the input date.
     */
    public int getMillisecond(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.MILLISECOND);
    }
    
    /**
     * Returns the number of milliseconds date2 is after date1
     * <p>
     * If date2 is before date1, then a negative number is returned. The number
     * returned is based on the number of milliseconds each date is from the
     * epoch (January 1, 1970, 00:00:00 GMT)
     * 
     * @param date1
     *            The first date in the range
     * @param date2
     *            The second date in the range
     * 
     * @return The number of milliseconds date2 is after date1.
     */
    public long getMillisecondsAfter(Timestamp date1, Timestamp date2)
    {
        final long amount1 = date1.getTime();
        final long amount2 = date2.getTime();
        final long difference = amount2 - amount1;
        return difference;
    }
    
    /**
     * Returns the minute from the input date
     * 
     * @param inputDate
     *            The date for which the minute is requested
     * 
     * @return The minute from the input date
     */
    public int getMinute(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.MINUTE);
    }
    
    /**
     * Returns the number of minutes date2 is after date1
     * <p>
     * If date2 is before date1, then a negative number is returned. The number
     * returned is based on the number of milliseconds each date is from the
     * epoch (January 1, 1970, 00:00:00 GMT).
     * 
     * @param date1
     *            The first date in the range
     * @param date2
     *            The second date in the range
     * 
     * @return The number of minutes date2 is after date1
     */
    public double getMinutesAfter(Timestamp date1, Timestamp date2)
    {
        final double amount1 = date1.getTime();
        final double amount2 = date2.getTime();
        final double difference = (amount2 - amount1) / 60000;
        return difference;
    }
    
    /**
     * Returns the month from the input date
     * 
     * @param inputDate
     *            The date for which the month is requested
     * 
     * @return The month from the input date
     */
    public int getMonth(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.MONTH);
    }
    
    /**
     * Returns the month abbreviation from the input date
     * <p>
     * Abbreviations are three letters in length. For example the abbreviation
     * for January is "Jan" and February is "Feb".
     * 
     * @param inputDate
     *            The date for which the month abbreviation is requested
     * 
     * @return The month abbreviation from the input date
     */
    public String getMonthAbrv(Timestamp inputDate)
    {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");
        final String monthAbrv = dateFormat.format(inputDate);
        return monthAbrv;
    }
    
    /**
     * Returns the month name from the input date, such as "January" or
     * "February"
     * 
     * @param inputDate
     *            The date for which the month name is requested
     * 
     * @return The month name from the input date
     */
    public String getMonthName(Timestamp inputDate)
    {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        final String monthName = dateFormat.format(inputDate);
        return monthName;
    }
    
    /**
     * Returns the second from the input date
     * 
     * @param inputDate
     *            The date for which the second is requested
     * 
     * @return The second from the input date
     */
    public int getSecond(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.SECOND);
    }
    
    /**
     * Returns the number of seconds date2 is after date1
     * <p>
     * If date2 is before date1, then a negative number is returned. The number
     * returned is based on the number of milliseconds each date is from the
     * epoch (January 1, 1970, 00:00:00 GMT).
     * 
     * @param date1
     *            The first date in the range
     * @param date2
     *            The second date in the range
     * 
     * @return The number of seconds date2 is after date1
     */
    public double getSecondsAfter(Timestamp date1, Timestamp date2)
    {
        final double amount1 = date1.getTime();
        final double amount2 = date2.getTime();
        final double difference = (amount2 - amount1) / 1000;
        return difference;
    }
    
    /**
     * Returns the time zone offset from the input date
     * <p>
     * The offset represents the milliseconds from Grenwich Mean Time (GMT)
     * 
     * @param inputDate
     *            The date for which the time zone offset is requested
     * 
     * @return The time zone offset from the input date
     */
    public int getTimeZoneOffset(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.ZONE_OFFSET);
    }
    
    /**
     * Returns the week of month from the input date
     * 
     * @param inputDate
     *            The date for which the week of month is requested
     * 
     * @return The week of month from the input date
     */
    public int getWeekOfMonth(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.WEEK_OF_MONTH);
    }
    
    /**
     * Returns the week of year from the input date
     * 
     * @param inputDate
     *            The date for which the week of year is requested
     * 
     * @return The week of year from the input date
     */
    public int getWeekOfYear(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * Returns the year from the input date
     * 
     * @param inputDate
     *            The date for which the year is requested
     * 
     * @return The year from the input date
     */
    public int getYear(Timestamp inputDate)
    {
        _calendar.setTime(inputDate);
        return _calendar.get(Calendar.YEAR);
    }
    
    /**
     * Indicates if date1 is after date2
     * 
     * @param date1
     *            The first date to be compared
     * @param date2
     *            The second date to be compared
     * 
     * @return True is date1 is after date2, otherwise false
     */
    public boolean isAfter(Timestamp date1, Timestamp date2)
    {
        boolean rtnValue = false;
        
        if (date1.getTime() > date2.getTime())
        {
            rtnValue = true;
        }
        
        return rtnValue;
    }
    
    /**
     * Indicates if the time in the input date is in the AM or not
     * 
     * @param inputDate
     *            The date to evaluate
     * 
     * @return True is time of date is in the AM, otherwise false
     */
    public boolean isAM(Timestamp inputDate)
    {
        boolean rtnValue;
        
        _calendar.setTime(inputDate);
        
        switch (_calendar.get(Calendar.AM_PM))
        {
            case Calendar.AM:
                rtnValue = true;
                break;
            default:
                rtnValue = false;
        }
        
        return rtnValue;
    }
    
    /**
     * Indicates if date1 is before date2
     * 
     * @param date1
     *            The first date to be compared
     * @param date2
     *            The second date to be compared
     * 
     * @return True is date1 is before date2, otherwise false
     */
    public boolean isBefore(Timestamp date1, Timestamp date2)
    {
        boolean rtnValue = false;
        
        if (date1.getTime() < date2.getTime())
        {
            rtnValue = true;
        }
        
        return rtnValue;
    }
    
    /**
     * Indicates if date1 is equal to date2
     * 
     * @param date1
     *            The first date to be compared
     * @param date2
     *            The second date to be compared
     * 
     * @return True is date1 is equal to date2, otherwise false
     */
    public boolean isEqualTo(Timestamp date1, Timestamp date2)
    {
        boolean rtnValue = false;
        
        if (date1.getTime() == date2.getTime())
        {
            rtnValue = true;
        }
        
        return rtnValue;
    }
    
    /**
     * Indicates if the year is a leap year or not
     * 
     * @param inputDate
     *            The date to evaluate
     * 
     * @return True is year is a leap year, otherwise false
     */
    public boolean isLeapYear(Timestamp inputDate)
    {
        boolean rtnValue = false;
        
        _calendar.setTime(inputDate);
        rtnValue = ((GregorianCalendar) _calendar).isLeapYear(getYear(inputDate));
        
        return rtnValue;
    }
    
    /**
     * Indicates if the time in the input date is in the PM or not
     * 
     * @param inputDate
     *            The date to evaluate
     * 
     * @return True is time of date is in the PM, otherwise false
     */
    public boolean isPM(Timestamp inputDate)
    {
        boolean rtnValue;
        
        _calendar.setTime(inputDate);
        
        switch (_calendar.get(Calendar.AM_PM))
        {
            case Calendar.PM:
                rtnValue = true;
                break;
            default:
                rtnValue = false;
        }
        
        return rtnValue;
    }
    
    /**
     * Sets the internal calendar to be used by this object
     * 
     * @param calendar
     *            The internal calendar to be used by this object
     */
    private void setCalendar(Calendar calendar)
    {
        _calendar = calendar;
    }
    
    /**
     * Sets the day of month on the input date
     * 
     * @param inputDate
     *            The date for which to set the day of month
     * @param dayOfMonth
     *            The day of month to set on the input date
     * 
     * @return The new date with the new day of month value
     */
    public Timestamp setDayOfMonth(Timestamp inputDate, int dayOfMonth)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the day of week on the input date
     * 
     * @param inputDate
     *            The date for which to set the day of week
     * @param dayOfWeek
     *            The day of week to set on the input date
     * 
     * @return The new date with the new day of week value
     */
    public Timestamp setDayOfWeek(Timestamp inputDate, int dayOfWeek)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the day of year on the input date
     * 
     * @param inputDate
     *            The date for which to set the day of year
     * @param dayOfYear
     *            The day of year to set on the input date
     * 
     * @return The new date with the new day of year value
     */
    public Timestamp setDayOfYear(Timestamp inputDate, int dayOfYear)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the hour on the input <code>Timestamp</code>
     * 
     * @param inputDate
     *            The date for which to set the hour
     * @param hour
     *            The hour to set on the input date
     * 
     * @return The new date with the new hour value
     */
    public Timestamp setHour(Timestamp inputDate, int hour)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.HOUR, hour);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the hour of day on the input <code>Timestamp</code>
     * 
     * @param inputDate
     *            The date for which to set the hour of day
     * @param hourOfDay
     *            The hour to set on the input date
     * 
     * @return The new <code>Timestamp</code> with the new hour of day value
     */
    public Timestamp setHourOfDay(Timestamp inputDate, int hourOfDay)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the millisecond on the input date
     * 
     * @param inputDate
     *            The date for which to set the millisecond
     * @param millisecond
     *            The millisecond to set on the input date
     * 
     * @return The new date with the new millisecond value
     */
    public Timestamp setMillisecond(Timestamp inputDate, int millisecond)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.MILLISECOND, millisecond);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the minute on the input date
     * 
     * @param inputDate
     *            The date for which to set the minute
     * @param minute
     *            The minute to set on the input date
     * 
     * @return The new date with the new minute value
     */
    public Timestamp setMinute(Timestamp inputDate, int minute)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.MINUTE, minute);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the month on the input date
     * 
     * @param inputDate
     *            The date for which to set the month
     * @param month
     *            The month to set on the input date
     * 
     * @return The new date with the new month value
     */
    public Timestamp setMonth(Timestamp inputDate, int month)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.MONTH, month);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the second on the input date
     * 
     * @param inputDate
     *            The date for which to set the second
     * @param second
     *            The second to set on the input date
     * 
     * @return The new date with the new second value
     */
    public Timestamp setSecond(Timestamp inputDate, int second)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.SECOND, second);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the week of month on the input date
     * 
     * @param inputDate
     *            The date for which to set the week of month
     * @param weekOfMonth
     *            The week of month to set on the input date
     * 
     * @return The new date with the new week of month value
     */
    public Timestamp setWeekOfMonth(Timestamp inputDate, int weekOfMonth)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.WEEK_OF_MONTH, weekOfMonth);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the week of year on the input date
     * 
     * @param inputDate
     *            The date for which to set the week of year
     * @param weekOfYear
     *            The week of year to set on the input date
     * 
     * @return The new date with the new week of year value
     */
    public Timestamp setWeekOfYear(Timestamp inputDate, int weekOfYear)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Sets the year on the input date
     * 
     * @param inputDate
     *            The date for which to set the year
     * @param year
     *            The month to set on the input date
     * 
     * @return The new date with the new year value
     */
    public Timestamp setYear(Timestamp inputDate, int year)
    {
        _calendar.setTime(inputDate);
        _calendar.set(Calendar.YEAR, year);
        return new Timestamp(_calendar.getTimeInMillis());
    }
    
    /**
     * Used to truncate the time from the given date
     * 
     * @param aDate
     * @return a truncated <code>Timestamp</code>
     */
    public Timestamp truncateDate(Timestamp aTimestamp)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(aTimestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Timestamp sysDateTruncated = new Timestamp(c.getTime().getTime());
        return sysDateTruncated;
    }
    
}
