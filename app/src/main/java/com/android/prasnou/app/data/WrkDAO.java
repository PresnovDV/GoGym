package com.android.prasnou.app.data;

import android.content.ContentValues;

import com.android.prasnou.app.WrkDataObject;
import com.android.prasnou.app.data.DataContract.WorkoutEntry;

/**
 * Created by Dzianis_Prasnou on 9/30/2016.
 */
public class WrkDAO {

    public static void addWrk(WrkDataObject wrkDObj){

        // insert to wrk
        ContentValues wrkValues = new ContentValues();
        wrkValues.put(WorkoutEntry.COLUMN_DATE, 0);
        wrkValues.put(WorkoutEntry.COLUMN_NUMBER, wrkDObj.getWrkNumb());
        wrkValues.put(WorkoutEntry.COLUMN_WRK_TYPE_ID, wrkDObj.getWrkTypeId());


// insert to wrk_ex presnov todo
        /*ex
        wrk_id
                ex_id
        ex_numb

// insert to wrk_ex_set
                id
        wrk_ex_id
                set_numb
        set_type_id
                set_weight
        set_reps*/
    }
}
