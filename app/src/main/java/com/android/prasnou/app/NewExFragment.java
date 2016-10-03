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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.prasnou.app.data.DataContract;
import com.android.prasnou.app.data.DataContract.ExTypeEntry;

import java.util.Map;



/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class NewExFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final int EX_TYPE_LOADER_ID = 0;
    private SimpleCursorAdapter spExTypeAdapter = null;
    private WrkDataObject.Ex mWrkEx = null;
    private Spinner spExType = null;

    //*************** Excercise Type List Cols ***********************
    private static final String[] EX_TYPE_LIST_COLUMNS = {
            ExTypeEntry._ID,
            ExTypeEntry.COLUMN_NAME
    };
    static final int COL_EX_TYPE_ID = 0;
    static final int COL_EX_TYPE_NAME = 1;
    private Map<Integer, String> exTypes = null;
    //******************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent inputIntent = getActivity().getIntent();
        mWrkEx = (WrkDataObject.Ex) inputIntent.getExtras().get(AddWrkActivity.WRK_EX_PARAM);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_new_ex, container, false);

        final TextView exNumb = (TextView)rootView.findViewById(R.id.ex_numb_textview);
        exNumb.setText(getContext().getResources().getString(R.string.new_ex_numb_prefix, mWrkEx.getExNumb()));

        //---------- Ex Type spinner init -----------------------
        spExType = (Spinner) rootView.findViewById(R.id.sp_ex_type);
        // start loader
        getLoaderManager().initLoader(EX_TYPE_LOADER_ID, null, this);
        spExTypeAdapter=new SimpleCursorAdapter(getContext(),
                R.layout.sp_item,
                null,
                new String[]{ExTypeEntry.COLUMN_NAME},
                new int[]{android.R.id.text1},
                0);
        spExTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExType.setAdapter(spExTypeAdapter);
        spExType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        CharSequence exTypeText = ((TextView)view).getText();
                        CharSequence  msg = "Ex Type: " + ((TextView)view).getText();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        mWrkEx.setExTypeId(Integer.parseInt(Long.toString(id)));
                        mWrkEx.setExName((String)exTypeText);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getActivity(), "Select excercise type", Toast.LENGTH_SHORT).show();

                    }
                });

        // ------ Set List init --------------
        final ListView setList = (ListView) rootView.findViewById(R.id.set_list);

        final NewExAdapter newExAdapter = new NewExAdapter(getContext(),R.id.set_list, mWrkEx.getExSetList());
        setList.setAdapter(newExAdapter);

        //----------- editors --------
        // next set number
        final TextView vSetNumb = (TextView) rootView.findViewById(R.id.header_set_numb_texview);
        vSetNumb.setText(String.valueOf(1));

        final EditText edWeight = (EditText) rootView.findViewById(R.id.header_set_weight_edit);
        final EditText edReps = (EditText) rootView.findViewById(R.id.header_set_reps_edit);

        //----------- Add Set button --------
        ImageButton btnAddSet = (ImageButton) rootView.findViewById(R.id.btn_add_set);
        if(btnAddSet != null){
            btnAddSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                int weight = 0;
                int reps = 0;
                if(edWeight.getText().length()>0){
                    weight = Integer.parseInt(edWeight.getText().toString());
                }
                if(edReps.getText().length()>0){
                    reps = Integer.parseInt(edReps.getText().toString());
                }
                newExAdapter.add(mWrkEx.newSet(weight, reps));
                newExAdapter.notifyDataSetChanged();

                // update next set number
                vSetNumb.setText(String.valueOf(mWrkEx.getExSetList().size()+1));
                }
            });
        }

        // --------------- Save Button --------------------
        Button btnReturn = (Button) rootView.findViewById(R.id.btn_save_ex);
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

    /** send result back to new Workout fragment */
    public void sendResult(){
        Intent intent = new Intent();
        intent.putExtra(AddWrkActivity.WRK_EX_PARAM, mWrkEx);
        if (getActivity().getParent() == null) {
            getActivity().setResult(Activity.RESULT_OK, intent);
        } else {
            getActivity().getParent().setResult(Activity.RESULT_OK, intent);
        }
        getActivity().finish();
    }

    // ---------- Loaders ------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] select = {};
        String orderBy = null;
        Uri uri = null;

        switch (id) {
            case (EX_TYPE_LOADER_ID): {
                select = EX_TYPE_LIST_COLUMNS;
                orderBy = ExTypeEntry.COLUMN_NAME + " ASC";
                uri = ExTypeEntry.CONTENT_URI.buildUpon().appendPath(DataContract.PATH_LIST).build();
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
                    int exPos = Utils.getAdapterItemPositionById(spExTypeAdapter, mWrkEx.getExTypeId());
                    if(exPos != -1){
                        spExType.setSelection(exPos,true);
                    }
                    break;
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

