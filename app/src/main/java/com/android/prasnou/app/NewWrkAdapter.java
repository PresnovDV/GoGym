package com.android.prasnou.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.prasnou.app.component.WrkSet;

import java.util.List;

/**
 * Created by Dzianis_Prasnou on 9/27/2016.
 */
public class NewWrkAdapter extends ArrayAdapter<NewWrkDataObject.Ex> {
    List<NewWrkDataObject.Ex> exList;

    public NewWrkAdapter(Context context, int resource, List<NewWrkDataObject.Ex> objects) {
        super(context, resource, objects);
        exList = objects;
    }


    private void recalcIndexes() {
        for (int i = 0; i < exList.size(); i++) {
            NewWrkDataObject.Ex ex = exList.get(i);
            ex.setExInd(i);
            ex.setExNumb(i + 1);
        }
    }

    @Override
    public void add(NewWrkDataObject.Ex object) {
        super.add(object);
        recalcIndexes();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout exView = null;

        final NewWrkDataObject.Ex ex = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // ---------- add ex Item -----------
        exView = (LinearLayout) inflater.inflate(R.layout.new_wrk_ex_item, parent, false);
        LinearLayout setContainer = (LinearLayout) exView.findViewById(R.id.set_container);

        // Ex #
        TextView numbView = (TextView) exView.findViewById(R.id.ex_numb_textview);
        if(numbView != null) {
            numbView.setText(String.valueOf(ex.getExNumb()));
        }

        // Ex Name
        TextView nameView = (TextView) exView.findViewById(R.id.ex_name_textview);
        if(nameView != null) {
            nameView.setText(ex.getExName());
        }

        // ----------- add sets -----------

        for(NewWrkDataObject.Set set : ex.getExSetList()){
            WrkSet setItem = new WrkSet(getContext());
            setItem.setType(1);
            setItem.setWeight(set.getSetWeight());
            setItem.setReps(set.getSetReps());

            StringBuilder sbTag = new StringBuilder(ex.getExNumb()).append(":").append(set.getSetNumb());
            setItem.setTag(sbTag.toString());
            setContainer.addView(setItem);
        }

        //------------------ Swipe to remove ----------------------

        final NewWrkAdapter newExAdapter = this;
        exView.setOnTouchListener(new View.OnTouchListener() {
            float dX, rawX;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        rawX = event.getRawX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (event.getRawX() < rawX) {
                            view.animate()
                                    .x(event.getRawX() + dX)
                                    .setDuration(0)
                                    .start();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (-0.6 * view.getWidth() < view.getX()) {
                            view.animate().x(0).setDuration(50).start();
                        } else {
                            newExAdapter.remove(ex);
                            recalcIndexes();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }

        });
        return exView;
    }

}
