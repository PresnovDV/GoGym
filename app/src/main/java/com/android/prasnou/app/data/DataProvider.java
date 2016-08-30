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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DataProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DataDbHelper mOpenHelper;

    public static final int WORKOUT = 100;
    public static final int WORKOUT_WITH_NUMBER = 101;
    public static final int WORKOUT_TYPE = 110;
/*
    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    static{
        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();
        
        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByLocationSettingQueryBuilder.setTables(
                WeatherContract.WeatherEntry.TABLE_NAME + " INNER JOIN " +
                        WeatherContract.LocationEntry.TABLE_NAME +
                        " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
                        "." + WeatherContract.WeatherEntry.COLUMN_LOC_KEY +
                        " = " + WeatherContract.LocationEntry.TABLE_NAME +
                        "." + WeatherContract.LocationEntry._ID);
    }

    //location.location_setting = ?
    private static final String sLocationSettingSelection =
            WeatherContract.LocationEntry.TABLE_NAME+
                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? ";

    //location.location_setting = ? AND date >= ?
    private static final String sLocationSettingWithStartDateSelection =
            WeatherContract.LocationEntry.TABLE_NAME+
                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    WeatherContract.WeatherEntry.COLUMN_DATE + " >= ? ";

    //location.location_setting = ? AND date = ?
    private static final String sLocationSettingAndDaySelection =
            WeatherContract.LocationEntry.TABLE_NAME +
                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ";



    private Cursor getWeatherByLocationSettingAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
        long date = WeatherContract.WeatherEntry.getDateFromUri(uri);

        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelection,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }
*/

    // wrk.number = ?
    private Cursor getWorkoutByNumber(Uri uri, String[] projection, String sortOrder) {
        String number = DataContract.WorkoutEntry.getWrkNumberFromUri(uri);

        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{number};
        selection = DataContract.WorkoutEntry.TABLE_NAME + "." + DataContract.WorkoutEntry.COLUMN_NUMBER + " = ? ";

        return mOpenHelper.getReadableDatabase().query(
                DataContract.WorkoutEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT ,WORKOUT);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT + "/#", WORKOUT_WITH_NUMBER);
        matcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_WORKOUT_TYPE, WORKOUT_TYPE);
        // ?? add other paths
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new DataDbHelper(getContext());
        return true;
    }


    // Use the Uri Matcher to determine what kind of URI this is.
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
// ?? to add more
        switch (match) {
            case WORKOUT_WITH_NUMBER:
                return DataContract.WorkoutEntry.CONTENT_ITEM_TYPE;
            case WORKOUT:
                return DataContract.WorkoutEntry.CONTENT_TYPE;
            case WORKOUT_TYPE:
                return DataContract.WorkoutTypeEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*"
            case WORKOUT_WITH_NUMBER: {
                retCursor = getWorkoutByNumber(uri, projection, sortOrder);
                break;
            }
            // "weather"
            case WORKOUT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.WorkoutEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case WORKOUT: {
                normalizeDate(values);
                long _id = db.insert(DataContract.WorkoutEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DataContract.WorkoutEntry.buildWrkUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            // ?? add more
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;

        if(selection == null){
            selection = "1";
        }
        switch (match){
            case WORKOUT:
                rowsDeleted = db.delete(DataContract.WorkoutEntry.TABLE_NAME, selection, selectionArgs);
                break;
            // ?? add more
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updatedRows = 0;

        switch (match){
            case WORKOUT:
                updatedRows = db.update(DataContract.WorkoutEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            // ?? add more
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
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
    // Normalize Date
    private void normalizeDate(ContentValues values) {
        if (values.containsKey(DataContract.WorkoutEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(DataContract.WorkoutEntry.COLUMN_DATE);
            values.put(DataContract.WorkoutEntry.COLUMN_DATE, DataContract.normalizeDate(dateValue));
        }
    }

    // shutdown for unitests
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}