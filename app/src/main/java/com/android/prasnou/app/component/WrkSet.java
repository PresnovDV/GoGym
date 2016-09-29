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
    private int mType = 0;
    private int mSelected = 0;

    /** state layouts */
    private final int LAYOUT_WARM = 10;
    private final int LAYOUT_WARM_SELECTED = 11;
    private final int LAYOUT_WORK = 20;
    private final int LAYOUT_WORK_SELECTED = 21;


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
        final LayoutParams lParams = (RelativeLayout.LayoutParams) new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(lParams);

        weightTextView = (TextView) rootView.findViewById(R.id.weightTextView);
        repsTextView = (TextView) rootView.findViewById(R.id.repsTextView);
        weightTextView.setText(String.valueOf(mWeight));
        repsTextView.setText(String.valueOf(mReps));
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
        this.setBackgroundResource(resolveLayout());
        invalidate();
        requestLayout();
    }

    public int getType(){
        return mType;
    }

    public int getSelected() {
        return mSelected;
    }

    public void setSelected(int isSelected) {
        mSelected = isSelected;
    }

    private int resolveLayout(){
        int layout = 0;

        switch (mType * 10 | mSelected){
            case(LAYOUT_WARM):
                layout = R.drawable.wrk_set_warm;
                break;
            case(LAYOUT_WARM_SELECTED):
                layout = R.drawable.wrk_set_warm_selected;
                break;
            case(LAYOUT_WORK):
                layout = R.drawable.wrk_set_work;
                break;
            case(LAYOUT_WORK_SELECTED):
                layout = R.drawable.wrk_set_work_selected;
                break;
        }

        return layout;
    }



}
