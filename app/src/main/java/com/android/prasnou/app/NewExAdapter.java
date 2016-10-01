package com.android.prasnou.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dzianis_Prasnou on 9/27/2016.
 */
public class NewExAdapter extends ArrayAdapter<WrkDataObject.Set> {
    List<WrkDataObject.Set> setList;
    public NewExAdapter(Context context, int resource, List<WrkDataObject.Set> objects) {
        super(context, resource, objects);
        setList = objects;
    }



    private void recalcIndexes(){
        for(int i = 0; i<setList.size(); i++){
            WrkDataObject.Set set = setList.get(i);
            set.setInd(i);
            set.setSetNumb(i+1);
        }
    }

    @Override
    public void add(WrkDataObject.Set object) {
        super.add(object);
        recalcIndexes();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View setView = null;

        final WrkDataObject.Set set = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        setView = inflater.inflate(R.layout.new_ex_set_item, parent, false);

        TextView set_numb = (TextView)setView.findViewById(R.id.set_numb_textview);
        if(set_numb != null){
            set_numb.setText(String.valueOf(set.getSetNumb()));
        }

        final TextView set_weight = (TextView)setView.findViewById(R.id.set_weight_textview);
        if(set_weight != null){
            set_weight.setText(String.valueOf(set.getSetWeight()));
        }

        final TextView set_reps = (TextView)setView.findViewById(R.id.set_reps_textview);
        if(set_reps != null){
            set_reps.setText(String.valueOf(set.getSetReps()));
        }

        final NewExAdapter newExAdapter = this;
        setView.setOnTouchListener(new View.OnTouchListener() {
            float dX, rawX;
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        rawX = event.getRawX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if(event.getRawX() < rawX) {
                            view.animate()
                                    .x(event.getRawX() + dX)
                                    .setDuration(0)
                                    .start();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if(-0.6 * view.getWidth() < view.getX()){
                            view.animate().x(0).setDuration(50).start();
                        }
                        else{
                            newExAdapter.remove(set);
                            recalcIndexes();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }

        });
        return setView;
    }
}
