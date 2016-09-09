package com.android.prasnou.app;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Black on 9/7/2016.
 */
public class Utils {
    private static final String DATE_FORMAT_MED = "MM/dd/yyyy EE";

    /* date to String MM/DD/YYYY dd */
    public static String formatDate(long dateInMillis) {

        Date date = new Date(dateInMillis);
        StringBuilder sb = new StringBuilder(
                android.text.format.DateFormat.format(DATE_FORMAT_MED, date)
                    .subSequence(0,DATE_FORMAT_MED.length()));
        return sb.toString();
    }
}