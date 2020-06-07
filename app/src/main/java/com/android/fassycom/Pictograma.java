package com.android.fassycom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Pictograma {
    String categ ;
    String nombrepic ;
    Object foto ;
    Object audio;
    String basex;
    SQLiteDatabase db;

    public Pictograma(String categ,String nombrepic, Object foto, Object audio, String basex) {
        this.categ =categ;
        this.nombrepic = nombrepic;
        this.foto = foto;
        this.audio = audio;
        this.basex = basex;
    }
    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public String getNombrepic() {
        return nombrepic;
    }

    public Pictograma setNombrepic(String nombrepic) {
        this.nombrepic = nombrepic;
        return this;
    }

    public Object getFoto() {
        return foto;
    }

    public Pictograma setFoto(Object foto) {
        this.foto = foto;
        return this;
    }


    public Object getAudio() {
        return audio;
    }

    public Pictograma setAudio(String audio) {
        this.audio = audio;
        return this;
    }

    public String getBasex() {
        return basex;
    }

    public Pictograma setBasex(String basex) {
        this.basex = basex;
        return this;
    }
    public void Insertpictograma(Context context,String selecionadacategoria,String nombre){
        DataSQL mDbHelper = new DataSQL(context);
        db=mDbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " + "VALUES ('" + selecionadacategoria + "','" + nombre + "' , '" + foto + "' ,'" + audio + "','sdcard','0')");
        Log.i("ingresado item:  ", "categoria: " + selecionadacategoria + "nombre: " + nombre + " y foto: " + foto + ",audio" + audio);
        db.close();
    }
}
