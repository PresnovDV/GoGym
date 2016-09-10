/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.prasnou.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.prasnou.app.R;
import com.android.prasnou.app.data.DataContract.WorkoutEntry;
import com.android.prasnou.app.data.DataContract.WorkoutTypeEntry;

/**
 * Manages a local database.
 */
public class DataDbHelper extends SQLiteOpenHelper {
    private Context mContext;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME = "gogym.db";

    public DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WorkoutEntry.SQL_CREATE_WORKOUT_TABLE);

        sqLiteDatabase.execSQL(WorkoutTypeEntry.SQL_CREATE_WORKOUT_TYPE_TABLE);
        initData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WorkoutEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WorkoutTypeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    private void initData(SQLiteDatabase db) {
        // Initialize the db with values from data_init.xml
        ContentValues values = new ContentValues();
        Resources res = mContext.getResources();

        // Workout Type dictionary
        String[] wrkTypeData = res.getStringArray(R.array.wrk_type_data);
        for (String item : wrkTypeData){
            values.put(WorkoutTypeEntry.COLUMN_NAME, item);
            db.insert(WorkoutTypeEntry.TABLE_NAME, null, values);
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

    }
}
