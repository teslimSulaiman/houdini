package com.example.user.houdini;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.user.houdini.data.HoudiniContract;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddActivity extends AppCompatActivity {

    @Bind(R.id.headerName)
    EditText headerName;
    private String TABLE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (intent != null) {

            //Bundle extras = intent.getExtras();
            String header = intent.getStringExtra("header");
            headerName.setHint(header);
        }
    }




    public void onSave(View v) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                ContentValues values = new ContentValues();
                String header = headerName.getText().toString();
                values.put(HoudiniContract.StateEntry.COLUMN_TITLE, header);

                Intent intent = getIntent();

                if (intent != null) {

                    //Bundle extras = intent.getExtras();
                    String headerLabel = intent.getStringExtra("header");
                    if (headerLabel.equalsIgnoreCase("states")){
                        Uri uri = getContentResolver().insert(HoudiniContract.StateEntry.CONTENT_URI, values);
                    }

                }

                 Intent manageInfoIntent = new Intent(getApplicationContext(), ManageInfo.class);
                manageInfoIntent.putExtra("header",getIntent().getStringExtra("header"));
                startActivity(manageInfoIntent);
                 finish();  //Kill the activity from which you will go to next activity
                 startActivity(manageInfoIntent);

            }
        });
    }
    private boolean validateInput(String input){
        if(TextUtils.isEmpty(input)) return false;
        return true;
    }
}
