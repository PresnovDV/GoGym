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
import android.os.Bundle;
import android.util.Log;

import com.android.prasnou.app.WrkDataObject;
import com.android.prasnou.app.data.DataContract.ExcerciseEntry;
import com.android.prasnou.app.data.DataContract.WorkoutEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExSetEntry;
import com.android.prasnou.app.data.DataContract.WorkoutTypeEntry;

public class DataProvider extends ContentProvider {
    // static constants
    public static final String WORKOUT_OBJ = "WRK_D_OBJ";
    // custom methods
    public static final String ADD_WORKOUT_MTHD = "addWrk";
    // db helper
    private DataDbHelper mOpenHelper;


    //********************* URI Matcher ************************************************************
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // constants for URI Matcher
    // workout list
    public static final int WORKOUT = 10;
    public static final int WORKOUT_LIST = 11;
    public static final int WORKOUT_LIST_ID = 12;
    public static final int WORKOUT_MAX_NUMB = 13;
    // excercise list
    public static final int EX = 20;
    public static final int EX_LIST = 21;
    public static final int EX_LIST_ID = 22;

    // set
    public static final int EX_SET = 30;
    public static final int EX_SET_LIST = 31;
    public static final int EX_SET_LIST_ID = 32;
    // workout type ref
    public static final int WRK_TYPE_LIST = 40;
    // excercise type ref
    public static final int EX_TYPE_LIST = 41;


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
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT + "/" + DataContract.PATH_MAX, WORKOUT_MAX_NUMB);

        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX , EX);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX + "/" + DataContract.PATH_LIST + "/#", EX_LIST);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX_SET + "/" + DataContract.PATH_LIST + "/#/#", EX_LIST_ID);

        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX_SET , EX_SET);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX_SET + "/" + DataContract.PATH_LIST + "/#", EX_SET_LIST);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WRK_EX_SET + "/" + DataContract.PATH_LIST + "/#/#", EX_SET_LIST_ID);

        // refs
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT_TYPE + "/" + DataContract.PATH_LIST, WRK_TYPE_LIST);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_EXCERCISE + "/" + DataContract.PATH_LIST, EX_TYPE_LIST);

        return matcher;
    }

    // getType by URI Matcher
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        // match to type of data DIR / ITEM
        switch (match) {
            // workout
            case WORKOUT:
            case WORKOUT_LIST:
                return WorkoutEntry.CONTENT_TYPE;
            case WORKOUT_LIST_ID:
                return WorkoutEntry.CONTENT_ITEM_TYPE;
            case WORKOUT_MAX_NUMB:
                return WorkoutEntry.CONTENT_ITEM_TYPE;

            // excercise
            case EX:
            case EX_LIST:
                return WorkoutExEntry.CONTENT_TYPE;
            case EX_LIST_ID:
                return WorkoutExEntry.CONTENT_ITEM_TYPE;
            // set
            case EX_SET:
            case EX_SET_LIST:
                return WorkoutExSetEntry.CONTENT_TYPE;
            case EX_SET_LIST_ID:
                return WorkoutExSetEntry.CONTENT_ITEM_TYPE;
            // workout type
            case WRK_TYPE_LIST:
                return WorkoutTypeEntry.CONTENT_TYPE;
            // ex type
            case EX_TYPE_LIST:
                return ExcerciseEntry.CONTENT_TYPE;
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

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if(method.equals(ADD_WORKOUT_MTHD)) {
            WrkDataObject wrkDataObject = (WrkDataObject) extras.get(WORKOUT_OBJ);
            addWrk(wrkDataObject);
        }
        return null;
    }

    // --------------- Custom opertations ---------------------------------------
    private boolean addWrk(WrkDataObject wrkDObj){
        boolean result = true;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        //--- query the max wrk numb
        Uri maxNumbUri = WorkoutEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_MAX).build();
        String maxNumbProj = "max("+WorkoutEntry.COLUMN_NUMBER+") as number";
        Cursor cursor = db.query(createDataSource(maxNumbUri), new String[]{maxNumbProj},null,null,null,null,null);
        if(!cursor.moveToFirst()){
            return false;
        }
        else {
            wrkDObj.setWrkNumb(cursor.getInt(0)+1);
        }
        Log.d(ADD_WORKOUT_MTHD, "new wrk #"+wrkDObj.getWrkNumb());
        //---- save new wrk
        db.beginTransaction();

        try {
            // insert to wrk
            ContentValues wrkValues = new ContentValues();
            wrkValues.put(WorkoutEntry.COLUMN_DATE, 0);
            wrkValues.put(WorkoutEntry.COLUMN_NUMBER, wrkDObj.getWrkNumb());
            wrkValues.put(WorkoutEntry.COLUMN_WRK_TYPE_ID, wrkDObj.getWrkTypeId());
            wrkValues.put(WorkoutEntry.COLUMN_DURATION, 0);

            //normalizeDate(wrkValues);
            long wrkId = db.insert(createDataSource(WorkoutEntry.CONTENT_URI), null, wrkValues);
            Log.i(ADD_WORKOUT_MTHD,"Wrk saved id="+wrkId);

            // insert to wrk_ex
            if(wrkId > 0) {
                for (WrkDataObject.Ex ex : wrkDObj.getWrkExList()) {
                    ContentValues wrkExValues = new ContentValues();
                    wrkExValues.put(WorkoutExEntry.COLUMN_WRK_ID, wrkId);
                    wrkExValues.put(WorkoutExEntry.COLUMN_EX_ID, ex.getExInd());
                    wrkExValues.put(WorkoutExEntry.COLUMN_EX_NUMB, ex.getExNumb());

                    long wrkExId = db.insert(createDataSource(WorkoutExEntry.CONTENT_URI), null, wrkExValues);
                    Log.i(ADD_WORKOUT_MTHD,"Ex saved id="+wrkExId);

                    // insert to wrk_ex_set
                    if(wrkExId > 0) {
                        for (WrkDataObject.Set set : ex.getExSetList()) {
                            ContentValues wrkExSetValues = new ContentValues();
                            wrkExSetValues.put(WorkoutExSetEntry.COLUMN_WRK_EX_ID, wrkExId);
                            wrkExSetValues.put(WorkoutExSetEntry.COLUMN_SET_NUMB, set.getSetNumb());
                            wrkExSetValues.put(WorkoutExSetEntry.COLUMN_SET_WEIGHT, set.getSetWeight());
                            wrkExSetValues.put(WorkoutExSetEntry.COLUMN_SET_REPS, set.getSetReps());

                            long _id = db.insert(createDataSource(WorkoutExSetEntry.CONTENT_URI), null, wrkExSetValues);
                            Log.i(ADD_WORKOUT_MTHD,"Set saved id = "+ _id);
                        }
                    }
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return result;
    }

    /**********************************************************************************************/
    /****************************  Data Sources  **************************************************/
    /**********************************************************************************************/

    // Data Source
    private String createDataSource(Uri uri) {
        StringBuilder ds = new StringBuilder();
        switch (sUriMatcher.match(uri)){
            // workout
            case WORKOUT:
            case WORKOUT_MAX_NUMB:
                ds.append(WorkoutEntry.TABLE_NAME);
                break;
            case WORKOUT_LIST:{
                ds.append(WorkoutEntry.TABLE_NAME).append(" inner join ")
                        .append(WorkoutTypeEntry.TABLE_NAME).append(" on ")
                        .append(WorkoutEntry.TABLE_NAME).append(".").append(WorkoutEntry.COLUMN_WRK_TYPE_ID).append(" = ")
                        .append(WorkoutTypeEntry.TABLE_NAME).append(".").append(WorkoutTypeEntry._ID);
                break;
            }
            // wrk - excercise
            case EX_LIST:{
                ds.append(WorkoutExEntry.TABLE_NAME).append(" inner join ")
                        .append(ExcerciseEntry.TABLE_NAME).append(" on ")
                        .append(WorkoutExEntry.TABLE_NAME).append(".").append(WorkoutExEntry.COLUMN_EX_ID).append(" = ")
                        .append(ExcerciseEntry.TABLE_NAME).append(".").append(ExcerciseEntry._ID);
                break;
            }
            case EX_LIST_SET:{
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
            // workout type
            case WRK_TYPE_LIST:
                ds.append(WorkoutTypeEntry.TABLE_NAME);
                break;
            // excercise type
            case EX_TYPE_LIST:
                ds.append(ExcerciseEntry.TABLE_NAME);
                break;

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
                case WORKOUT_LIST_ID:{
                    whereClause = sb.append(WorkoutEntry.TABLE_NAME).append(".")
                            .append(WorkoutEntry._ID).append("== ?").toString();
                    break;
                }
                case EX_LIST:
                case EX_LIST_SET:{
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
                case WORKOUT_LIST_ID: {
                    whereClauseArgs = new String[]{ WorkoutEntry.getWrkIdFromUri(uri) };
                    break;
                }
                case EX_LIST:
                case EX_LIST_SET:{
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
