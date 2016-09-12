package com.android.prasnou.app;




import android.content.Intent;
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
import android.widget.ListView;

import com.android.prasnou.app.data.DataContract;
import com.android.prasnou.app.data.DataContract.*;

/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class ExcerciseListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int WRK_EX_LIST_LOADER_ID = 0;
    private ExAdapter mWrkExAdapter;
    private ListView mListView;
    private int mWrkID = -1;

    //*************** Workout List Cols ***********************
    private static final String[] WRK_EX_LIST_COLUMNS = {
            WorkoutExEntry.TABLE_NAME + "." + WorkoutEntry._ID,
            WorkoutExEntry.COLUMN_WRK_ID,
            WorkoutExEntry.COLUMN_EX_ID,
            WorkoutExEntry.COLUMN_EX_NUMB,
            ExcerciseEntry.TABLE_NAME + "." + WorkoutTypeEntry.COLUMN_NAME
    };

    static final int COL_WRK_ID = 0;
    static final int COL_EX_ID = 1;
    static final int COL_EX_NUMB = 2;
    static final int COL_EX_NAME = 3;
    //******************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(WRK_EX_LIST_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWrkExAdapter = new ExAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fr_ex_list, container, false);
        mListView = (ListView) rootView.findViewById(R.id.ex_listview);
        mListView.setAdapter(mWrkExAdapter);

        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }*/
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mWrkID > -1) {
            String[] select = WRK_EX_LIST_COLUMNS;
            String orderBy = WorkoutExEntry.COLUMN_EX_NUMB + " ASC";
            Uri uri = DataContract.WorkoutExEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).appendPath(String.valueOf(mWrkID)).build();

            return new CursorLoader(getActivity(), uri, select, null, null, orderBy);
        }
        else {
            return null;
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mWrkExAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWrkExAdapter.changeCursor(null);
    }

    public void setWrkID(int id) {
        mWrkID = id;
    }

}
