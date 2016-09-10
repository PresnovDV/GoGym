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

public class DataProvider extends ContentProvider {
    // db helper
    private DataDbHelper mOpenHelper;


    //********************* URI Matcher ************************************************************
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // constants for URI Matcher
    public static final int WORKOUT = 100;
    public static final int WORKOUT_LIST = 101;
    public static final int WORKOUT_LIST_ID = 102;


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
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT + "/#", WORKOUT_LIST_ID);

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
                return DataContract.WorkoutEntry.CONTENT_TYPE;
            case WORKOUT_LIST_ID:
                return DataContract.WorkoutEntry.CONTENT_ITEM_TYPE;
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
            returnUri = DataContract.WorkoutEntry.buildWrkIdUri(_id);
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
                ds.append(DataContract.WorkoutEntry.TABLE_NAME);
                break;
            case(WORKOUT_LIST):{
                ds.append(DataContract.WorkoutEntry.TABLE_NAME).append(" inner join ")
                        .append(DataContract.WorkoutTypeEntry.TABLE_NAME).append(" on ")
                        .append(DataContract.WorkoutEntry.TABLE_NAME).append(".").append(DataContract.WorkoutEntry._ID).append(" = ")
                        .append(DataContract.WorkoutTypeEntry.TABLE_NAME).append(".").append(DataContract.WorkoutTypeEntry._ID);
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
                    sb.append(DataContract.WorkoutEntry.TABLE_NAME).append(".")
                            .append(DataContract.WorkoutEntry._ID).append("== ?");
                    break;
                }
            }
            whereClause = sb.toString();
        }
        return whereClause;
    }


    /** create args list for where clause template */
    private String[] createWhereClauseArgs(Uri uri, String[] whereClauseArgs) {
        if(whereClauseArgs == null) {
            switch (sUriMatcher.match(uri)) {
                case (WORKOUT_LIST_ID): {
                    whereClauseArgs = new String[]{ DataContract.WorkoutEntry.getWrkIdFromUri(uri) };
                    break;
                }
            }
        }
        return whereClauseArgs;
    }



    //*********************** Service methods *****************************//

    // Normalize Date
    private void normalizeDate(ContentValues values) {
        if (values.containsKey(DataContract.WorkoutEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(DataContract.WorkoutEntry.COLUMN_DATE);
            values.put(DataContract.WorkoutEntry.COLUMN_DATE, DataContract.normalizeDate(dateValue));
        }
    }
}