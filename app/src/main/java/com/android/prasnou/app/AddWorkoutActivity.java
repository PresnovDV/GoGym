package com.android.prasnou.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Dzianis_Prasnou on 9/15/2016.
 */
public class AddWorkoutActivity extends AppCompatActivity {
    public static final String WRK_EX_PARAM = "ex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* presnov
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_WRK_KEY)) {
            mSelectedWrkId = savedInstanceState.getInt(SELECTED_WRK_KEY);
        }
        */
        setContentView(R.layout.activity_add_wrk);
    }


    /* presnov
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_WRK_KEY, mSelectedWrkId);
        super.onSaveInstanceState(outState);
    }
    */
}
