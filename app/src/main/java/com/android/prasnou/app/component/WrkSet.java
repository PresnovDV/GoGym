package com.android.prasnou.app.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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
public class WrkSet extends RelativeLayout {
    View rootView;
    TextView weightTextView;
    TextView repsTextView;

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
        weightTextView = (TextView) rootView.findViewById(R.id.weightTextView);
        repsTextView = (TextView) rootView.findViewById(R.id.repsTextView);
        weightTextView.setText("100");
        repsTextView.setText("11");

        this.setBackgroundResource(R.drawable.wrk_set_warm);

    }

}
