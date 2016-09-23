package com.android.prasnou.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

/**
 * Created by Dzianis_Prasnou on 9/15/2016.
 */
public class AddExActivity extends AppCompatActivity {

    // radio group listener
    static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);
            }
        }
    };

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


    public void onClickSetType(View view) {
        RadioGroup rg = (RadioGroup)view.getParent();
        rg.check(view.getId());
    }

    /* presnov
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_WRK_KEY, mSelectedWrkId);
        super.onSaveInstanceState(outState);
    }
    */

}
