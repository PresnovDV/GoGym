package com.android.prasnou.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.prasnou.app.R;
import com.android.prasnou.app.data.DataContract.WorkoutEntry;
import com.android.prasnou.app.data.DataContract.WorkoutTypeEntry;
import com.android.prasnou.app.data.DataContract.WorkoutSetEntry;
import com.android.prasnou.app.data.DataContract.ExcerciseEntry;
import com.android.prasnou.app.data.DataContract.SetTypeEntry;

/**
 * Manage a local database.
 */
public class DataDbHelper extends SQLiteOpenHelper {
    private Context mContext;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 12;

    public static final String DATABASE_NAME = "gogym.db";

    public DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WorkoutTypeEntry.SQL_CREATE_WORKOUT_TYPE_TABLE);
        sqLiteDatabase.execSQL(ExcerciseEntry.SQL_CREATE_EXCERCISE_TABLE);
        sqLiteDatabase.execSQL(SetTypeEntry.SQL_CREATE_SET_TYPE_TABLE);

        sqLiteDatabase.execSQL(WorkoutEntry.SQL_CREATE_WORKOUT_TABLE);
        sqLiteDatabase.execSQL(WorkoutSetEntry.SQL_CREATE_WORKOUT_SET_TABLE);

        initData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WorkoutSetEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WorkoutEntry.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WorkoutTypeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExcerciseEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SetTypeEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }


    private void initData(SQLiteDatabase db) {
        // Initialize the db with values from data_init.xml
        ContentValues values = new ContentValues();
        Resources res = mContext.getResources();

        // Workout Type reference
        String[] wrkTypeData = res.getStringArray(R.array.wrk_type_data);
        for (String item : wrkTypeData){
            values.put(WorkoutTypeEntry.COLUMN_NAME, item);
            db.insert(WorkoutTypeEntry.TABLE_NAME, null, values);
        }

        // excercise reference
        String[] exData = res.getStringArray(R.array.ex_data);
        for (String item : exData){
            values.put(DataContract.ExcerciseEntry.COLUMN_NAME, item);
            db.insert(DataContract.ExcerciseEntry.TABLE_NAME, null, values);
        }

        // excercise reference
        String[] setTypeData = res.getStringArray(R.array.set_type_data);
        for (String item : setTypeData){
            values.put(DataContract.SetTypeEntry.COLUMN_NAME, item);
            db.insert(DataContract.SetTypeEntry.TABLE_NAME, null, values);
        }

        // Test !! Workout
        values.clear();
        values.put(WorkoutEntry.COLUMN_NUMBER, 189);
        values.put(WorkoutEntry.COLUMN_WRK_TYPE_ID, 1);
        values.put(WorkoutEntry.COLUMN_DATE, System.currentTimeMillis());
        values.put(WorkoutEntry.COLUMN_DURATION, 60);
        values.put(WorkoutEntry.COLUMN_NOTES, "Went good. some pain in knee. Mood is good. a bit overdone");
        db.insert(WorkoutEntry.TABLE_NAME, null, values);
        values.clear();
        values.put(WorkoutEntry.COLUMN_NUMBER, 190);
        values.put(WorkoutEntry.COLUMN_WRK_TYPE_ID, 2);
        values.put(WorkoutEntry.COLUMN_DATE, System.currentTimeMillis());
        values.put(WorkoutEntry.COLUMN_DURATION, 75);
        values.put(WorkoutEntry.COLUMN_NOTES, "No power, undersleep");
        db.insert(WorkoutEntry.TABLE_NAME, null, values);
        values.clear();
        values.put(WorkoutEntry.COLUMN_NUMBER, 191);
        values.put(WorkoutEntry.COLUMN_WRK_TYPE_ID, 3);
        values.put(WorkoutEntry.COLUMN_DATE, System.currentTimeMillis());
        values.put(WorkoutEntry.COLUMN_DURATION, 72);
        values.put(WorkoutEntry.COLUMN_NOTES, "good");
        db.insert(WorkoutEntry.TABLE_NAME, null, values);

        // Test !! WorkoutSet
        // ex 1
        values.clear();
        values.put(WorkoutSetEntry.COLUMN_WRK_ID, 0);
        values.put(WorkoutSetEntry.COLUMN_EX_ID, 0);
        values.put(WorkoutSetEntry.COLUMN_SET_TYPE_ID, 0);
        values.put(WorkoutSetEntry.COLUMN_SET_NUMB, 1);
        values.put(WorkoutSetEntry.COLUMN_SET_WEIGHT, 55);
        values.put(WorkoutSetEntry.COLUMN_SET_REPS, 8);
        db.insert(WorkoutSetEntry.TABLE_NAME, null, values);

        values.clear();
        values.put(WorkoutSetEntry.COLUMN_WRK_ID, 0);
        values.put(WorkoutSetEntry.COLUMN_EX_ID, 0);
        values.put(WorkoutSetEntry.COLUMN_SET_TYPE_ID, 1);
        values.put(WorkoutSetEntry.COLUMN_SET_NUMB, 2);
        values.put(WorkoutSetEntry.COLUMN_SET_WEIGHT, 75);
        values.put(WorkoutSetEntry.COLUMN_SET_REPS, 5);
        db.insert(WorkoutSetEntry.TABLE_NAME, null, values);

        // ex 2
        values.clear();
        values.put(WorkoutSetEntry.COLUMN_WRK_ID, 0);
        values.put(WorkoutSetEntry.COLUMN_EX_ID, 1);
        values.put(WorkoutSetEntry.COLUMN_SET_TYPE_ID, 0);
        values.put(WorkoutSetEntry.COLUMN_SET_NUMB, 1);
        values.put(WorkoutSetEntry.COLUMN_SET_WEIGHT, 120);
        values.put(WorkoutSetEntry.COLUMN_SET_REPS, 8);
        db.insert(WorkoutSetEntry.TABLE_NAME, null, values);

        values.clear();
        values.put(WorkoutSetEntry.COLUMN_WRK_ID, 0);
        values.put(WorkoutSetEntry.COLUMN_EX_ID, 1);
        values.put(WorkoutSetEntry.COLUMN_SET_TYPE_ID, 1);
        values.put(WorkoutSetEntry.COLUMN_SET_NUMB, 2);
        values.put(WorkoutSetEntry.COLUMN_SET_WEIGHT, 175);
        values.put(WorkoutSetEntry.COLUMN_SET_REPS, 6);
        db.insert(WorkoutSetEntry.TABLE_NAME, null, values);

    }
}
