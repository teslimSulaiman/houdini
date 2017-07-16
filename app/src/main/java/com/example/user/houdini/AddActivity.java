package com.example.user.houdini;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.houdini.data.HoudiniContract;
import com.example.user.houdini.data.TaskUpdateService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddActivity extends AppCompatActivity {

    @Bind(R.id.headerName)
    EditText headerName;

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
        ContentValues values = new ContentValues();
        String header = headerName.getText().toString();
        values.put(HoudiniContract.StateEntry.COLUMN_TITLE, header);

        if(validateInput(header)){
            TaskUpdateService.insertNewTask(this, values);
            Toast.makeText(this, "done",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ManageInfo.class);
            finish();  //Kill the activity from which you will go to next activity
            startActivity(i);
           // finish();
        }
        else {
            Toast.makeText(this, "empty string",
                    Toast.LENGTH_LONG).show();
        }
    }
    private boolean validateInput(String input){
        if(TextUtils.isEmpty(input)) return false;
        return true;
    }
}
