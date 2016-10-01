package com.android.prasnou.app;

import android.widget.Adapter;

import java.util.Date;

/**
 * Created by Black on 9/7/2016.
 */
public class Utils {
    private static final String DATE_FORMAT_MED = "MM/dd/yyyy EE";
    private static final String TIME_FORMAT = "hhh:mmm";

    /* date to String MM/DD/YYYY dd */
    public static String formatDate(long dateInMillis) {

        Date date = new Date(dateInMillis);
        StringBuilder sb = new StringBuilder(
                android.text.format.DateFormat.format(DATE_FORMAT_MED, date)
                    .subSequence(0,DATE_FORMAT_MED.length()));
        return sb.toString();
    }

    public static String wrkResult(long durInMillis, long totalWeight){
        return "Time: 1h:20m  Weight: 2300lb"; // todo
    }

    /* find item position by id */
    public static int getAdapterItemPositionById(Adapter adapter, long id){

        for (int i = 0; i < adapter.getCount(); i++) {
            if (id == adapter.getItemId(i)) {
                return i;
            }
        }

        return -1;
    }
}
