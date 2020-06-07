package com.android.fassycom;

import android.appcilsa.R;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;

public class RecycleCategdapter extends RecyclerView.Adapter<RecycleCategdapter.ViewHolder> {
    private Context context;
ArrayList<CategoriaObject> listacateg;
    int Tamano;
    public RecycleCategdapter(ArrayList<CategoriaObject> mCateg, int Tamaño) {
        this.listacateg=mCateg; this.Tamano=Tamaño;
    }


    @NonNull
    @Override
    public RecycleCategdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_categorias, parent, false);
        // Return a new view holder

        return new ViewHolder(view);
    }
    Boolean delete=false;
    int hold_id;
    @Override
    public void onBindViewHolder(@NonNull final RecycleCategdapter.ViewHolder holder, final int position) {
        holder.mTexto.setText(listacateg.get(position).nombre);
        Glide.with(holder.itemView.getContext()).load(listacateg.get(position).foto).apply(new RequestOptions().override(Tamano).fitCenter()).into(holder.imageview);
         holder.BarView.setId(position);

    }
    @Override
    public int getItemCount() {
        return listacateg.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


       public ImageView imageview;
       public TextView mTexto;
       LinearLayout BarView;
        LinearLayout mItemview;
       public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.pictogramadapter);
            BarView=itemView.findViewById(R.id.barrview);
            mTexto=itemView.findViewById(R.id.textodapter);
            mItemview =itemView.findViewById(R.id.itemadapt);

        }
    }
}
