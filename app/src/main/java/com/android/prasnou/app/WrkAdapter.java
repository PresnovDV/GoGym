package com.android.prasnou.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link WrkAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class WrkAdapter extends CursorAdapter {
    private boolean mIsSelectedWorkout;

    public WrkAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // view types
    private static final int VIEW_TYPE_DETAILED = 0;
    private static final int VIEW_TYPE_REGULAR = 1;
//    private static final int VIEW_TYPE_COUNT = 2; todo

    /*public void setUseDetailedLayout(boolean isSpecTodayLayout){
        mIsSelectedWorkout = isSpecTodayLayout;
    }*/


    @Override
    public int getItemViewType(int position) {

        return (mIsSelectedWorkout) ? VIEW_TYPE_DETAILED : VIEW_TYPE_REGULAR;
    }

    /*
    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
*/
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_DETAILED: {
                    layoutId = R.layout.wrk_list_item_selected;
                    break;
            }
            case VIEW_TYPE_REGULAR: {
                    layoutId = R.layout.wrk_list_item;
                    break;
            }
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Wrk #
        String numb = cursor.getString(WorkoutListFragment.COL_WRK_NUMBER);
        viewHolder.numbView.setText(numb);

        // Wrk Type
        String wrkType = cursor.getString(WorkoutListFragment.COL_WRK_TYPE);
        viewHolder.typeView.setText(wrkType);

        // Wrk Date
        String wrkDate = cursor.getString(WorkoutListFragment.COL_WRK_DATE);
        viewHolder.dateView.setText(wrkDate);

        // Wrk Result
        String wrkResult = cursor.getString(WorkoutListFragment.COL_WRK_DURATION);
        viewHolder.resultView.setText(wrkResult);

        // Wrk Notes
        String wrkNotes = cursor.getString(WorkoutListFragment.COL_WRK_NOTES);
        viewHolder.noteView.setText(wrkNotes);
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView numbView;
        public final TextView typeView;
        public final TextView dateView;
        public final TextView resultView;
        public final TextView noteView;

        public ViewHolder(View view) {
            numbView = (TextView) view.findViewById(R.id.wrk_numb_textview);
            typeView = (TextView) view.findViewById(R.id.wrk_type_textview);
            dateView = (TextView) view.findViewById(R.id.wrk_date_textview);
            resultView = (TextView) view.findViewById(R.id.wrk_result_textview);
            noteView = (TextView) view.findViewById(R.id.wrk_note_textview);

        }
    }
}