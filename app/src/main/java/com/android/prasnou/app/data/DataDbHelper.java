package com.android.prasnou.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.prasnou.app.R;
import com.android.prasnou.app.data.DataContract.ExcerciseEntry;
import com.android.prasnou.app.data.DataContract.SetTypeEntry;
import com.android.prasnou.app.data.DataContract.WorkoutEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExSetEntry;
import com.android.prasnou.app.data.DataContract.WorkoutTypeEntry;

/**
 * Manage a local database.
 */
public class DataDbHelper extends SQLiteOpenHelper {
    private Context mContext;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 24;

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

        // test data presnov
        sqLiteDatabase.execSQL(WorkoutEntry.SQL_CREATE_WORKOUT_TABLE);
        sqLiteDatabase.execSQL(WorkoutExEntry.SQL_CREATE_WRK_EX_TABLE);
        sqLiteDatabase.execSQL(WorkoutExSetEntry.SQL_CREATE_WORKOUT_EX_SET_TABLE);

        initData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WorkoutExSetEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WorkoutExEntry.TABLE_NAME);
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

        // Test !! Workout presnov
        String[] wrkTestData = res.getStringArray(R.array.workout_test_data);
        for (String item : wrkTestData){
            String[] vals = item.split("[;]");
            values.clear();
            values.put(WorkoutEntry.COLUMN_NUMBER, vals[0]);
            values.put(WorkoutEntry.COLUMN_WRK_TYPE_ID, vals[1]);
            values.put(WorkoutEntry.COLUMN_DATE, vals[2]);
            values.put(WorkoutEntry.COLUMN_DURATION, vals[3]);
            values.put(WorkoutEntry.COLUMN_NOTES, vals[4]);
            db.insert(WorkoutEntry.TABLE_NAME, null, values);
        }

        // Test !! WRK_EX presnov
        String[] wrkExTestData = res.getStringArray(R.array.workout_ex_test_data);
        for (String item : wrkExTestData){
            String[] vals = item.split("[;]");
            values.clear();
            values.put(WorkoutExEntry.COLUMN_WRK_ID, vals[0]);
            values.put(WorkoutExEntry.COLUMN_EX_ID, vals[1]);
            values.put(WorkoutExEntry.COLUMN_EX_NUMB, vals[2]);
            db.insert(WorkoutExEntry.TABLE_NAME, null, values);
        }

        // Test !! WRK_EX_SET presnov
        String[] wrkExSetTestData = res.getStringArray(R.array.workout_ex_set_test_data);
        for (String item : wrkExSetTestData){
            String[] vals = item.split("[;]");
            values.clear();
            values.put(WorkoutExSetEntry.COLUMN_WRK_EX_ID, vals[0]);
            values.put(WorkoutExSetEntry.COLUMN_SET_NUMB, vals[1]);
            values.put(WorkoutExSetEntry.COLUMN_SET_TYPE_ID, vals[2]);
            values.put(WorkoutExSetEntry.COLUMN_SET_WEIGHT, vals[3]);
            values.put(WorkoutExSetEntry.COLUMN_SET_REPS, vals[4]);
            db.insert(WorkoutExSetEntry.TABLE_NAME, null, values);
        }


    }
}
