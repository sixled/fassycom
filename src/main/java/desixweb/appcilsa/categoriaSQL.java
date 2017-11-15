package desixweb.appcilsa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

//Created by sixled on 15/10/2017.

public class categoriaSQL extends SQLiteOpenHelper {
    private static final int basededatos_version =2;
    private static final String database = "Fassycom.db";

  private   String[] categoriaslist = {"Yo quiero", "Me siento", "Personas", "Si/No", "Alimentos", "Ropa", "Clima", "Cuándo", "Dónde","Cuantos"};
   private Integer[] categoriaphotolist = {R.drawable.yoquiero_1, R.drawable.mesiento_1, R.drawable.personas_1, R.drawable.sino_1,
            R.drawable.alimentos_1, R.drawable.ropa_1, R.drawable.clima_1, R.drawable.cuando_1,
            R.drawable.donde_1,R.drawable.cuantos_1};
//items yo quiero
   private String [] itemlist1={"Quiero","No quiero","Me gusta","No me gusta","Comer","Tomar","Dormir",
            "Ir al baño","Bañarme","Ir a","Comprar","Ver televisión","Jugar","Jugar video juegos"};

   private   Integer[] itemphotolist1={R.drawable.yoquiero_1,R.drawable.yoquiero_no_quiero,R.drawable.yoquiero_megusta,
            R.drawable.yoquiero_nomegusta,R.drawable.yoquiero_comer,R.drawable.yoquiero_tomar,R.drawable.yoquiero_dormir,R.drawable.yoquiero_iralbano,
            R.drawable.yoquiero_banarme,R.drawable.yoquiero_ira,R.drawable.yoquiero_comprar,R.drawable.yoquiero_vertelevision,R.drawable.yoquiero_jugar,R.drawable.yoquiero_jugarvideojuegos};

  private Integer []audiolist1={R.raw.quiero,R.raw.noquiero,R.raw.megusta,R.raw.nomegusta,R.raw.comer,R.raw.tomar,R.raw.dormir,R.raw.iralbano,R.raw.banarme,R.raw.ira,R.raw.comprar,R.raw.vertelevision,R.raw.jugar,R.raw.jugarvideojuegos};
  //items mesiento
  private   String [] itemlist2={"Me siento","Contento","Enojado","Asustado","Triste","Mareado","Con frío",
            "Con calor","Con fiebre","Con dolor","Con dolor de espalda","Con dolor de estómago","Con dolor de muelas"};
  private   Integer[] itemphotolist2={R.drawable.mesiento_1,R.drawable.mesiento_contento,R.drawable.mesiento_enojado,
            R.drawable.mesiento_asustado,R.drawable.mesiento_triste,R.drawable.mesiento_mareado,R.drawable.mesiento_confrio,R.drawable.mesiento_concalor,
            R.drawable.mesiento_confiebre,R.drawable.mesiento_condolor,R.drawable.mesiento_condolordeespalda,R.drawable.mesiento_condolordeestomago,R.drawable.mesiento_condolordemuelas};

    private Integer []audiolist2={R.raw.mesiento,R.raw.contento,R.raw.enojado,R.raw.asustado,R.raw.triste,R.raw.mareado,R.raw.confrio,R.raw.concalor,R.raw.confiebre,R.raw.condolor,R.raw.condolordeespalda,R.raw.condolordeestomago,R.raw.condolordemuela};
  //items personas
private String [] itemlist3={"Yo","Extraño a","Quiero a","Quiero hablar con","Viene"};
  private   Integer[] itemphotolist3={R.drawable.personas_yo,R.drawable.personas_extranoa,R.drawable.personas_quieroa,R.drawable.personas_hablarcon,R.drawable.personas_viene,};
    private Integer []audiolist3={R.raw.yo,R.raw.extranoa,R.raw.quieroa,R.raw.quierohablarcon,R.raw.viene};
  //items si no
private String [] itemlist4={"Si","No"};
    private Integer[] itemphotolist4={R.drawable.sino_si,R.drawable.sino_no,};
    private Integer []audiolist4={R.raw.si,R.raw.no};
//alimentos
private String [] itemlist5={"Me gusta","No me gusta","Pan","Agua","Leche","Carne","Pollo",
        "Empanada","Tomate","Gaseosa","Helado","Torta"};
    private   Integer[] itemphotolist5={R.drawable.alimentos_megusta,R.drawable.alimentos_nomegusta,R.drawable.alimentos_pan,
            R.drawable.alimentos_agua,R.drawable.alimentos_leche,R.drawable.alimentos_carne,R.drawable.alimentos_pollo,R.drawable.alimentos_empanada,
            R.drawable.alimentos_tomate,R.drawable.alimentos_cocacola,R.drawable.alimentos_helado,R.drawable.alimentos_torta};
    private Integer []audiolist5={R.raw.megusta,R.raw.nomegusta,R.raw.pan,R.raw.agua,R.raw.leche,R.raw.carne,R.raw.pollo,R.raw.empanada,R.raw.tomate,R.raw.gaseosa,R.raw.helado,R.raw.torta};
//ropa

