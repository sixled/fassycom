package com.android.fassycom;

import android.app.Activity;
import android.appcilsa.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingMenu extends AppCompatActivity {
        Boolean rep;
    Boolean edition;
    String rotate="";
    DataSQL mDbHelper = new DataSQL(this);
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ImageButton save=findViewById(R.id.savebuton);
        final Switch reproducir =findViewById(R.id.reprocheck);
        final Switch edit=findViewById(R.id.switchedit);
        db = mDbHelper.getWritableDatabase();
//cargar editar
        Cursor us = db.rawQuery("SELECT edit,repro,rotar FROM usuario", null);
        if (us.moveToFirst()) {
            do {
                 edition = Boolean.valueOf(us.getString(0));
                 rep = Boolean.valueOf(us.getString(1));
                rotate =us.getString(2);
            } while (us.moveToNext());
        }
        us.close();
        ImageButton rotatescreen=findViewById(R.id.rotateScreen);
        reproducir.setChecked(rep);
        edit.setChecked(edition);
        rotatescreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int  value =getResources().getConfiguration().orientation;

                if (value == Configuration.ORIENTATION_PORTRAIT) {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    rotate="LANDSCAPE";
                }

                if (value == Configuration.ORIENTATION_LANDSCAPE) {
                    rotate="PORTRAIT";
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rep=reproducir.isChecked();
                edition=edit.isChecked();
                db.execSQL("UPDATE usuario SET repro='"+rep+"',edit='"+edition+"',rotar='"+rotate+"'");
                Intent resultseting=new Intent();
                resultseting.putExtra("repro",rep);
                resultseting.putExtra("edit",edition);
                resultseting.putExtra("rotate",rotate);
                setResult(Activity.RESULT_OK, resultseting);
                finish();
            }
        });
        reproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });



    }
    @Override
    public void onBackPressed()
    { final Switch reproducir =findViewById(R.id.reprocheck);
        final Switch edit=findViewById(R.id.switchedit);
        rep=reproducir.isChecked();
        edition=edit.isChecked();
        db.execSQL("UPDATE usuario SET repro='"+rep+"',edit='"+edition+"',rotar='"+rotate+"'");
        Intent resultseting=new Intent();
        resultseting.putExtra("repro",rep);
        resultseting.putExtra("edit",edition);
        resultseting.putExtra("rotate",rotate);
        setResult(Activity.RESULT_OK, resultseting);
        finish();
        super.onBackPressed();  // Invoca al método
    }
}
