/*
 * Created on Wed Apr 07 2021
 *
 * The Unlicense
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public
 * domain. We make this dedication for the benefit of the public at large and to
 * the detriment of our heirs and successors. We intend this dedication to be an
 * overt act of relinquishment in perpetuity of all present and future rights to
 * this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package uc.seng301.eventapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to handle (stringified) date objects.
 */
public class DateUtil {

  private static DateUtil instance;

  /**
   * Default date format
   */
  private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
  /**
   * Current date to fake a clock
   */
  private Date currentDate;

  /**
   * Singleton (aka unique instance) => hide constructor
   */
  private DateUtil() {
    currentDate = new Date();
  }

  /**
   * Retrieve a unique instance of a DateUtil (thread-safe)
   * 
   * @return this unique instance
   */
  public static synchronized DateUtil getInstance() {
    if (null == instance) {
      instance = new DateUtil();
    }
    return instance;
  }

  /**
   * Get the default date format (same as for the clock)
   * 
   * @return the default format for dates
   */
  public String getDefaultDateFormat() {
    return DEFAULT_DATE_FORMAT;
  }

  /**
   * Get the current date (value)
   * 
   * @return the current date for this simplified clock
   */
  public Date getCurrentDate() {
    return (Date) currentDate.clone();
  }

  /**
   * Update this instance current date to new date (thread safe)
   * 
   * @param newDate the new date (using {@link #getDefaultDateFormat})
   * @return true if the date could be updated to given date, false otherwise
   *         (e.g., wrong format)
   */
  public synchronized boolean changeCurrentDate(String newDate) {
    Date newCurrentDate = convertToDate(newDate);
    if (null != newCurrentDate) {
      currentDate = newCurrentDate;
      return true;
    }
    return false;
  }

  /**
   * Convert given string date to a date object using default
   * {@link DateUtil.DATE_FORMAT}
   * 
   * @param date a date compliant to given format
   * @return the date object corresponding to given date using default format,
   *         null if the given date can't be parsed acccording to default format
   */
  public Date convertToDate(String date) {
    return convertToDate(DEFAULT_DATE_FORMAT, date);
  }

  /**
   * Convert given string date in given format to a date object
   * 
   * @param format a {@link SimpleDateFormat} string format
   * @param date   a date compliant to given format
   * @return the date object corresponding to given date in given format, null if
   *         the given date can't be parsed acccording to given format
   */
  public Date convertToDate(String format, String date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    try {
      return dateFormat.parse(date);
    } catch (ParseException e) {
      return null;
    }
  }
}