    private String [] itemlist6 ={"Ropa","Ponerme","Ponerme el abrigo","Sacarme el abrigo","Remera","Pantalón","Camisa",
            "Campera","Bufanda","Gorro","Guantes","Pijama"};
    private   Integer[] itemphotolist6 ={R.drawable.ropa_1,R.drawable.ropa_ponerme,R.drawable.ropa_ponermeelabrigo,
            R.drawable.ropa_sacarmeelabrigo,R.drawable.ropa_remera,R.drawable.ropa_pantalon,R.drawable.ropa_camisa,R.drawable.ropa_campera,
            R.drawable.ropa_bufanda,R.drawable.ropa_gorro,R.drawable.ropa_guantes,R.drawable.ropa_pijama};

    private Integer []audiolist6={R.raw.ropa,R.raw.ponerme,R.raw.ponermeelabrigo,R.raw.sacarmeelabrigo,R.raw.remera,R.raw.pantalon,R.raw.camisa,R.raw.campera,R.raw.bufanda,R.raw.gorro,R.raw.guante,R.raw.pijama};

//clima
private String [] itemlist7 ={"Hay sol","Llueve","Hay tormenta","Hay viento"};
    private   Integer[] itemphotolist7 ={R.drawable.clima_hay_sol,R.drawable.clima_llueve,R.drawable.clima_tormenta,
            R.drawable.clima_viento};

    private Integer []audiolist7={R.raw.haysol,R.raw.llueve,R.raw.haytormenta,R.raw.hayviento};

//cuando
private String [] itemlist8 ={"Cuándo","Hoy","Ayer","Mañana"};
    private   Integer[] itemphotolist8 ={R.drawable.cuando_1,R.drawable.cuando_hoy,R.drawable.cuando_ayer,R.drawable.cuando_manana,};

    private Integer []audiolist8={R.raw.cuando,R.raw.hoy,R.raw.ayer,R.raw.manana};
//donde
private String [] itemlist9 ={"Dónde","Casa","Escuela","Hospital","Estar","Ir"};
    private   Integer[] itemphotolist9 ={R.drawable.donde_1,R.drawable.donde_casa,R.drawable.donde_escuela,
            R.drawable.donde_hospital,R.drawable.donde_estar,R.drawable.donde_ir,};
    private Integer []audiolist9={R.raw.donde,R.raw.casa,R.raw.escuela,R.raw.hospital,R.raw.estar,R.raw.ir};
//cuantos

    private String [] itemlist10={"Cuántos","Muchos","Pocos","Nada","No hay","Hay","Uno",
            "Dos","Tres","Cuatro","Cinco"};
    private   Integer[] itemphotolist10={R.drawable.cuantos_1,R.drawable.cuantos_muchos,R.drawable.cuantos_pocos,
            R.drawable.cuantos_nada,R.drawable.cuantos_nohay,R.drawable.cuantos_hay,R.drawable.cuantos_uno,R.drawable.cuantos_dos,
            R.drawable.cuantos_tres,R.drawable.cuantos_cuatro,R.drawable.cuantos_cinco};

    private Integer []audiolist10={R.raw.cuantos,R.raw.muchos,R.raw.pocos,R.raw.nada,R.raw.nohay,R.raw.hay,R.raw.uno,R.raw.dos,R.raw.tres,R.raw.cuatro,R.raw.cinco};

    //Sentencia SQL para crear la tabla de Usuarios
   private String sqlCreate = "CREATE TABLE categorias (nombre TEXT,photo TEXT,base TEXT)";
   private String sqlCreate2 = "CREATE TABLE item (categ TEXT,nombre TEXT,audio TEXT,foto TEXT,base TEXT,uso TEXT)";
   private String sqlCreate3 = "CREATE TABLE usuario (instalacion BOOLEAN,  pasos INTEGER)";

