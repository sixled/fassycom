package com.android.fassycom;

import android.app.Activity;
import android.appcilsa.R;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;

public class Mas_foto extends AppCompatActivity {
    public ArrayList<Integer> lettersIcons = new ArrayList<>();
    int result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mas_foto);
Log.i("create","actibiti");
        final ImageButton addimage  = findViewById(R.id.addimage);
        for (int x=1; x<=72; x++)
        {
            String imageName = "img"+x;
            int resID = getResources().getIdentifier(imageName , "drawable", getPackageName());
            lettersIcons.add(resID);
        }

        final GridView gridViews = (GridView) findViewById(R.id.gridview);
        ExtraAdapter gridAdapter = new ExtraAdapter (getApplicationContext(), lettersIcons);
        gridViews.setAdapter(gridAdapter);

        gridViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                addimage.setVisibility(View.VISIBLE);
                String source = (Long.valueOf(id)).toString();
                int number=Integer.parseInt(source)+1;
                String obtenernom="img"+number;
                result=getResources().getIdentifier(obtenernom , "drawable", getPackageName());

            }
        });
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                Log.i("toca","finish2");
            }
        });
    }


}
