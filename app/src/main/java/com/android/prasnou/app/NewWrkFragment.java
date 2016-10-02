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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.prasnou.app.data.DataContract;
import com.android.prasnou.app.data.DataContract.WorkoutTypeEntry;
import com.android.prasnou.app.data.DataProvider;


/**
 * Created by Dzianis_Prasnou on 9/1/2016.
 */
public class NewWrkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final int EX_EDIT_REQUEST_CODE = 0;

    private static final int WRK_INIT_LOADER_ID = 0;
    private final int WRK_TYPE_LOADER_ID = 10;

    private SimpleCursorAdapter spWrkTypeAdapter = null;
    private WrkDataObject mWrk = new WrkDataObject();
    private View rootView = null;
    private WrkAdapter wrkAdapter;
    Spinner spWrkType = null;

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
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_new_wrk, container, false);

        //---------- Workout Type spinner init -----------------------
        spWrkType = (Spinner) rootView.findViewById(R.id.sp_wrk_type);
        // start loader
        getLoaderManager().initLoader(WRK_TYPE_LOADER_ID, null, this);
        spWrkTypeAdapter=new SimpleCursorAdapter(getContext(),
                R.layout.sp_item,
                null,
                new String[]{WorkoutTypeEntry.COLUMN_NAME},
                new int[]{android.R.id.text1},
                0);
        spWrkTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWrkType.setAdapter(spWrkTypeAdapter);
        spWrkType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        CharSequence exTypeText = ((TextView)view).getText();
                        CharSequence  msg = "Wrk Type: " + ((TextView)view).getText();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        mWrk.setWrkTypeId(Integer.parseInt(Long.toString(id)));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getActivity(), "Select workout type", Toast.LENGTH_SHORT).show();

                    }
                });

        // init workout object presnov todo

        if(mWrk.getWrkNumb()>0){
            final TextView wrkNumb = (TextView)rootView.findViewById(R.id.wrk_numb_textview);
            wrkNumb.setText(getContext().getResources().getString(R.string.new_wrk_numb_prefix, mWrk.getWrkNumb()));
        }

        // -------  add ex button -----------
        ImageButton btnAddEx = (ImageButton) rootView.findViewById(R.id.btn_add_ex);
        if (btnAddEx != null) {
            btnAddEx.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editEx(-1);
                }
            });
        }

        // ------ Ex List init --------------
        final ListView exList = (ListView) rootView.findViewById(R.id.ex_list);

        wrkAdapter = new WrkAdapter(getContext(),R.id.ex_list, mWrk.getWrkExList());
        exList.setAdapter(wrkAdapter);
        //------------------------------------

        // --------------- Save Button --------------------
        Button btnReturn = (Button) rootView.findViewById(R.id.btn_save_wrk);
        if(btnReturn != null){
            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveWrk();
                }
            });
        }
        return rootView;
    }

    /**
     * creates new Ex and starts edit Ex activity
     * @param ind
     */
    private void editEx(int ind) {
        WrkDataObject.Ex ex = null;
        // get ex by ind (-1 - new)
        if(ind < 0){ // new - create empty ex
            ex = mWrk.newEx();
        }
        else {
            if(ind < mWrk.getExCount()) {
                ex = mWrk.getWrkExList().get(ind);
            }
        }
        // send ex as param
        Intent intent = new Intent(getActivity(), AddExActivity.class);
        intent.putExtra(AddWrkActivity.WRK_EX_PARAM, ex);
        startActivityForResult(intent, EX_EDIT_REQUEST_CODE);
    }

    /** handles results of edit Ex activity */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EX_EDIT_REQUEST_CODE
                && resultCode == Activity.RESULT_OK){

            WrkDataObject.Ex newEx = (WrkDataObject.Ex)data.getExtras().get(AddWrkActivity.WRK_EX_PARAM);
            int ind = newEx.getExInd();
            if(ind > -1 && ind < mWrk.getExCount()) {
                mWrk.getWrkExList().set(newEx.getExInd(), newEx);
            }
            else{
                wrkAdapter.add(newEx);
            }
            wrkAdapter.notifyDataSetChanged();
        }
    }

    // ---------------- Save WrkObject to DB ------------------
    private void saveWrk() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataProvider.WORKOUT_OBJ, mWrk);
        getContext().getContentResolver().call(DataContract.WorkoutEntry.CONTENT_URI,DataProvider.ADD_WORKOUT_MTHD,null,bundle);
    }


    // ---------------- Loaders -------------------------------
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
                case(WRK_INIT_LOADER_ID): // presnov finish todo
                    mWrk.setWrkTypeId(1); // follow up
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}


}

