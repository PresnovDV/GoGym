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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.prasnou.app.component.WrkSet;
import com.android.prasnou.app.data.DataContract;
import com.android.prasnou.app.data.DataContract.ExcerciseEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExSetEntry;

/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class ExcerciseListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int WRK_EX_LIST_LOADER_ID = 0;
    private LinearLayout mListViewContainer;
    private LayoutInflater rootInflater;

    //*************** Workout List Cols ***********************
    private static final String[] WRK_EX_LIST_COLUMNS = {
            WorkoutExEntry.TABLE_NAME + "." + WorkoutExEntry._ID,
            WorkoutExEntry.COLUMN_WRK_ID,
            WorkoutExEntry.TABLE_NAME + "." + WorkoutExEntry.COLUMN_EX_ID,
            WorkoutExEntry.COLUMN_EX_NUMB,
            ExcerciseEntry.TABLE_NAME + "." + ExcerciseEntry.COLUMN_NAME,
            WorkoutExSetEntry.TABLE_NAME + "." + WorkoutExSetEntry._ID,
            WorkoutExSetEntry.COLUMN_SET_NUMB,
            WorkoutExSetEntry.COLUMN_SET_TYPE_ID,
            WorkoutExSetEntry.COLUMN_SET_WEIGHT,
            WorkoutExSetEntry.COLUMN_SET_REPS
    };


    static final int COL_ID = 0;
    static final int COL_WRK_ID = 1;
    static final int COL_EX_ID = 2;
    static final int COL_EX_NUMB = 3;
    static final int COL_EX_NAME = 4;
    static final int COL_SET_ID = 5;
    static final int COL_SET_NUMB = 6;
    static final int COL_SET_TYPE = 7;
    static final int COL_SET_WEIGHT = 8;
    static final int COL_SET_REPS = 9;

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
        int mWrkID = ((MainActivity)getActivity()).getSelectedWrkId();
        if(mWrkID > 0) {
            String[] select = WRK_EX_LIST_COLUMNS;
            String orderBy = WorkoutExEntry.COLUMN_EX_NUMB + " ASC";
            Uri uri = DataContract.WorkoutExSetEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).appendPath(String.valueOf(mWrkID)).build();

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
            LinearLayout exItem = null;
            ViewHolder vHolder = null;

            do{
                if(exId != data.getInt(ExcerciseListFragment.COL_EX_ID)){
                    exId = data.getInt(ExcerciseListFragment.COL_EX_ID);
                    exItem = (LinearLayout)rootInflater.inflate(R.layout.wrk_ex_list_item,mListViewContainer,false);
                    vHolder = new ViewHolder(exItem);

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
                    mListViewContainer.addView(exItem);
                }

                // add set

                //exItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                if(data.getInt(ExcerciseListFragment.COL_SET_ID)>0) {
                    WrkSet set = new WrkSet(getContext());

                    set.setWeight(data.getInt(ExcerciseListFragment.COL_SET_WEIGHT));
                    set.setReps(data.getInt(ExcerciseListFragment.COL_SET_REPS));
                    StringBuilder setTag = new StringBuilder(data.getString(ExcerciseListFragment.COL_EX_NUMB)).append(":")
                            .append(data.getString(ExcerciseListFragment.COL_SET_NUMB));
                    set.setTag(setTag.toString());
                    set.setType(data.getInt(ExcerciseListFragment.COL_SET_TYPE));

                    exItem.addView(set);
                }
            }while (data.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    public static class ViewHolder {
        public final TextView numbView;
        public final TextView nameView;

        public ViewHolder(View view) {
            numbView = (TextView) view.findViewById(R.id.ex_numb_textview);
            nameView = (TextView) view.findViewById(R.id.ex_name_textview);
        }
    }

}
