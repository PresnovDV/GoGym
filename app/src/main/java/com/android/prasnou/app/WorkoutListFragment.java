package com.android.prasnou.app;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.prasnou.app.data.DataContract;

/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class WorkoutListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int WORKOUT_LIST_LOADER_ID = 0;
    private static final String SELECTED_KEY = "selected_wrk";
    private int mPosition = ListView.INVALID_POSITION;
    private WrkAdapter mWorkoutAdapter;
    private ListView mListView;

    //*************** Workout List Cols ***********************
    private static final String[] WRK_LIST_COLUMNS = {
            DataContract.WorkoutEntry.TABLE_NAME + "." + DataContract.WorkoutEntry._ID,
            DataContract.WorkoutEntry.COLUMN_NUMBER,
            DataContract.WorkoutTypeEntry.TABLE_NAME + "." + DataContract.WorkoutTypeEntry.COLUMN_NAME,
            DataContract.WorkoutEntry.COLUMN_DATE,
            DataContract.WorkoutEntry.COLUMN_DURATION,
            DataContract.WorkoutEntry.COLUMN_NOTES,

    };

    static final int COL_WRK_ID = 0;
    static final int COL_WRK_NUMBER = 1;
    static final int COL_WRK_TYPE = 2;
    static final int COL_WRK_DATE = 3;
    static final int COL_WRK_DURATION = 4;
    static final int COL_WRK_NOTES = 5;
    static final int COL_WRK_WEIGHT = 6;

    //******************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(WORKOUT_LIST_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mWorkoutAdapter = new WrkAdapter(getActivity(), null, 0);
        //mWorkoutAdapter.setUseSpecialTodayLayout(mUseSpecialTodayLayout);

        View rootView = inflater.inflate(R.layout.fr_wrk_list, container, false);
        mListView = (ListView) rootView.findViewById(R.id.wrk_listview);
        mListView.setAdapter(mWorkoutAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // presnov todo here
                View view = LayoutInflater.from(context).inflate(layoutId, parent, false);


                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(
                            DataContract.WorkoutEntry.buildWeatherLocationWithDate(
                                    Utility.getPreferredLocation(getContext()),
                                    cursor.getLong(COL_WEATHER_DATE)));
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DataContract.WorkoutEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).build();
        String[] select = WRK_LIST_COLUMNS;
        String orderBy = DataContract.WorkoutEntry.COLUMN_DATE + " ASC";

        return new CursorLoader(getActivity(),uri,select,null,null,orderBy);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mWorkoutAdapter.changeCursor(data);
        /*if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWorkoutAdapter.changeCursor(null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

}
