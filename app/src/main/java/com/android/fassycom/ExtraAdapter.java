package com.android.fassycom;

import android.appcilsa.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by sixled on 13/10/2017.
 */

public class ExtraAdapter extends BaseAdapter{
    public ArrayList<Integer> icons;
    public Context context;
    public LayoutInflater inflater;

    public ExtraAdapter(Context context, ArrayList<Integer> icons) {
        this.icons = icons;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int position) {
        return icons.get(position);
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

            gridview=inflater.inflate(R.layout.griditem2,null);

        } ImageView icon=(ImageView) gridview.findViewById(R.id.icon);
        Glide.with(context).load(icons.get(position)).apply(new RequestOptions().fitCenter()).into(icon);
      //  icon.setImageBitmap(icons.get(position));
        return gridview;
    }
}
