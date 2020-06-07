package com.android.fassycom;

import android.appcilsa.R;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;

public class RecyclePlaydapter extends RecyclerView.Adapter<RecyclePlaydapter.ViewHolder> {
    private Context context;
ArrayList<Pictograma> listapictograma;
int Tamano;
    public RecyclePlaydapter(ArrayList<Pictograma> mPic, int Tamaño) {
        this.listapictograma=mPic;
        this.Tamano=Tamaño;
    }


    @NonNull
    @Override
    public RecyclePlaydapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_play, parent, false);
        // Return a new view holder

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclePlaydapter.ViewHolder holder, final int position) {
        holder.mTexto.setText(listapictograma.get(position).nombrepic);
        Glide.with(holder.itemView.getContext()).load(listapictograma.get(position).foto).apply(new RequestOptions().override(Tamano)).into(holder.imageview);
        holder.mItemview.setId(6500+position);
    }

    @Override
    public int getItemCount() {
        return listapictograma.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{


       public ImageButton imageview;
       public TextView mTexto;
        LinearLayout mItemview;
       public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.imageviewadapter2);
            mTexto=itemView.findViewById(R.id.textodapterx);
            mItemview =itemView.findViewById(R.id.itemadapter2);

        }
    }
}
