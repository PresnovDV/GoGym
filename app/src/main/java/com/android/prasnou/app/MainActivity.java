package com.android.prasnou.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements WorkoutListFragment.Callback{
    private static final String SELECTED_WRK_KEY = "selected_wrk";
    private int mSelectedWrkId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_WRK_KEY)) {
            mSelectedWrkId = savedInstanceState.getInt(SELECTED_WRK_KEY);
        }
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_wrk) {
            startActivity(new Intent(this, com.android.prasnou.app.AddWorkoutActivity.class));
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_WRK_KEY, mSelectedWrkId);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onItemSelected(int wrkId) {
        if(mSelectedWrkId == wrkId){
            mSelectedWrkId = -1;
        }
        else {
            mSelectedWrkId = wrkId;
        }
    }

    public int getSelectedWrkId(){
        return mSelectedWrkId;
    }
}
