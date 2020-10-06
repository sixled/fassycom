package com.android.fassycom;

import androidx.annotation.NonNull;

public class CategoriaObject  {
    String nombre;
    Object foto;
    String tipo;


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public CategoriaObject(String nombre, Object foto, String tipo) {
        this.nombre = nombre;
        this.foto = foto;
        this.tipo = tipo;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Object getFoto() {
        return foto;
    }

    public void setFoto(Object foto) {
        this.foto = foto;
    }
}
