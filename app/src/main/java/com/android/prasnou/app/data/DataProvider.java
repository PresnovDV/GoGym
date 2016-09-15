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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.android.prasnou.app.data.DataContract.ExcerciseEntry;
import com.android.prasnou.app.data.DataContract.WorkoutEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExSetEntry;
import com.android.prasnou.app.data.DataContract.WorkoutTypeEntry;

public class DataProvider extends ContentProvider {
    // db helper
    private DataDbHelper mOpenHelper;


    //********************* URI Matcher ************************************************************
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // constants for URI Matcher
    public static final int WORKOUT = 100;
    public static final int WORKOUT_LIST = 101;
    public static final int WORKOUT_LIST_ID = 102;
    public static final int EX = 200;
    public static final int EX_LIST = 201;
    public static final int EX_LIST_ID = 202;
    public static final int EX_LIST_SET = 203;


    //********************* Init content provider **************************************************
    @Override
    public boolean onCreate() {
        mOpenHelper = new DataDbHelper(getContext());
        return true;
    }

    /**********************************************************************************************/
    /**************************************  URI Matcher  *****************************************/
    /**********************************************************************************************/

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT, WORKOUT);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT + "/" + DataContract.PATH_LIST, WORKOUT_LIST);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT + "/" + DataContract.PATH_LIST + "/#", WORKOUT_LIST_ID);

        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_EXCERCISE, EX);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX + "/" + DataContract.PATH_LIST + "/#", EX_LIST);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX_SET + "/" + DataContract.PATH_LIST + "/#", EX_LIST_SET);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX_SET + "/" + DataContract.PATH_LIST + "/#/#", EX_LIST_ID);

        return matcher;
    }

    // getType by URI Matcher
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        // match to type of data DIR / ITEM
        switch (match) {
            case WORKOUT:
            case WORKOUT_LIST:
                return WorkoutEntry.CONTENT_TYPE;
            case WORKOUT_LIST_ID:
                return WorkoutEntry.CONTENT_ITEM_TYPE;
            case EX:
            case EX_LIST:
            case EX_LIST_SET:
                return ExcerciseEntry.CONTENT_TYPE;
            case EX_LIST_ID:
                return ExcerciseEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    /**********************************************************************************************/
    /**************************************  Query Section ****************************************/
    /**********************************************************************************************/

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String whereClause = createWhereClause(uri, selection);
        String[] whereClauseArgs = createWhereClauseArgs(uri, selectionArgs);

        Cursor retCursor = mOpenHelper.getReadableDatabase().query(createDataSource(uri), projection, whereClause, whereClauseArgs, null, null, sortOrder);
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        normalizeDate(values);
        long _id = db.insert(createDataSource(uri), null, values);
        if ( _id > 0 )
            returnUri = WorkoutEntry.buildWrkIdUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        if(selection == null){
            selection = "1";
        }
        int rowsDeleted = db.delete(createDataSource(uri), selection, selectionArgs);

        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int updatedRows = db.update(createDataSource(uri), values, selection, selectionArgs);
        if(updatedRows !=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }

/* ?? override for insert sets
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
  */

    /**********************************************************************************************/
    /****************************  Data Sources  **************************************************/
    /**********************************************************************************************/

    // Data Source
    private String createDataSource(Uri uri) {
        StringBuilder ds = new StringBuilder();
        switch (sUriMatcher.match(uri)){
            case(WORKOUT):
                ds.append(WorkoutEntry.TABLE_NAME);
                break;
            case(WORKOUT_LIST):{
                ds.append(WorkoutEntry.TABLE_NAME).append(" inner join ")
                        .append(WorkoutTypeEntry.TABLE_NAME).append(" on ")
                        .append(WorkoutEntry.TABLE_NAME).append(".").append(WorkoutEntry.COLUMN_WRK_TYPE_ID).append(" = ")
                        .append(WorkoutTypeEntry.TABLE_NAME).append(".").append(WorkoutTypeEntry._ID);
                break;
            }
            case(EX_LIST):{
                ds.append(WorkoutExEntry.TABLE_NAME).append(" inner join ")
                        .append(ExcerciseEntry.TABLE_NAME).append(" on ")
                        .append(WorkoutExEntry.TABLE_NAME).append(".").append(WorkoutExEntry.COLUMN_EX_ID).append(" = ")
                        .append(ExcerciseEntry.TABLE_NAME).append(".").append(ExcerciseEntry._ID);
                break;
            }
            case(EX_LIST_SET):{
                ds.append(WorkoutExEntry.TABLE_NAME)
                        .append(" left outer join ")
                        .append(WorkoutExSetEntry.TABLE_NAME).append(" on ")
                        .append(WorkoutExEntry.TABLE_NAME).append(".").append(WorkoutExEntry._ID).append(" = ")
                        .append(WorkoutExSetEntry.TABLE_NAME).append(".").append(WorkoutExSetEntry.COLUMN_WRK_EX_ID)
                        .append(" inner join ")
                        .append(ExcerciseEntry.TABLE_NAME).append(" on ")
                        .append(WorkoutExEntry.TABLE_NAME).append(".").append(WorkoutExEntry.COLUMN_EX_ID).append(" = ")
                        .append(ExcerciseEntry.TABLE_NAME).append(".").append(ExcerciseEntry._ID);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return ds.toString();
    }

    /** creates where clause template */
    private String createWhereClause(Uri uri, String whereClause) {
        if(whereClause == null){
            StringBuilder sb = new StringBuilder();
            switch (sUriMatcher.match(uri)){
                case(WORKOUT_LIST_ID):{
                    whereClause = sb.append(WorkoutEntry.TABLE_NAME).append(".")
                            .append(WorkoutEntry._ID).append("== ?").toString();
                    break;
                }
                case(EX_LIST):
                case(EX_LIST_SET):{
                    whereClause = sb.append(WorkoutExEntry.TABLE_NAME).append(".")
                            .append(WorkoutExEntry.COLUMN_WRK_ID).append("== ?").toString();
                    break;
                }
            }
        }
        return whereClause;
    }

    /** create args list for where clause template */
    private String[] createWhereClauseArgs(Uri uri, String[] whereClauseArgs) {
        if(whereClauseArgs == null) {
            switch (sUriMatcher.match(uri)) {
                case (WORKOUT_LIST_ID): {
                    whereClauseArgs = new String[]{ WorkoutEntry.getWrkIdFromUri(uri) };
                    break;
                }
                case (EX_LIST):
                case (EX_LIST_SET):{
                    whereClauseArgs = new String[]{ WorkoutExEntry.getWrkIdFromUri(uri) };
                    break;
                }
            }
        }
        return whereClauseArgs;
    }

    //*********************** Service methods *****************************//

    // Normalize Date
    private void normalizeDate(ContentValues values) {
        if (values.containsKey(WorkoutEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(WorkoutEntry.COLUMN_DATE);
            values.put(WorkoutEntry.COLUMN_DATE, DataContract.normalizeDate(dateValue));
        }
    }
}