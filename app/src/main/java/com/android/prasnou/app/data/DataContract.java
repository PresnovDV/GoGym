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
    public static final String PATH_LIST = "list";

    public static final String PATH_WORKOUT_TYPE = "workout_type";
    public static final String PATH_EXCERCISE = "excercise";
    public static final String PATH_SET_TYPE = "set_type";


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

    /** Workout Set table  */
    public static final class WorkoutSetEntry implements BaseColumns {
        private WorkoutSetEntry(){};
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUT_SET).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT_SET;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORKOUT_SET;

        // table
        public static final String TABLE_NAME = "wrk_set";
        public static final String COLUMN_WRK_ID = "wrk_id";
        public static final String COLUMN_EX_ID = "ex_id";
        public static final String COLUMN_SET_TYPE_ID = "set_type_id";
        public static final String COLUMN_SET_NUMB = "set_numb";
        public static final String COLUMN_SET_WEIGHT = "set_weight";
        public static final String COLUMN_SET_REPS = "set_reps";


        // create sql
        public static final String SQL_CREATE_WORKOUT_SET_TABLE = "CREATE TABLE " + WorkoutSetEntry.TABLE_NAME + " (" +
                WorkoutSetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WorkoutSetEntry.COLUMN_WRK_ID + " INTEGER NOT NULL, " +
                WorkoutSetEntry.COLUMN_EX_ID + " INTEGER NOT NULL, " +
                WorkoutSetEntry.COLUMN_SET_TYPE_ID + " INTEGER NOT NULL," +
                WorkoutSetEntry.COLUMN_SET_NUMB + " INTEGER NOT NULL, " +
                WorkoutSetEntry.COLUMN_SET_WEIGHT + " INTEGER NOT NULL, " +
                WorkoutSetEntry.COLUMN_SET_REPS + " INTEGER NOT NULL, " +
                // foreign keys
                " FOREIGN KEY (" + WorkoutSetEntry.COLUMN_WRK_ID + ") REFERENCES " +
                WorkoutEntry.TABLE_NAME + " (" + WorkoutEntry._ID + "), " +
                " FOREIGN KEY (" + WorkoutSetEntry.COLUMN_EX_ID + ") REFERENCES " +
                ExcerciseEntry.TABLE_NAME + " (" + ExcerciseEntry._ID + ");"; todo error near ;

        /** returns workoutSet/id uri */
        public static Uri buildWrkSetIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from workoutSet/id uri
        public static String getWrkSetIdFromUri(Uri uri) {
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


    /* excercise table */
    public static final class ExcerciseEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXCERCISE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXCERCISE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXCERCISE;

        // table
        public static final String TABLE_NAME = "excercise";
        public static final String COLUMN_NAME = "name";

        // create sql
        public static final String SQL_CREATE_EXCERCISE_TABLE = "CREATE TABLE " + ExcerciseEntry.TABLE_NAME + " (" +
                ExcerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ExcerciseEntry.COLUMN_NAME + " TEXT NOT NULL);";

        /** returns excercise/id uri */
        public static Uri buildExUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from ex/id uri
        public static String getExIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* set type table */
    public static final class SetTypeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SET_TYPE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SET_TYPE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SET_TYPE;

        // table
        public static final String TABLE_NAME = "set_type";
        public static final String COLUMN_NAME = "name";

        // create sql
        public static final String SQL_CREATE_SET_TYPE_TABLE = "CREATE TABLE " + SetTypeEntry.TABLE_NAME + " (" +
                SetTypeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SetTypeEntry.COLUMN_NAME + " TEXT NOT NULL);";

        /** returns setType/id uri */
        public static Uri buildSetTypeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // returns id from SetType/id uri
        public static String getSetTypeIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }



}
