package com.android.prasnou.app;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link ExAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class ExAdapter extends CursorAdapter {

    public ExAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.ex_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Ex #
        String numb = cursor.getString(ExcerciseListFragment.COL_EX_NUMB);
        if(viewHolder.numbView != null) {
            viewHolder.numbView.setText(numb);
        }

        // Ex Name
        String exName = cursor.getString(ExcerciseListFragment.COL_EX_NAME);
        if(viewHolder.nameView != null) {
            viewHolder.nameView.setText(exName);
        }

    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView numbView;
        public final TextView nameView;

        public ViewHolder(View view) {
            numbView = (TextView) view.findViewById(R.id.ex_numb_textview);
            nameView = (TextView) view.findViewById(R.id.ex_name_textview);
        }
    }
}


/*                LinearLayout exItem = (LinearLayout)view.findViewById(R.id.ex_item);

                exItem.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                WrkSet set = new WrkSet(context);
                set.setWeight(123);
                set.setReps(8);
                set.setTag("ex1.set1");

                exItem.addView(set);

                WrkSet set1 = new WrkSet(context);
                set1.setWeight(12);
                set1.setReps(2);
                exItem.addView(set1);

                ////
                WrkSet mySet = (WrkSet)view.findViewWithTag("ex1.set1");
                if(mySet != null) {
                    mySet.setReps(111);
                }
                ////*/