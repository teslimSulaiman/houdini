package com.example.user.houdini;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                try {
                    return getContentResolver().query(HoudiniContract.StateEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
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

    }
}