    public categoriaSQL(Context contexto) {
        super(contexto, database, null, basededatos_version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate2);
        db.execSQL(sqlCreate3);
        installcateg(db);
      db.execSQL("INSERT INTO usuario (instalacion,pasos) " +
                "VALUES ('True' , '0' )");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo_categoria vacía con el nuevo_categoria formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS item");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        //Se crea la nueva versión de la tabla
        //db.execSQL(sqlCreate3);
       // db.execSQL(sqlCreate);
       // db.execSQL(sqlCreate2);

        //db.execSQL("INSERT INTO usuario (instalacion,pasos) " +
           //     "VALUES ('False' , '0' )");
        onCreate(db);
    }
    String MEDIA_DIRECTORY = "/Fassycom/";
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void DeleteItem(SQLiteDatabase dba,String nombre,String categoria,Context context)
    {     File borraritem=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator +categoria+File.separator+nombre+".jpg");
        File borraritemaudio=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator +categoria+File.separator+nombre+".ogg");
    if(borraritem.exists()&& borraritemaudio.exists())
      {
          borraritem.delete();
          borraritemaudio.delete();
      } Toast.makeText(context, "Borrado Pictograma "+nombre, Toast.LENGTH_LONG).show();
        dba.execSQL("DELETE FROM item where nombre='"+nombre+"'");
        dba.close();
    }
    public void Deletecategoria(SQLiteDatabase dba,String nombre,Context context)
    { Log.i("entro","si");

        File borrarcategoria=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator +nombre);

        if (borrarcategoria.exists())
        {
            String[] children = borrarcategoria.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(borrarcategoria, children[i]).delete();
                borrarcategoria.delete();
            }

        }
                Toast.makeText(context, "Borrado Categoría "+nombre, Toast.LENGTH_LONG).show();
        dba.execSQL("DELETE FROM categorias where nombre='"+nombre+"'");
        dba.close(); Log.i("se borro","si");

    }
private void installcateg(SQLiteDatabase db)
{
    for (int i = 0; i <= 9; i++) {
        //Generamos los datos
        //Insertamos los datos en la tabla Usuarios
        db.execSQL("INSERT INTO categorias (nombre,photo,base) " +
                "VALUES ('" + categoriaslist[i] + "' , '" + categoriaphotolist[i] + "', 'local')");

        if(i==0) {
            for (int j = 0; j <= 13; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist1[j] + "' , " + itemphotolist1[j] + " ,'"+audiolist1[j]+"','local','0')");
            }
        }
        if(i==1) {
            for (int j = 0; j <= 12; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist2[j] + "' , " + itemphotolist2[j] + " ,'"+audiolist2[j]+"','local','0')");
            }
        }
        if(i==2) {
            for (int j = 0; j <= 4; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist3[j] + "' , " + itemphotolist3[j] + "  ,'"+audiolist3[j]+"','local','0')");
            }
        }
        if(i==3) {
            for (int j = 0; j <= 1; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist4[j] + "' , " + itemphotolist4[j] + "  ,'"+audiolist4[j]+"','local','0')");
            }
        }
        if(i==4 ) {
            for (int j = 0; j <= 11; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist5[j] + "' , " + itemphotolist5[j] + "  ,'"+audiolist5[j]+"','local','0')");
            }
        }
        if(i==5 ) {
            for (int j = 0; j <= 11; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist6[j] + "' , " + itemphotolist6[j] + " ,'"+audiolist6[j]+"' ,'local','0')");
            }
        }
        if(i==6) {
            for (int j = 0; j <= 3; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist7[j] + "' , " + itemphotolist7[j] + " ,'"+audiolist7[j]+"' ,'local','0')");
            }
        }
        if(i==7) {
            for (int j = 0; j <= 3; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist8[j] + "' , " + itemphotolist8[j] + "  ,'"+audiolist8[j]+"','local','0')");
            }
        }
        if(i==8) {
            for (int j = 0; j <= 5; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist9[j] + "' , " + itemphotolist9[j] + "  ,'"+audiolist9[j]+"','local','0')");
            }
        }
        if(i==9) {
            for (int j = 0; j <= 10; j++) {
                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                        "VALUES ('"+categoriaslist[i]+"','" + itemlist10[j] + "' , " + itemphotolist10[j] + "  ,'"+audiolist10[j]+"','local','0')");
            }
        }

    }
}

}
