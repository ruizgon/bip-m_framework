/* 
 * Copyright (C) 2016 BIP-M Framework.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package system.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Gonzalo
 */
public class DateManager {

    /**
     * Diferencia entre FILTETME_EPOCH y UNIX_EPOCH
     */
    private final static long FILETIME_EPOCH_DIFFERENCE = 11644473600000L; //+ 24 * 60 * 60 * 1000;

    public static String getActualDate(String format, String hourFormat) {
        StringBuilder dateString = new StringBuilder("");

        try {
            Calendar calendar = new GregorianCalendar();
            Date date = calendar.getTime();
            SimpleDateFormat dt = new SimpleDateFormat(format);
            dateString.append(dt.format(date));
            dateString.append("T");
            dt = new SimpleDateFormat(hourFormat);
            dateString.append(dt.format(date));
        } catch (Exception ex) {

        }

        return dateString.toString();
    }
    
    public static String getActualDate() {
        StringBuilder dateString = new StringBuilder("");

        try {
            Calendar calendar = new GregorianCalendar();
            Date date = calendar.getTime();
            SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
            dateString.append(dt.format(date));
            dateString.append("T");
            dt = new SimpleDateFormat("hhmmssSSSz");
            dateString.append(dt.format(date));
        } catch (Exception ex) {

        }

        return dateString.toString();
    }
    
    public static String getActualDateWithISOFormat() {
        StringBuilder dateString = new StringBuilder("");

        try {
            Calendar calendar = new GregorianCalendar();
            Date date = calendar.getTime();
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            dateString.append(dt.format(date));
            dateString.append("T");
            dt = new SimpleDateFormat("hh:mm:ss.SSSz");
            dateString.append(dt.format(date));
        } catch (Exception ex) {

        }

        return dateString.toString();
    }

    public static Date hexToDateTime(String hex) {
        /*String s1 = hex;    //2011-12-8,22:13:30.0
         String year = s1.substring(0, 2);
         String month = s1.substring(2, 4);
         String day = s1.substring(4, 6);
         String hour = s1.substring(6, 8);
         //String minute = s1.substring(10, 12);
         Calendar cal = Calendar.getInstance();
         cal.set(Calendar.YEAR, Integer.parseInt(year, 16));
         cal.set(Calendar.MONTH, Integer.parseInt(month, 16));
         cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day, 16));
         cal.set(Calendar.HOUR, Integer.parseInt(hour, 16));
         //cal.set(Calendar.MINUTE, Integer.parseInt(minute, 16));
         //System.out.println(cal.getTime());
         return cal.getTime();
         */
        Date dateReturn = null;

        try {

            long timeStamp = Conversor.hexToLong(hex);
            long milliseconds = (timeStamp / 10000) - FILETIME_EPOCH_DIFFERENCE;
            dateReturn = new Date(milliseconds);
        } catch (Exception ex) {

        }
        return dateReturn;
    }

    public static String hexToDateTimeStringFormat(String hex, String formatStr) {
        Date dateReturn = null;
        String dateStr = "";
        
        try {

            long timeStamp = Conversor.hexToLong(hex);
            long milliseconds = (timeStamp / 10000) - FILETIME_EPOCH_DIFFERENCE;
            dateReturn = new Date(milliseconds);
            Format format = new SimpleDateFormat(formatStr);
            dateStr = format.format(dateReturn);
        } catch (Exception ex) {

        }
        return dateStr;
    }
}
