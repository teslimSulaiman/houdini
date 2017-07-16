package com.example.user.houdini.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.houdini.R;
import com.example.user.houdini.data.HoudiniContract;
import com.example.user.houdini.model.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 7/16/2017.
 */



import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 6/3/2017.
 */

public  class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {

    private static final String TAG = InfoAdapter.class.getSimpleName();

    private List<Info> books = new ArrayList<Info>();
    private Context context;
    final private ListItemClickListener mOnClickListener;
    private Cursor mCursor;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public InfoAdapter(Context context, ListItemClickListener listener ) {

        this.context = context;
        mOnClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.info_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(HoudiniContract.StateEntry._ID);
        int posterIndex = mCursor.getColumnIndex(HoudiniContract.StateEntry.COLUMN_TITLE);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String posterPath = mCursor.getString(posterIndex);
        holder.itemView.setTag(id);
        holder.textView.setText(posterPath);


    }

    public Cursor getItem(int id) {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.infoId);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
