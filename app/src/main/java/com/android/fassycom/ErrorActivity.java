package com.android.fassycom;

import android.appcilsa.R;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ErrorActivity extends AppCompatActivity {
    DataSQL mDbHelper = new DataSQL(this);
    String activityerror;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
       activityerror=getIntent().getExtras().getString("error");
           // TextView settext = (TextView) findViewById(R.id.showerror);
           // settext.setText(activityerror);
        }
        public void sendfassy(View v)
        {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","sixled17@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fassycom error");
            emailIntent.putExtra(Intent.EXTRA_TEXT, activityerror);
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
    public void oklist(View v)
    {
        finish();
        Intent ListSong = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ListSong);
    }
    }