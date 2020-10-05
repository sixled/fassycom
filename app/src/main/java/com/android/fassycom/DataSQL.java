package com.android.fassycom;

import android.appcilsa.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

//Created by sixled on 15/10/2017.

public class DataSQL extends SQLiteOpenHelper{
    // version de la base de datos y nombre
    private static final int basededatos_version =59;
    private static final String database = "Fassycom.db";
    //variables
    private ArrayList<Pictograma> pic = new ArrayList<>();
    private ArrayList<Pictograma> picinstall = new ArrayList<>();
    private ArrayList<CategoriaObject> categbackup=new ArrayList<>();
    private CategoriaObject[] categ=new CategoriaObject[10];
    private String MEDIA_DIRECTORY = "/Fassycom/";
    private String path = Environment.getExternalStorageDirectory()+File.separator+"Android"+File.separator+"data"+File.separator+"com.app.fassycom";
    //Sentencia SQL para crear la tabla de usuario,categoria,pictograma
    private String sqlCreate = "CREATE TABLE categorias (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT,photo TEXT,base TEXT,usernom TEXT)";
    private String sqlCreate2 = "CREATE TABLE item (id INTEGER PRIMARY KEY AUTOINCREMENT,categ TEXT,nombre TEXT,audio TEXT,foto TEXT,base TEXT,uso TEXT,backup BOOLEAN)";
    private String sqlCreate3 = "CREATE TABLE usuario (instalacion BOOLEAN,  pasos INTEGER,repro BOOLEAN,edit BOOLEAN,rotar TEXT)";
    private Context context;

    public DataSQL(Context contexto) {
        super(contexto, database, null, basededatos_version);
        context = contexto;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//executando creacion de tablas
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate2);
        db.execSQL(sqlCreate3);
        //instalando categorias y pictogramas
        installcategorias(db);
        installPictogramas(db);
        //insertando datos en usuario
        db.execSQL("INSERT INTO usuario (instalacion,pasos,repro,edit,rotar) " + "VALUES ('false' , '1','false','false','false')");
        //checkear fassycomviejo
        checkimpot(db,path + "/Import");
    }

    public Boolean checkimpot(SQLiteDatabase db,String ruta) {
        Boolean checked = false;


        File directory = new File(ruta);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File inFile : files) {

                }
                if (files.length == 1) {
                    Log.d("Files", "vacio");
                } else {
                    checked = true;
                    insertpictogram(db,files);
                }
            }


        }
        return checked;
    }
    private  void insertpictogram(SQLiteDatabase db,File files[])
    {
        db.execSQL("INSERT INTO categorias (nombre,photo,base) " +
                "VALUES ('Externo' , '" + android.R.drawable.ic_menu_gallery + "', 'local')");


        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".jpg")) {
                String archivox = files[i].getName().replace(".jpg", "");
                String fileorigen = path + "/Import/" + archivox;
                String filedestino = path + "/Categories" + "/Imported/" + archivox;
                mover(fileorigen + ".jpg", filedestino + ".jpg");
                mover(fileorigen + ".ogg", filedestino + ".ogg");

                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('Externo','" + archivox + "' , '" + filedestino + ".jpg" + "','" + filedestino + ".ogg" + "','sdcard','0')");
           db.close();
            }
        }
    }
    public void mover(String rutavieja,String rutanueva) {
    File file = new File(rutavieja);
    if (file.exists())
    {
        File fileTo = new File(rutanueva);
         file.renameTo(fileTo);
    }else {
        Log.e("File","no se encuentra el archivode origen");
    }
}
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo_categoria vacía con el nuevo_categoria formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.
        //Se elimina la versión anterior de la tabla

