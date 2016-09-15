package com.android.prasnou.app.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.prasnou.app.R;

/**
 * To add component
 <com.android.prasnou.app.component.WrkSet
     android:layout_width="wrap_content"
     android:layout_height="wrap_content"
     style="@style/WrkSetWarm"/>
  */
public class WrkSet extends RelativeLayout{

    View rootView;
    TextView weightTextView;
    TextView repsTextView;

    private int mWeight = 0;
    private int mReps = 0;
    private int mType = -1;

    public WrkSet(Context context) {

        super(context);
        init(context);
    }

    public WrkSet(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.wrk_set, this);
        rootView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        weightTextView = (TextView) rootView.findViewById(R.id.weightTextView);
        repsTextView = (TextView) rootView.findViewById(R.id.repsTextView);
        weightTextView.setText(String.valueOf(mWeight));
        repsTextView.setText(String.valueOf(mReps));

        this.setBackgroundResource(R.drawable.wrk_set_warm);
    }

    public void setWeight(int weight){
        mWeight = weight;
        weightTextView.setText(String.valueOf(mWeight));
        invalidate();
        requestLayout();
    }

    public int getWeight(){
        return mWeight;
    }

    public void setReps(int reps){
        mReps = reps;
        repsTextView.setText(String.valueOf(mReps));
        invalidate();
        requestLayout();
    }

    public int getReps(){
        return mReps;
    }

    public void setType(int type){
        mType = type;
        // presnov
        invalidate();
        requestLayout();
    }

    public int getType(){
        return mType;
    }

}
