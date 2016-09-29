package com.android.prasnou.app;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
public class WrkListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int WORKOUT_LIST_LOADER_ID = 0;
    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;
    private WrkListAdapter mWorkoutAdapter;
    private ListView mListView;

    /** callback to pass selected workout id to other activities/fragments*/
    public interface Callback {
        public void onItemSelected(int wrkId);
    }

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

        mWorkoutAdapter = new WrkListAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fr_wrk_list, container, false);
        mListView = (ListView) rootView.findViewById(R.id.wrk_listview);
        mListView.setAdapter(mWorkoutAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = mPosition==position ? ListView.INVALID_POSITION : position;
                ((Callback)getActivity()).onItemSelected((Integer)view.getTag(R.id.fr_wrk_list));
                ExcerciseListFragment exFt = (ExcerciseListFragment)getFragmentManager().findFragmentById(R.id.fr_ex_list);
                if(exFt != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(exFt);
                    ft.commitNow();
                }

                mWorkoutAdapter.notifyDataSetChanged();
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
        String orderBy = DataContract.WorkoutEntry.COLUMN_NUMBER + " DESC";

        return new CursorLoader(getActivity(),uri,select,null,null,orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mWorkoutAdapter.changeCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWorkoutAdapter.changeCursor(null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_KEY, mPosition);
        super.onSaveInstanceState(outState);
    }

}

