package com.android.prasnou.app;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * {@link WrkAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class WrkAdapter extends CursorAdapter {

    private int mSelectedPosition = ListView.INVALID_POSITION;

    public WrkAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // view types
    private static final int VIEW_TYPE_DONE = 0;
    private static final int VIEW_TYPE_DRAFT = 1;

    private static final int VIEW_TYPE_COUNT = 2;

    @Override
    public int getItemViewType(int position) {
        int isDone = 1;// getCursor().getInt(); presnov todo
        return (isDone > 0) ? VIEW_TYPE_DONE : VIEW_TYPE_DRAFT;
    }


    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);

        if(mSelectedPosition == position)
        {
            return LayoutInflater.from(mContext).inflate(R.layout.wrk_list_item_selected, parent, false);
        }

        return super.getView(position, convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_DONE: {
                    layoutId = R.layout.wrk_list_item;
                    break;
            }
            case VIEW_TYPE_DRAFT: {
                layoutId = R.layout.wrk_list_item_draft;
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
        if(viewHolder.numbView != null) {
            viewHolder.numbView.setText(numb);
        }

        // Wrk Type
        String wrkType = cursor.getString(WorkoutListFragment.COL_WRK_TYPE);
        if(viewHolder.typeView != null) {
            viewHolder.typeView.setText(wrkType);
        }

        // Wrk Date
        Long wrkDate = Long.valueOf(cursor.getString(WorkoutListFragment.COL_WRK_DATE));
        if(viewHolder.dateView != null) {
            viewHolder.dateView.setText(Utils.formatDate(wrkDate));
        }

        // Wrk Result
        long wrkDuration = Long.valueOf(cursor.getString(WorkoutListFragment.COL_WRK_DURATION));
        if(viewHolder.resultView != null) {
            String wrkResult = Utils.wrkResult(wrkDuration,0);
            viewHolder.resultView.setText(wrkResult);
        }

        // Wrk Notes
        String wrkNotes = cursor.getString(WorkoutListFragment.COL_WRK_NOTES);
        if(viewHolder.noteView != null) {
            viewHolder.noteView.setText(wrkNotes);
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;
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