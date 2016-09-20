package com.android.prasnou.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Dzianis_Prasnou on 9/15/2016.
 */
public class AddExActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* presnov
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_WRK_KEY)) {
            mSelectedWrkId = savedInstanceState.getInt(SELECTED_WRK_KEY);
        }
        */
        setContentView(R.layout.activity_add_ex);
    }



    /* presnov
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_WRK_KEY, mSelectedWrkId);
        super.onSaveInstanceState(outState);
    }
    */

}