;
        //backup pictogramas
        Cursor cl = db.rawQuery(" SELECT categ,nombre,foto,base,audio FROM item Where base='sdcard'", null);
        if (cl.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do { String catego = cl.getString(0);
                String nombre = cl.getString(1);
             String   foto = cl.getString(2);

                String base = cl.getString(3);
            String     audio = cl.getString(4);
            pic.add(new Pictograma(catego,nombre,foto,audio,base));
                Log.e("guardado pic", "asdasd");

            }
            while (cl.moveToNext());
        }
        cl.close();
        //backup categorias

        Cursor cs = db.rawQuery(" SELECT nombre,photo FROM categorias Where base='sdcard'", null);
        if (cs.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre = cs.getString(0);
                String   foto2 = cs.getString(1);
                categbackup.add(new CategoriaObject(nombre,foto2,"ninguno"));
                Log.e("guardado categorias", "asdasd");

            }
            while (cs.moveToNext());
        }
         cs.close();


        db.execSQL("DROP TABLE  IF EXISTS usuario");
        db.execSQL("DROP TABLE  IF EXISTS item");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        db.execSQL(sqlCreate);
        Log.i("craete table  categoria","pic33");
        db.execSQL(sqlCreate2);
        Log.i("craete table item","pic33");
        db.execSQL(sqlCreate3);
        Log.i("craete table user","pic33");
        installcategorias(db);
        installPictogramas(db);

        Log.i("craete instalacion","pic33");
        for (int th=0;th<categbackup.size();th++)
              {   db.execSQL("INSERT INTO categorias (nombre,photo,base) " +
                      "VALUES ('"+categbackup.get(th).nombre+"', +'"+categbackup.get(th).foto+"','sdcard')");
              }
        for (int xh=0;xh<pic.size();xh++)
        {
            db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                    "VALUES ('"+pic.get(xh).categ+"','"+pic.get(xh).nombrepic+"','"+pic.get(xh).foto+"','"+pic.get(xh).audio+"','"+pic.get(xh).basex+"','0')");
        }
        db.execSQL("INSERT INTO usuario (instalacion,pasos,repro,edit,rotar) " + "VALUES ('true' , '7','false','false','false')");
        Log.i("craete insert user","pic33");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void DeleteItem(SQLiteDatabase dba, String nombre, Context context, String categoria)
    {
        File borraritem=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY +File.separator + "Categories"+ File.separator+categoria+File.separator+nombre+".jpg");
        File borraritemaudio=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY +File.separator + "Categories"+ File.separator+categoria+File.separator+nombre+".ogg");
        if(borraritem.exists()&& borraritemaudio.exists())
          {
              borraritem.delete();
              borraritemaudio.delete();
          }
        Toasty.success(context, "Borrado Pictograma "+ nombre, Toast.LENGTH_SHORT, true).show();
            dba.execSQL("DELETE FROM item where nombre='"+ nombre+"'");
           dba.close();
    }


    public void Deletecategoria(SQLiteDatabase dba,String nombre,Context context)
    {
        File borrarcategoria=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + "Categories"+ File.separator+nombre);
        File borrarfotocategoria=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + "Categories"+ File.separator+nombre+".jpg");
        if (borrarcategoria.exists())
        {if(borrarfotocategoria.delete())
            { deletefolder(borrarcategoria);
            }

        }
        dba.execSQL("DELETE FROM categorias where nombre='"+nombre+"'");
        Toasty.success(context, "Borrado Categoría "+ nombre, Toast.LENGTH_SHORT, true).show();
        dba.close();
    }
    public void deletefolder(File folder)
    {   if(folder.exists())
     {
        String[] children = folder.list();
        if(children!=null){
        for (String aChildren : children) {
            new File(folder, aChildren).delete();
            folder.delete();
        }}
     }

    }
    public void Insertpictograma(SQLiteDatabase dba,Context context,String selecionadacategoria,String nombre,Object foto,Object audio){
        DataSQL mDbHelper = new DataSQL(context);
        dba=mDbHelper.getWritableDatabase();
        dba.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " + "VALUES ('" + selecionadacategoria + "','" + nombre + "' , '" + foto + "' ,'" + audio + "','sdcard','0')");
        Log.i("ingresado item:  ", "categoria: " + selecionadacategoria + "nombre: " + nombre + " y foto: " + foto + ",audio" + audio);
        dba.close();
    }

    private void installPictogramas(SQLiteDatabase db)
    { String categn=context.getString(R.string.categnom1);
        for(int k=1;k<=83;k++){
            //guardando pictogramas
            switch (k)
            {
                case 15:
                    //cargar  de categoria
                    categn=context.getString(R.string.categnom2);
                    break;
                case 28:
                    categn=context.getString(R.string.categnom3);
                    break;
                case 33:
                    categn=context.getString(R.string.categnom4);
                    break;
                case 35:
                    categn=context.getString(R.string.categnom5);
                    break;
                case 47:
                    categn=context.getString(R.string.categnom6);
                    break;
                case 59:
                    categn=context.getString(R.string.categnom7);
                    break;
                case 63:
                    categn=context.getString(R.string.categnom8);
                    break;
                case 67:
                    categn=context.getString(R.string.categnom9);
                    break;
                case 73:
                    categn=context.getString(R.string.categnom10);
                    break;

            }
            picinstall.add(new Pictograma(categn,context.getString(rcovert("pic"+k,"string")),rcovert("pic"+k,"drawable"),rcovert("pic"+k,"raw"),"local"));
        }
        for(int h=0;h<picinstall.size();h++){
            //insertando pictogramas a la base de datos
            db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                    "VALUES ('"+picinstall.get(h).getCateg()+"','" +picinstall.get(h).getNombrepic()+ "' , " +picinstall.get(h).getFoto()+ "  ,'"+picinstall.get(h).getAudio()+"','local','0')");
       }
    }

    private void installcategorias(SQLiteDatabase db){

        for (int j = 0; j <=9; j++) {
            int c=j+1;
            Log.i("datacateg","R.string.categnom"+c);
            String nomcateg = context.getString(rcovert("categnom"+c,"string"));
            categ[j] =new CategoriaObject(nomcateg,rcovert("categlist"+c,"drawable"),"ninguno");
            db.execSQL("INSERT INTO categorias (nombre,photo,base) " +
                    "VALUES ('" +categ[j].getNombre()+ "' , '" + categ[j].getFoto() + "', 'local')");
        }
    }
    public int rcovert(String nombre,String tipe)
    {
        int covertido=context.getResources().getIdentifier(nombre,tipe,context.getPackageName());
        return covertido;
    }
    public static Boolean isnumeric(String cadena){
        try {Integer.parseInt(cadena);
            return true;
        }catch (NumberFormatException nfe){return false;}


    }

}
