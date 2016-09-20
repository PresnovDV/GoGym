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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.prasnou.app.data.DataContract;

import java.util.Map;


/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class NewExFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final int EX_TYPE_LOADER_ID = 11;
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
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_new_ex, container, false);
        TextView textView = (TextView)rootView.findViewById(R.id.ex_textview);

        textView.setText(String.valueOf(mWrkEx.getExNumb()));

        Button btnReturn = (Button) rootView.findViewById(R.id.btn_return);
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
                orderBy = DataContract.ExcerciseEntry.COLUMN_NAME + " ASC";
                uri = DataContract.ExcerciseEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).build();
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

