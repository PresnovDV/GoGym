package com.android.prasnou.app;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.prasnou.app.data.DataContract;
import com.android.prasnou.app.data.DataContract.ExcerciseEntry;
import com.android.prasnou.app.data.DataContract.WorkoutEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExEntry;

/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class ExcerciseListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int WRK_EX_LIST_LOADER_ID = 0;
    private LinearLayout mListViewContainer;
    private LayoutInflater rootInflater;
    private int mWrkID = 1;  // presnov change to -1

    //*************** Workout List Cols ***********************
    private static final String[] WRK_EX_LIST_COLUMNS = {
            WorkoutExEntry.TABLE_NAME + "." + WorkoutEntry._ID,
            WorkoutExEntry.COLUMN_WRK_ID,
            WorkoutExEntry.COLUMN_EX_ID,
            WorkoutExEntry.COLUMN_EX_NUMB,
            ExcerciseEntry.TABLE_NAME + "." + ExcerciseEntry.COLUMN_NAME
    };

    static final int COL_ID = 0;
    static final int COL_WRK_ID = 1;
    static final int COL_EX_ID = 2;
    static final int COL_EX_NUMB = 3;
    static final int COL_EX_NAME = 4;
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
        rootInflater = inflater;
        View rootView = inflater.inflate(R.layout.fr_ex_list, container, false);
        mListViewContainer = (LinearLayout) rootView.findViewById(R.id.ex_list);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mWrkID > 0) {
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
        if(data.moveToFirst()) {
            int exId = -1;
            View view = null;
            ViewHolder vHolder = null;

            do{
                if(exId != data.getInt(ExcerciseListFragment.COL_EX_ID)){
                    exId = data.getInt(ExcerciseListFragment.COL_EX_ID);
                    view = rootInflater.inflate(R.layout.ex_list_item,mListViewContainer,false);
                    vHolder = new ViewHolder(view);
                }
                // Ex #
                String numb = data.getString(ExcerciseListFragment.COL_EX_NUMB);
                if(vHolder.numbView != null) {
                    vHolder.numbView.setText(numb);
                }

                // Ex Name
                String exName = data.getString(ExcerciseListFragment.COL_EX_NAME);
                if(vHolder.nameView != null) {
                    vHolder.nameView.setText(exName);
                }

                mListViewContainer.addView(view);

            }while (data.moveToNext());


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    public void setWrkID(int id) {
        mWrkID = id;
    }

    public static class ViewHolder {
        public final TextView numbView;
        public final TextView nameView;

        public ViewHolder(View view) {
            numbView = (TextView) view.findViewById(R.id.ex_numb_textview);
            nameView = (TextView) view.findViewById(R.id.ex_name_textview);
        }
    }

}
