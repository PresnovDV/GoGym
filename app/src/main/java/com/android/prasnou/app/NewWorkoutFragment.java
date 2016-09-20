package com.android.prasnou.app;


import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.prasnou.app.data.DataContract;
import com.android.prasnou.app.data.DataContract.WorkoutTypeEntry;


/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class NewWorkoutFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final int EX_EDIT_REQUEST_CODE = 0;

    private static final int WRK_INIT_LOADER_ID = 0;
    private final int WRK_TYPE_LOADER_ID = 10;
    private final int EX_TYPE_LOADER_ID = 11;

    private SimpleCursorAdapter spWrkTypeAdapter = null;
    private NewWorkoutDataObject newWorkout  = new NewWorkoutDataObject();
    private View rootView = null;

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
        getLoaderManager().initLoader(WRK_TYPE_LOADER_ID, null, this);
        getLoaderManager().initLoader(EX_TYPE_LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_new_wrk, container, false);

        // Workout Type spinner init
        Spinner spWrkType = (Spinner) rootView.findViewById(R.id.sp_wrk_type);
        spWrkTypeAdapter=new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{WorkoutTypeEntry.COLUMN_NAME},
                new int[]{android.R.id.text1},
                0);
        spWrkTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWrkType.setAdapter(spWrkTypeAdapter);
        /*
        // change type
        spWrkType.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onClick(View view) {
                newWorkout.setWrkTypeId(2);
            }
        });
*/
        // init workout object
        if(newWorkout.getWrkNumb()>0){
            TextView wrkNumb = (TextView) rootView.findViewById(R.id.wrk_numb_textview);
            wrkNumb.setText(R.string.wrk_numb_prefix + newWorkout.getWrkNumb());
        }

        // add ex button
        ImageButton btnAddEx = (ImageButton) rootView.findViewById(R.id.btn_add_ex);
        if (btnAddEx != null) {
            btnAddEx.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout exList = (LinearLayout) rootView.findViewById(R.id.ex_list);
                    editEx(-1);
                }
            });
        }

        return rootView;
    }

    private void editEx(int ind) {
        NewWorkoutDataObject.Ex ex = null;
        // get ex by ind (-1 - new)
        if(ind < 0){ // new - create empty ex
            ex = newWorkout.newEx();
            ex.setExInd(newWorkout.getWrkExList().size());
        }
        else {
            if(ind < newWorkout.getExCount()) {
                ex = newWorkout.getWrkExList().get(ind);
            }
        }
        // send ex as param
        Intent intent = new Intent(getActivity(), com.android.prasnou.app.AddExActivity.class);
        intent.putExtra(AddWorkoutActivity.WRK_EX_PARAM, ex);
        startActivityForResult(intent, EX_EDIT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EX_EDIT_REQUEST_CODE
                && resultCode == Activity.RESULT_OK){

            NewWorkoutDataObject.Ex newEx = (NewWorkoutDataObject.Ex)data.getExtras().get(AddWorkoutActivity.WRK_EX_PARAM);
            newWorkout.getWrkExList().set(newEx.getExInd(), newEx);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] select = {};
        String orderBy = null;
        Uri uri = null;

        switch (id) {
            case (WRK_TYPE_LOADER_ID): {
                select = WRK_TYPE_LIST_COLUMNS;
                orderBy = WorkoutTypeEntry._ID + " ASC";
                uri = WorkoutTypeEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).build();
            }
        }
        return new CursorLoader(getActivity(), uri, select, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            switch (loader.getId()){
                case(WRK_TYPE_LOADER_ID):
                    spWrkTypeAdapter.changeCursor(data);
                    break;
                case(WRK_INIT_LOADER_ID): // presnov finish
                    newWorkout.setWrkTypeId(1); // folow up
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

//    public static class ViewHolder {
//        public final TextView numbView;
//        public final TextView nameView;
//
//        public ViewHolder(View view) {
//            numbView = (TextView) view.findViewById(R.id.ex_numb_textview);
//            nameView = (TextView) view.findViewById(R.id.ex_name_textview);
//        }
//    }

}

