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
    public static final String PATH_LIST = "list";
    public static final String PATH_WORKOUT_TYPE = "workout_type";
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
        private WorkoutEntry(){};
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUT).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT;

        // table
        public static final String TABLE_NAME = "wrk";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_WRK_TYPE_ID = "wrk_type_id";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_NOTES = "notes";
        // create sql
        public static final String SQL_CREATE_WORKOUT_TABLE = "CREATE TABLE " + WorkoutEntry.TABLE_NAME + " (" +
                WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WorkoutEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                WorkoutEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                WorkoutEntry.COLUMN_WRK_TYPE_ID + " INTEGER NOT NULL," +
                WorkoutEntry.COLUMN_DURATION + " INTEGER NOT NULL, " +
                WorkoutEntry.COLUMN_NOTES + " TEXT, " +
                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + WorkoutEntry.COLUMN_WRK_TYPE_ID + ") REFERENCES " +
                WorkoutTypeEntry.TABLE_NAME + " (" + WorkoutTypeEntry._ID + "), " +
                // constraint
                " UNIQUE (" + WorkoutEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        /** returns workout/id uri */
        public static Uri buildWrkIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from workout/id uri
        public static String getWrkIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    /****** reference tables *****/
    /* workout type table */
    public static final class WorkoutTypeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUT_TYPE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT_TYPE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT_TYPE;

        // table
        public static final String TABLE_NAME = "wrk_type";
        public static final String COLUMN_NAME = "name";

        // create sql
        public static final String SQL_CREATE_WORKOUT_TYPE_TABLE = "CREATE TABLE " + WorkoutTypeEntry.TABLE_NAME + " (" +
                WorkoutTypeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WorkoutTypeEntry.COLUMN_NAME + " TEXT NOT NULL);";

        /** returns workoutType/id uri */
        public static Uri buildWrkTypeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from workoutType/id uri
        public static String getWrkTypeIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


    /// todo other tables


}
