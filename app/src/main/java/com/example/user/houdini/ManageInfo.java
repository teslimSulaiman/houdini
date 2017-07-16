package com.example.user.houdini;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import com.example.user.houdini.adapter.InfoAdapter;
import com.example.user.houdini.data.HoudiniContract;

public class ManageInfo extends AppCompatActivity implements
        InfoAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_LOADER = 1;
    private static final String TAG = ManageInfo.class.getSimpleName();
    private InfoAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_info);

        mAdapter = new InfoAdapter(null, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.info);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().initLoader(TASK_LOADER, null, this);

         /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                // COMPLETED (1) Construct the URI for the item to delete
                //[Hint] Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = HoudiniContract.StateEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // COMPLETED (2) Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);


                // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
                //getSupportLoaderManager().initLoader(TASK_LOADER, null,);
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor cursor;
            @Override
            protected void onStartLoading() {
                forceLoad();
            }
            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                if (getIntent() != null) {

                    //Bundle extras = intent.getExtras();
                    String headerLabel = getIntent().getStringExtra("header");

                    if (headerLabel.equalsIgnoreCase("states")){
                         cursor = getContentResolver().query(HoudiniContract.StateEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
                    }else cursor = null;

                }


                return cursor;

            }
        };
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER, null, this);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Cursor cursor = mAdapter.getItem(clickedItemIndex);
        cursor.moveToPosition(clickedItemIndex);
        int id = cursor.getColumnIndex(HoudiniContract.StateEntry._ID);
        int   bookPathId = cursor.getInt(id);
        Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
        String headerLabel = getIntent().getStringExtra("header");
        intent.putExtra("id", bookPathId);
        intent.putExtra("header", headerLabel);
        String stringId = cursor.getString(id);
        Uri uri = HoudiniContract.StateEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        // COMPLETED (2) Delete a single row of data using a ContentResolver
        intent.setData(uri);
        startActivity(intent);
    }
}
