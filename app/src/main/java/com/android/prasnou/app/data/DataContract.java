package com.android.prasnou.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Dzianis_Prasnou on 8/29/2016.
 */
public class DataContract {
    public static final String CONTENT_AUTHORITY = "com.android.prasnou.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WORKOUT = "workout";
    public static final String PATH_WORKOUT_SET = "workout_set";

    // all dates normalize to the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Workout table
     */
    public static final class WorkoutEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUT).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT;

        // table
        public static final String TABLE_NAME = "wrk";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_WRK_TYPE_ID = "wrk_type_id";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_NOTES = "notes";

        public static Uri buildWrkUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /// todo other tables


}
