package com.android.prasnou.app;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.prasnou.app.data.DataContract;
import com.android.prasnou.app.data.DataContract.WorkoutTypeEntry;


/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class NewWorkoutFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final int SP_WRK_TYPE_LOADER_ID = 10;
    private final int SP_WRK_TEMPL_LOADER_ID = 11;
    SimpleCursorAdapter spWrkTypeAdapter = null;
    SimpleCursorAdapter spWrkTemplAdapter = null;

    //*************** Workout Type List Cols ***********************
    private static final String[] WRK_TYPE_LIST_COLUMNS = {
            WorkoutTypeEntry._ID,
            WorkoutTypeEntry.COLUMN_NAME
    };
    static final int COL_WRK_TYPE_ID = 0;
    static final int COL_WRK_TYPE_NAME = 1;
    //******************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SP_WRK_TYPE_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_new_wrk, container, false);

        // Workout Type
        Spinner spWrkType = (Spinner) rootView.findViewById(R.id.sp_wrk_type);
        spWrkTypeAdapter=new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{WorkoutTypeEntry.COLUMN_NAME},
                new int[]{android.R.id.text1},
                0);
        spWrkTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWrkType.setAdapter(spWrkTypeAdapter);

        // Template list Presnov
/*        Spinner spWrkTempl = (Spinner) rootView.findViewById(R.id.sp_template);
        spWrkTemplAdapter=new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{WorkoutTypeEntry.COLUMN_NAME},????
                new int[]{android.R.id.text1},
                0);
        spWrkTemplAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWrkTempl.setAdapter(spWrkTemplAdapter);
*/

        // footer
        Button btnAddEx = (Button) rootView.findViewById(R.id.btn_add_ex);
        if (btnAddEx != null) {
            btnAddEx.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout exList = (LinearLayout) rootView.findViewById(R.id.ex_list);
                    addEx(inflater, exList);
                }
            });
        }
        return rootView;
    }

    private void addEx(LayoutInflater inflater, LinearLayout exList) {
        View item = inflater.inflate(R.layout.ex_list_item_edit, exList, false);
        TextView item_numb = (TextView)item.findViewById(R.id.ex_numb_textview);
        if(item_numb != null){
            item_numb.setText(exList.getChildCount()+1);
        }
        Spinner item_name = (Spinner) item.findViewById(R.id.ex_name_spinner);
    }

    /* init template list refresh */
    private void queryTemplates(int wrkType){
        Bundle args = new Bundle();
        args.putInt(WorkoutTypeEntry.TABLE_NAME+"."+WorkoutTypeEntry._ID,wrkType);
        if(getLoaderManager().getLoader(SP_WRK_TEMPL_LOADER_ID) == null) {
            getLoaderManager().initLoader(SP_WRK_TEMPL_LOADER_ID, args, this);
        }
        else{
            getLoaderManager().restartLoader(SP_WRK_TEMPL_LOADER_ID, args, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] select = {};
        String orderBy = null;
        Uri uri = null;


        switch (id) {
            case (SP_WRK_TYPE_LOADER_ID): {
                select = WRK_TYPE_LIST_COLUMNS;
                orderBy = WorkoutTypeEntry._ID + " ASC";
                uri = WorkoutTypeEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).build();
            }
            case (SP_WRK_TEMPL_LOADER_ID): {
                //select = WRK_TEMPL_LIST_COLUMNS;
                //orderBy = WorkoutTypeEntry._ID + " ASC";
                //uri = WorkoutEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).build();
            }
        }

        return new CursorLoader(getActivity(), uri, select, null, null, orderBy);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            switch (loader.getId()){
                case(SP_WRK_TYPE_LOADER_ID):
                    spWrkTypeAdapter.changeCursor(data);
                    //queryTemplates(data.getInt(NewWorkoutFragment.COL_WRK_TYPE_ID));
                    break;
                case(SP_WRK_TEMPL_LOADER_ID):
                    spWrkTemplAdapter.changeCursor(data);
                    break;
            }

/*
            int exId = -1;
            LinearLayout exItem = null;
            ViewHolder vHolder = null;

            do{
                if(exId != data.getInt(ExcerciseListFragment.COL_EX_ID)){
                    exId = data.getInt(ExcerciseListFragment.COL_EX_ID);
                    exItem = (LinearLayout)rootInflater.inflate(R.layout.ex_list_item,mListViewContainer,false);
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
*/
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

