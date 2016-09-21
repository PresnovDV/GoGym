package com.android.prasnou.app;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.prasnou.app.data.DataContract;
import com.android.prasnou.app.data.DataContract.ExcerciseEntry;

import java.util.Map;


/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class NewExFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final int EX_TYPE_LOADER_ID = 0;
    private SimpleCursorAdapter spExTypeAdapter = null;
    private NewWorkoutDataObject.Ex mWrkEx = null;

    //*************** Excercise Type List Cols ***********************
    private static final String[] EX_TYPE_LIST_COLUMNS = {
            DataContract.ExcerciseEntry._ID,
            DataContract.ExcerciseEntry.COLUMN_NAME
    };
    static final int COL_EX_TYPE_ID = 0;
    static final int COL_EX_TYPE_NAME = 1;
    private Map<Integer, String> exTypes = null;
    //******************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent inputIntent = getActivity().getIntent();
        mWrkEx = (NewWorkoutDataObject.Ex) inputIntent.getExtras().get(AddWorkoutActivity.WRK_EX_PARAM);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(EX_TYPE_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_new_ex, container, false);

        final TextView exNumb = (TextView)rootView.findViewById(R.id.ex_numb_textview);

        // Ex Type spinner init
        Spinner spExType = (Spinner) rootView.findViewById(R.id.sp_ex_type);
        spExTypeAdapter=new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{ExcerciseEntry.COLUMN_NAME},
                new int[]{android.R.id.text1},
                0);
        spExTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExType.setAdapter(spExTypeAdapter);

        //------ Number pickers -------------
        // Weight
        Resources res = this.getResources();
        NumberPicker npWeight = (NumberPicker) rootView.findViewById(R.id.np_weight);
        npWeight.setWrapSelectorWheel(true);
        String[] weightValues = getDisplayValues(res.getInteger(R.integer.weight_min),
                res.getInteger(R.integer.weight_max), res.getInteger(R.integer.weight_step));
        npWeight.setMinValue(0);
        npWeight.setMaxValue(weightValues.length-1);
        npWeight.setDisplayedValues(weightValues);
        npWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String[] values = numberPicker.getDisplayedValues();
                if(i1 < values.length ) {
                    String newVal = values[i1];
                    exNumb.setText(newVal);
                }
            }
        });

        // Reps
        NumberPicker npReps = (NumberPicker) rootView.findViewById(R.id.np_reps);
        String[] repsValues = getDisplayValues(res.getInteger(R.integer.reps_min),
                res.getInteger(R.integer.reps_max), res.getInteger(R.integer.reps_step));
        npReps.setMinValue(0);
        npReps.setMaxValue(repsValues.length-1);
        npReps.setDisplayedValues(repsValues);
        npReps.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String[] values = numberPicker.getDisplayedValues();
                if(i1 < values.length) {
                    String newVal = values[i1];
                    exNumb.setText(newVal);
                }
            }
        });

        //-----------------------------------





        //textView.setText(String.valueOf(mWrkEx.getExNumb()));

        Button btnReturn = null; // (Button) rootView.findViewById(R.id.btn_return);
        if(btnReturn != null){
            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendResult();
                }
            });
        }
        return rootView;
    }

    private String[] getDisplayValues(int minVal, int maxVal, int step) {
        int steps  = 1+(maxVal-minVal)/step;
        String[] res = new String[steps];
        for(int i = 0; i<steps; i++){
            res[i]=String.valueOf(minVal+i*step);
        }
        return res;
    }

    /** send result back to new Workout fragment */
    public void sendResult(){
        Intent intent = new Intent();
        intent.putExtra(AddWorkoutActivity.WRK_EX_PARAM, mWrkEx);
        if (getActivity().getParent() == null) {
            getActivity().setResult(Activity.RESULT_OK, intent);
        } else {
            getActivity().getParent().setResult(Activity.RESULT_OK, intent);
        }
        getActivity().finish();
    }

    // WTF?? presnov
    private void addEx(LayoutInflater inflater, LinearLayout exList) {
        View item = inflater.inflate(R.layout.ex_list_item_edit, exList, false);
        TextView item_numb = (TextView)item.findViewById(R.id.ex_numb_textview);
        if(item_numb != null){
            item_numb.setText(exList.getChildCount()+1);
        }
        Spinner item_name = (Spinner) item.findViewById(R.id.ex_name_spinner);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] select = {};
        String orderBy = null;
        Uri uri = null;

        switch (id) {
            case (EX_TYPE_LOADER_ID): {
                select = EX_TYPE_LIST_COLUMNS;
                orderBy = ExcerciseEntry.COLUMN_NAME + " ASC";
                uri = ExcerciseEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).build();
            }
        }
        return new CursorLoader(getActivity(), uri, select, null, null, orderBy);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            switch (loader.getId()) {
                case (EX_TYPE_LOADER_ID):
                    spExTypeAdapter.changeCursor(data);
                    break;
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

