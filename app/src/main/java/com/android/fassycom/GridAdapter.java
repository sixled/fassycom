package com.android.fassycom;

import android.appcilsa.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;

/**
 * Created by sixled on 13/10/2017.
 */

public class GridAdapter extends BaseAdapter{
    ArrayList<Pictograma> pictoGridview;



    public Context context;
    public LayoutInflater inflater;
    public int  tamaño=0;
    public GridAdapter(Context context,ArrayList<Pictograma> pictoGridview,int tamaño) {
        this.pictoGridview = pictoGridview;
        this.context = context;
        this.tamaño=tamaño;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public ArrayList<Pictograma> getPictoGridview() {
        return pictoGridview;
    }

    public void setPictoGridview(ArrayList<Pictograma> pictoGridview) {
        this.pictoGridview = pictoGridview;
    }

    @Override
    public int getCount() {
        return pictoGridview.size();
    }

    @Override
    public Object getItem(int position) {
        return pictoGridview.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        View gridview=convertview;
        if(convertview==null)
        {

            gridview=inflater.inflate(R.layout.griditem,null);

        }
        ImageView icon=(ImageView) gridview.findViewById(R.id.icon);
        TextView letter=(TextView) gridview.findViewById(R.id.letters);


        Glide.with(context)
                .load(pictoGridview.get(position).getFoto())
                .apply(new RequestOptions().override(tamaño))
                .into(icon);



       // icon.setImageBitmap(icons.get(position));
        letter.setText(pictoGridview.get(position).getNombrepic());
        return gridview;
    }

}
