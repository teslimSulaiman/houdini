package com.example.user.houdini;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.houdini.data.HoudiniContract;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.headerName)
    EditText headerName;
    @Bind(R.id.btn_update)
    Button update;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null) {

            //Bundle extras = intent.getExtras();
           String headerLabel = intent.getStringExtra("header");
            int  id = intent.getIntExtra("id",1);
            if (headerLabel.equalsIgnoreCase("states")){
                GetTaskInfo getTaskInfo = new GetTaskInfo();
                getTaskInfo.execute(id);
            }

        }

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               updateInfo();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            int  stateId = getIntent().getIntExtra("id",1);
            deleteInfo(stateId);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateInfo(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                Intent intent = getIntent();

                if (intent != null) {

                    ContentValues values = new ContentValues();
                    String header = headerName.getText().toString();
                    values.put(HoudiniContract.StateEntry.COLUMN_TITLE, header);

                    //Bundle extras = intent.getExtras();
                    final Uri taskUri = getIntent().getData();

                    // COMPLETED (2) Delete a single row of data using a ContentResolver
                         getContentResolver().update(taskUri, values, null, null);

                }

                Intent manageInfoIntent = new Intent(getApplicationContext(), ManageInfo.class);
                manageInfoIntent.putExtra("header",getIntent().getStringExtra("header"));
                startActivity(manageInfoIntent);
                finish();  //Kill the activity from which you will go to next activity
                startActivity(manageInfoIntent);

            }
        });


    }

    private void deleteInfo(int stateId) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                Intent intent = getIntent();

                if (intent != null) {

                    //Bundle extras = intent.getExtras();
                    final Uri taskUri = getIntent().getData();

                    // COMPLETED (2) Delete a single row of data using a ContentResolver
                    getContentResolver().delete(taskUri, null, null);


                }

                Intent manageInfoIntent = new Intent(getApplicationContext(), ManageInfo.class);
                manageInfoIntent.putExtra("header",getIntent().getStringExtra("header"));
                startActivity(manageInfoIntent);
                finish();  //Kill the activity from which you will go to next activity
                startActivity(manageInfoIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    private class GetTaskInfo extends AsyncTask<Integer, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Integer ... uris) {
            try {
                return getContentResolver().query(HoudiniContract.StateEntry.CONTENT_URI,
                        null,
                        HoudiniContract.StateEntry._ID + "=" + uris[0],
                        null,
                        null);

            } catch (Exception e) {
                //Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursor mCursor) {
            mCursor.moveToFirst();
            int id = mCursor.getColumnIndex(HoudiniContract.StateEntry.COLUMN_TITLE);
            String   title  = mCursor.getString(id);
            headerName.setText(title);

        }
    }
}
