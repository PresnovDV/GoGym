package com.android.prasnou.app.data;

import android.content.ContentValues;
import android.net.Uri;

import com.android.prasnou.app.WrkDataObject;
import com.android.prasnou.app.data.DataContract.WorkoutEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExEntry;
import com.android.prasnou.app.data.DataContract.WorkoutExSetEntry;

import java.util.List;

/**
 * Created by Dzianis_Prasnou on 9/30/2016.
 */
public class WrkDAO {

    public static void addWrk(WrkDataObject wrkDObj){
        DataProvider dp = new DataProvider();

        // insert to wrk
        ContentValues wrkValues = new ContentValues();
        wrkValues.put(WorkoutEntry.COLUMN_DATE, 0);
        wrkValues.put(WorkoutEntry.COLUMN_NUMBER, wrkDObj.getWrkNumb());
        wrkValues.put(WorkoutEntry.COLUMN_WRK_TYPE_ID, wrkDObj.getWrkTypeId());
        Uri wrkResURI = dp.insert(WorkoutEntry.CONTENT_URI, wrkValues);
        int wrkId = Integer.parseInt(WorkoutEntry.getWrkIdFromUri(wrkResURI));

        // insert to wrk_ex
        if(wrkId > 0) {
            for (WrkDataObject.Ex ex : wrkDObj.getWrkExList()) {
                ContentValues wrkExValues = new ContentValues();
                wrkExValues.put(WorkoutExEntry.COLUMN_WRK_ID, wrkId);
                wrkExValues.put(WorkoutExEntry.COLUMN_EX_ID, ex.getExInd());
                wrkExValues.put(WorkoutExEntry.COLUMN_EX_NUMB, ex.getExNumb());

                Uri wrkExResURI = dp.insert(WorkoutExEntry.CONTENT_URI, wrkExValues);
                int wrkExId = Integer.parseInt(WorkoutExEntry.getWrkExIdFromUri(wrkExResURI));

                // insert to wrk_ex_set
                if(wrkExId > 0) {
                    for (WrkDataObject.Set set : ex.getExSetList()) {
                        ContentValues wrkExSetValues = new ContentValues();
                        wrkExSetValues.put(WorkoutExSetEntry.COLUMN_WRK_EX_ID, wrkExId);
                        wrkExSetValues.put(WorkoutExSetEntry.COLUMN_SET_NUMB, set.getSetNumb());
                        wrkExSetValues.put(WorkoutExSetEntry.COLUMN_SET_WEIGHT, set.getSetWeight());
                        wrkExSetValues.put(WorkoutExSetEntry.COLUMN_SET_REPS, set.getSetReps());

                        dp.insert(WorkoutExSetEntry.CONTENT_URI, wrkExSetValues);
                    }
                }
            }
        }

    }
}
