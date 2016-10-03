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
    public static final String PATH_WRK_EX = "wrk_ex";
    public static final String PATH_WRK_EX_SET = "wrk_ex_set";
    public static final String PATH_LIST = "list";
    public static final String PATH_MAX = "max";

    public static final String PATH_WORKOUT_TYPE = "workout_type";
    public static final String PATH_EX_TYPE = "ex_type";


    // all dates normalize to the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /** Workout table  */
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
                WorkoutTypeEntry.TABLE_NAME + " (" + WorkoutTypeEntry._ID + "));";

        /** returns workout/id uri */
        public static Uri buildWrkIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from workout/id uri
        public static String getWrkIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    /** Workout Excercise table  */
    public static final class WorkoutExEntry implements BaseColumns {
        private WorkoutExEntry(){};
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WRK_EX).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WRK_EX;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WRK_EX;

        // table
        public static final String TABLE_NAME = "wrk_ex";
        public static final String COLUMN_WRK_ID = "wrk_id";
        public static final String COLUMN_EX_ID = "ex_type_id";
        public static final String COLUMN_EX_NUMB = "ex_numb";

        // create sql
        public static final String SQL_CREATE_WRK_EX_TABLE = "CREATE TABLE " + WorkoutExEntry.TABLE_NAME + " (" +
                WorkoutExEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WorkoutExEntry.COLUMN_WRK_ID + " INTEGER NOT NULL, " +
                WorkoutExEntry.COLUMN_EX_ID + " INTEGER NOT NULL, " +
                WorkoutExEntry.COLUMN_EX_NUMB + " INTEGER NOT NULL, " +
                // foreign keys
                " FOREIGN KEY (" + WorkoutExEntry.COLUMN_WRK_ID + ") REFERENCES " +
                WorkoutEntry.TABLE_NAME + " (" + WorkoutEntry._ID + "), " +
                " FOREIGN KEY (" + WorkoutExEntry.COLUMN_EX_ID + ") REFERENCES " +
                ExTypeEntry.TABLE_NAME + " (" + ExTypeEntry._ID + "), " +
                // constraint
                " UNIQUE (" + WorkoutExEntry.COLUMN_WRK_ID + "," + WorkoutExEntry.COLUMN_EX_ID + ") ON CONFLICT REPLACE);";
        /** returns workoutEx/id uri */
        public static Uri buildWrkExIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from workoutEx/id uri
        public static String getWrkExIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        public static String getWrkIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
        public static String getWrkIdFromExSetListUri(Uri uri) {
            return uri.getPathSegments().get(4);
        }

    }

    /** Workout Excercise Set table  */
    public static final class WorkoutExSetEntry implements BaseColumns {
        private WorkoutExSetEntry(){};
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WRK_EX_SET).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WRK_EX_SET;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WRK_EX_SET;

        // table
        public static final String TABLE_NAME = "wrk_ex_set";
        public static final String COLUMN_WRK_EX_ID = "wrk_ex_id";
        public static final String COLUMN_SET_NUMB = "set_numb";
        public static final String COLUMN_SET_WEIGHT = "set_weight";
        public static final String COLUMN_SET_REPS = "set_reps";

        // create sql
        public static final String SQL_CREATE_WORKOUT_EX_SET_TABLE = "CREATE TABLE " + WorkoutExSetEntry.TABLE_NAME + " (" +
                WorkoutExSetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WorkoutExSetEntry.COLUMN_WRK_EX_ID + " INTEGER NOT NULL, " +
                WorkoutExSetEntry.COLUMN_SET_NUMB + " INTEGER NOT NULL, " +
                WorkoutExSetEntry.COLUMN_SET_WEIGHT + " INTEGER NOT NULL, " +
                WorkoutExSetEntry.COLUMN_SET_REPS + " INTEGER NOT NULL, " +
                // foreign keys
                " FOREIGN KEY (" + WorkoutExSetEntry.COLUMN_WRK_EX_ID + ") REFERENCES " +
                WorkoutExEntry.TABLE_NAME + " (" + WorkoutExEntry._ID + "));";

        /** returns workoutSet/id uri */
        public static Uri buildWrkExSetIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from workoutExSet/id uri
        public static String getWrkExSetIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        public static String getWrkExIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    /***********************************************************************************************/
    /************************************** reference tables ***************************************/


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


    /* excercise table */
    public static final class ExTypeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EX_TYPE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EX_TYPE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EX_TYPE;

        // table
        public static final String TABLE_NAME = "ex_type";
        public static final String COLUMN_NAME = "name";

        // create sql
        public static final String SQL_CREATE_EXCERCISE_TABLE = "CREATE TABLE " + ExTypeEntry.TABLE_NAME + " (" +
                ExTypeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ExTypeEntry.COLUMN_NAME + " TEXT NOT NULL);";

        /** returns excercise/id uri */
        public static Uri buildExUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from ex/id uri
        public static String getExIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}
