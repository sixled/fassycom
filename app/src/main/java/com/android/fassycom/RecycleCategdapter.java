package com.android.fassycom;

import android.appcilsa.R;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RecycleCategdapter extends RecyclerView.Adapter<RecycleCategdapter.ViewHolder> {
    private Context context;
    ArrayList<CategoriaObject> listacateg;
    int Tamano;
    public RecycleCategdapter(ArrayList<CategoriaObject> mCateg, int Tamaño) {
        this.listacateg=mCateg; this.Tamano=Tamaño;
    }
    LinearLayout olderbar;

    @NonNull
    @Override
    public RecycleCategdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_categorias, parent, false);
        // Return a new view holder

        return new ViewHolder(view);
    }
    Boolean delete=false;

    @Override
    public void onBindViewHolder(@NonNull final RecycleCategdapter.ViewHolder holder, final int position) {
        holder.mTexto.setText(listacateg.get(position).nombre);
        Glide.with(holder.itemView.getContext()).load(listacateg.get(position).foto).apply(new RequestOptions().override(Tamano).fitCenter()).into(holder.imageview);
         holder.BarView.setId(position);
         if(position==0){
           // al iniciar la categorias frecuentes se hace visible su barra
           holder.BarView.setVisibility(View.VISIBLE);
           olderbar=holder.BarView;
           delete=true;
         }
      holder.mItemview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          // cuando hace click en las categorias mostrar la barra y ocultar la antigua visible si delete es tue
          if(delete){
            olderbar.setVisibility(View.INVISIBLE);
            delete=false;
          }
          holder.BarView.setVisibility(View.VISIBLE);
          olderbar=holder.BarView;
          delete=true;
        }
      });
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
