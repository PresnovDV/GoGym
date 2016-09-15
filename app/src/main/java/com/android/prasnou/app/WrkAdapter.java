package com.android.prasnou.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WrkAdapter extends CursorAdapter {

    public WrkAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // view types
    private static final int VIEW_TYPE_DONE = 0;
    private static final int VIEW_TYPE_DRAFT = 1;
    private static final int VIEW_TYPE_DETAILED_DONE = 10;
    private static final int VIEW_TYPE_DETAILED_DRAFT = 11;

    @Override
    public int getItemViewType(int position) {
        int currWrkId = ((Cursor)getItem(position)).getInt(WorkoutListFragment.COL_WRK_ID);
        int selectedWrkId = ((MainActivity)mContext).getSelectedWrkId();
        boolean isDone = ((Cursor)getItem(position)).getLong(WorkoutListFragment.COL_WRK_DATE)>0;

        if(selectedWrkId == currWrkId){
            return (isDone) ? VIEW_TYPE_DETAILED_DONE : VIEW_TYPE_DETAILED_DRAFT;
        }
        else {
            return (isDone) ? VIEW_TYPE_DONE : VIEW_TYPE_DRAFT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = null;
        return super.getView(position, convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = null;
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case VIEW_TYPE_DONE: {
                    view = inflater.inflate(R.layout.wrk_list_item, parent, false);
                    break;
            }
            case VIEW_TYPE_DRAFT: {
                view = inflater.inflate(R.layout.wrk_list_item_draft, parent, false);
                break;
            }
            case VIEW_TYPE_DETAILED_DONE: {
                view = inflater.inflate(R.layout.wrk_list_item_detailed, parent, false);
                break;
            }
            case VIEW_TYPE_DETAILED_DRAFT: {
                view = inflater.inflate(R.layout.wrk_list_item_detailed_draft, parent, false);
                break;
            }
        }

        if(view != null) {
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        view.setTag(R.id.fr_wrk_list,cursor.getInt(WorkoutListFragment.COL_WRK_ID));

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

    /**
     * Cache of the children views
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