package desixweb.appcilsa;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {


    public ArrayList<Bitmap> lettersIcon = new ArrayList<>();
    public ArrayList<String> lettersList = new ArrayList<>();
    public ArrayList<String> playitemaudio = new ArrayList<>();
    public ArrayList<Integer> playimagen = new ArrayList<>();
    public ArrayList<String> playnom = new ArrayList<>();
    public ArrayList<String> localitemaudio = new ArrayList<>();
Boolean modoeditar=false;
    String selecionadacategoria = "";
    int idcateg = R.id.lineatouch;

    categoriaSQL mDbHelper = new categoriaSQL(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textitle = (TextView) findViewById(R.id.titleapp);
        String font_path = "font/Reef.otf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        textitle.setTypeface(TF);
       final Dialog closet= splashmodal();
        Thread timerTread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                   closet.dismiss();
                }
            }
        };
        timerTread.start();
        final ToggleButton btnToggles = (ToggleButton)findViewById(R.id.modoeditable);

        btnToggles.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (btnToggles.isChecked()) {
                    //Button is ON
                    // Do Something
                    modoeditar=true;
                } else {
                    //Button is OFF
                    // Do Something
                    modoeditar=false;
                }
            }});

//Abrimos la base de datos 'DBUsuarios' en modo escritura
        LinearLayout showlineatouch = (LinearLayout) findViewById(R.id.lineatouch);
        showlineatouch.setBackgroundColor(Color.GREEN);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if (db == null) {
            Log.i("basedatos : ", "vacia");
        }
        if (db != null) {
            Log.i("basedatos : ", "creada");
            Cursor us = db.rawQuery(" SELECT * FROM usuario  ", null);
            String instalacion = "";
            if (us.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros

                do {
                    String yoquese = us.getString(1);
                    instalacion = us.getString(0);
                    if (yoquese.equals("1")) {
                        LinearLayout paso = (LinearLayout) findViewById(R.id.paso1);
                        paso.setVisibility(View.GONE);
                    }

                } while (us.moveToNext());


            }
            us.close();
            int installcateg = 0;
            if (instalacion.equals("True")) {
                showlistacategorias();
                //Si hemos abierto correctamente la base de datos

                //Insertamos 9 categoria de ejemplo


            }


        }
        ImageButton newcateg=(ImageButton)findViewById(R.id.imagenuevo);
        newcateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearcategorias(v,false,null);
            }
        });
    }
public void showlistacategorias()
    { SQLiteDatabase db = mDbHelper.getWritableDatabase();

        LinearLayout listacategorias = (LinearLayout) findViewById(R.id.listacategorias);
listacategorias.removeAllViewsInLayout();
        int idlayout = 500;
        Cursor c = db.rawQuery(" SELECT nombre,photo,base FROM categorias  ", null);
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros

            do {
                idlayout++;
                final String nombre = c.getString(0);
                String base = c.getString(2);
                final int encontradocateg = c.getCount();

                //crear layaut
                final LinearLayout btnLO = new LinearLayout(this);
                LinearLayout.LayoutParams paramsLO = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                btnLO.setOrientation(LinearLayout.VERTICAL);
                btnLO.setFocusableInTouchMode(true);
                paramsLO.setMargins(6, 6, 6, 6);
                btnLO.setLayoutParams(paramsLO);
                listacategorias.addView(btnLO);

                final LinearLayout linea = new LinearLayout(this);
                //crear imagen buton
                ImageButton categorinew = new ImageButton(this);
                LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                if (base.equals("local")) {
                    Integer fotocateg = c.getInt(1);
                    Log.i("nombre categoria : " + nombre, " foto : " + fotocateg + "base=" + base);
                    categorinew.setImageResource(fotocateg);
                } else {

                }

                if (base.equals("sdcard")) {
                    String fotocategsdcard = c.getString(1);
                    File categ = new File(fotocategsdcard);
                    Bitmap bmp = null;
                    if (categ.exists()) {
                        bmp = BitmapFactory.decodeFile(fotocategsdcard);

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        Bitmap resizedBitmap = null;
                        switch (metrics.densityDpi) {
                            case DisplayMetrics.DENSITY_XHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmp, 96, 96, false);
                                break;
                            case DisplayMetrics.DENSITY_XXHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmp, 144, 144, false);
                                break;
                            case DisplayMetrics.DENSITY_XXXHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmp, 192, 192, false);
                                break;
                            case DisplayMetrics.DENSITY_HIGH: //HDPI

                                resizedBitmap = Bitmap.createScaledBitmap(bmp, 72, 72, false);
                                break;
                            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmp, 48, 48, false);
                                break;

                        }
                        categorinew.setImageBitmap(resizedBitmap);
                    } else {
                        categorinew.setImageResource(R.drawable.caution);

                    }


                    if (bmp != null) {
                        bmp.recycle();

                        bmp = null;
                    }
                }
                categorinew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GridView gridViews = (GridView) findViewById(R.id.gridview);
                        int oldid = idcateg;

                        LinearLayout delete = (LinearLayout) findViewById(oldid);
                        delete.setVisibility(View.INVISIBLE);

                        idcateg = linea.getId();
                        selecionadacategoria = nombre;

                        linea.setVisibility(View.VISIBLE);
                        gridViews.invalidateViews();
                        lettersIcon.clear();
                        lettersList.clear();
                        mostrarobjet(selecionadacategoria, true);

                    }
                });
                categorinew.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        editarcateg(view,nombre,btnLO);
                        return true;
                    }
                });
                categorinew.setLayoutParams(lp);
                categorinew.setTag(encontradocateg);
                btnLO.addView(categorinew);

                //crear texto
                TextView newtext = new TextView(this);
                newtext.setText(nombre);
                newtext.setTextColor(Color.BLACK);
                newtext.setGravity(Gravity.CENTER);
                btnLO.addView(newtext);
                // intento de linea
                final float scale = getResources().getDisplayMetrics().density;
                int dpHeightInPx = (int) (2 * scale);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpHeightInPx);
                linea.setOrientation(LinearLayout.VERTICAL);
                linea.setLayoutParams(params);
                linea.setBackgroundColor(Color.GREEN);
                linea.setVisibility(View.INVISIBLE);
                linea.setId(idlayout + 1);
                btnLO.addView(linea);

            } while (c.moveToNext());
        }
        View v1 = getWindow().getDecorView().getRootView();
        touchlist(v1);


        c.close();
        db.close();

    }
    ImageView setimg;
    private static String MEDIA_DIRECTORYTEMP = "/Fassycom/temp";
    private static String MEDIA_DIRECTORY = "/Fassycom/";
    private EditText texto1;

    private String mpath;
    private final int Photo_Code = 200;
    private final int SELECT_PICTURE = 100;
    private final int REQUEST_IMAGE_CROP=1;
    private Dialog elegirfoto;
    long totalTiempo;
    long tiempoInicio;
    private static String mFileName = null;
    private MediaRecorder mRecorder = null;
    private boolean tenerphoto = false;
    private boolean teneraudio = false;

public void editarcateg(final View view, final String nombre, final LinearLayout btnLO) {
    if (modoeditar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.setpictograma, (ViewGroup) findViewById(R.id.editon));
        builder.setView(Layout);

        ImageButton editarcateg = (ImageButton) Layout.findViewById(R.id.editarcateg);
        editarcateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearcategorias(view,modoeditar,nombre);
            }
        });
        ImageButton delete = (ImageButton) Layout.findViewById(R.id.tacho);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("¿Quiere borrar Categoria: " + " " + nombre + "?");
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        mDbHelper.Deletecategoria(db, nombre, getApplicationContext());
                        btnLO.setVisibility(View.GONE);
                    }
                });
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //finish();
                    }
                });

                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        });
        editar = builder.create();
        editar.show();
    }
}
    Dialog dialog;

    public void crearcategorias(View view, final Boolean editar, final String nombrecateg) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.nuevo_categoria, (ViewGroup) findViewById(R.id.agregarcategnew));
        builder.setView(Layout);
        dialog = elegirfoto = builder.create();
        elegirfoto.show();
        setimg = (ImageView) Layout.findViewById(R.id.setimagen2);
        Bitmap bmps = null;
        if(modoeditar) {
            final SQLiteDatabase db = mDbHelper.getWritableDatabase();
            final String[] args = new String[]{nombrecateg};

            Cursor c = db.rawQuery(" SELECT nombre,photo,base FROM categorias  WHERE nombre=?", args);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros

                do {
                    final String nombre = c.getString(0);
                    String foto6 = c.getString(1);
                    int foto5 = c.getInt(1);
                    String base = c.getString(2);
                    EditText placenom = (EditText) Layout.findViewById(R.id.textobjet);
                    placenom.setText(nombre);
                    if (base.equals("local")) {
                        bmps = BitmapFactory.decodeResource(getResources(), foto5);
                        setimg.setImageBitmap(bmps);
                    }
                    if (base.equals("sdcard")) {

                        File fotos = new File(foto6);
                        if (fotos.exists()) {
                            bmps = BitmapFactory.decodeFile(foto6);

                            setimg.setImageBitmap(bmps);


                        } else {
                            bmps = BitmapFactory.decodeResource(getResources(), R.drawable.caution);
                            setimg.setImageBitmap(bmps);
                        }

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        Bitmap resizedBitmap = null;
                        switch (metrics.densityDpi) {

                            case DisplayMetrics.DENSITY_XHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 96, 96, false);
                                break;
                            case DisplayMetrics.DENSITY_XXHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 144, 144, false);
                                break;
                            case DisplayMetrics.DENSITY_XXXHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 192, 192, false);
                                break;
                            case DisplayMetrics.DENSITY_HIGH: //HDPI

                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 72, 72, false);
                                break;
                            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 48, 48, false);
                                break;

                        }

                    }


                }
                while (c.moveToNext());

            }
        }
        Button button_cancelar_categoria = ((Button) Layout.findViewById(R.id.cancelarcrearcategoria));
        Button button_crear_categoria = ((Button) Layout.findViewById(R.id.butoncrearitems));
        TextView texto88 = (TextView) Layout.findViewById(R.id.textfont88);
        String font_path = "font/Reef.otf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        texto88.setTypeface(TF);
        button_cancelar_categoria.setTypeface(TF);
        button_crear_categoria.setTypeface(TF);
        // Add onClickListener to exit_button
        button_cancelar_categoria.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        dialog.dismiss();
                    }
                }

        );
        final Bitmap finalBmps = bmps;
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            if(finalBmps!=null){finalBmps.recycle();}
                // do something
                //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        });

        // Add onClickListener to exit_button
        button_crear_categoria.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        texto1 = (EditText) Layout.findViewById(R.id.textobjet);
                        String objeto = texto1.getText().toString();
                        String nombre = objeto.replace("'", "!");
                        if (objeto.isEmpty()) {
                            Toast.makeText(getBaseContext(), "Falta Nombre ", Toast.LENGTH_SHORT).show();
                        } else {

                            if (tenerphoto ) {

                                //si foto y audio y texto estan llenos se procede a crear carpeta en caso de que no existe
                                File folder = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY + File.separator + nombre);
                                boolean success = true;
                                if (!folder.exists()) {

                                    success = folder.mkdirs();
                                }
                                if (success) {


                                    File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORYTEMP + File.separator + "/temp.jpg");
                                    File tofile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + nombre + "/" + nombre + ".jpg");

                                    if (file2.renameTo(tofile2)) {
                                        String fcateg = Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + nombre + "/" + nombre + ".jpg";
                                        File nomediaFile = new File(folder, ".nomedia");

                                        try {
                                            nomediaFile.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if(modoeditar)
                                        { SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                            db.execSQL("UPDATE categorias  SET nombre='"+nombre+"' ,photo='"+fcateg+"',base='sdcard' WHERE nombre='"+nombrecateg+"' ");
                                            db.execSQL("UPDATE item  SET categ='"+nombre+"' WHERE categ='"+nombrecateg+"' ");
                                            Toast.makeText(getBaseContext(), "Editada  : " + nombre, Toast.LENGTH_SHORT).show();
                                            tenerphoto = false;
                                            dialog.cancel();
                                            editarclear.dismiss();
                                            showlistacategorias();
                                        }else
                                        {
                                            addcateg(nombre, fcateg);Toast.makeText(getBaseContext(), "Creada : " + nombre, Toast.LENGTH_SHORT).show();
                                            tenerphoto = false;
                                            dialog.cancel();
                                        }



                                    }


                                }

                            } else {
                                Toast.makeText(getBaseContext(), "Falta foto  ", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }

        );


    }

    public void mostrarobjet(String categoria, Boolean update) {
        final GridView gridViews = (GridView) findViewById(R.id.gridview);

        if (update) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            Cursor cl = db.rawQuery(" SELECT nombre,foto,base FROM item Where categ='" + categoria + "'", null);
            if (cl.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String nombre = cl.getString(0);
                    int foto = cl.getInt(1);
                    String foto1 = cl.getString(1);
                    String base = cl.getString(2);
                    lettersList.add(nombre);
                    if (base.equals("local")) {
                        Bitmap tempBMP = BitmapFactory.decodeResource(getResources(), foto);
                        lettersIcon.add(tempBMP);
                    }
                    if (base.equals("sdcard")) {
                        Bitmap bmps = null;
                        File fotos = new File(foto1);
                        if (fotos.exists()) {
                            bmps = BitmapFactory.decodeFile(foto1);


                        } else {
                            bmps = BitmapFactory.decodeResource(getResources(), R.drawable.caution);
                        }

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        Bitmap resizedBitmap = null;
                        switch (metrics.densityDpi) {

                            case DisplayMetrics.DENSITY_XHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 96, 96, false);
                                break;
                            case DisplayMetrics.DENSITY_XXHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 144, 144, false);
                                break;
                            case DisplayMetrics.DENSITY_XXXHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 192, 192, false);
                                break;
                            case DisplayMetrics.DENSITY_HIGH: //HDPI

                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 72, 72, false);
                                break;
                            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 48, 48, false);
                                break;

                        }
                        if (fotos.exists()) {
                            if (bmps != null) {
                                bmps.recycle();
                                bmps = null;
                            }
                        }


                        lettersIcon.add(resizedBitmap);


                    }


                } while (cl.moveToNext());
            }
            cl.close();
            db.close();
            Bitmap tempxxv = BitmapFactory.decodeResource(getResources(), R.drawable.icon_add);
            lettersList.add("Nuevo");
            lettersIcon.add(tempxxv);

        }
        gridViews.invalidateViews();

        GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), lettersIcon, lettersList);
        gridAdapter.notifyDataSetChanged();
        gridViews.setAdapter(gridAdapter);


        gridViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String nombreitem = (String) adapterView.getItemAtPosition(position);

                String source = (Long.valueOf(id)).toString();
                int cont = lettersList.size() - 1;
                String count = String.valueOf(cont);


                if (source.equals(count)) {
                    mostrar(view,modoeditar,nombreitem);
                } else {
                    int numEntero = Integer.parseInt(source);
                    Bitmap image = lettersIcon.get(numEntero);
                    showimageplay(nombreitem, image, numEntero);
                }



            }
        });
        gridViews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String nombreitem = (String) adapterView.getItemAtPosition(position);
                String source = (Long.valueOf(id)).toString();
                int numEntero = Integer.parseInt(source);
                int cont = lettersList.size() - 1;
                String count = String.valueOf(cont);
                if (source.equals(count)) {
                } else {
                    createDialog(view, nombreitem, numEntero, true);
                }

                return true;
            }
        });

    }
    Dialog editarclear;
    Dialog editar = null;
    public void createDialog(final View view, final String borrar, final int source, final Boolean item) {
        if (modoeditar) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View Layout = inflater.inflate(R.layout.setpictograma, (ViewGroup) findViewById(R.id.editon));
            builder.setView(Layout);

            ImageButton editarcateg = (ImageButton) Layout.findViewById(R.id.editarcateg);
            editarcateg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                mostrar(view,modoeditar,borrar);
                }
            });
            ImageButton delete = (ImageButton) Layout.findViewById(R.id.tacho);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                    //adb.setView(Main.this);
                    adb.setTitle("¿Quiere borrar Pictograma: " + " " + borrar + "?");
                    adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (item) {

                                lettersList.remove(source);
                                lettersIcon.remove(source);
                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                mDbHelper.DeleteItem(db, borrar, selecionadacategoria, getApplicationContext());

                                mostrarobjet(selecionadacategoria, false);
                                editar.dismiss();
                            }


                        }
                    });

                    adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            //finish();
                        }
                    });

                    AlertDialog alertDialog = adb.create();
                    alertDialog.show();

                }
            });
            editarclear=editar = builder.create();
            editar.show();

        }

    }

    public void showimageplay(String nombre, Bitmap image, int source) {
        Button sendfassy = (Button) findViewById(R.id.sendfass);
        sendfassy.setVisibility(View.VISIBLE);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int sumauso = 0;
        String base = "";
        String audio = "";
        String[] args = new String[]{nombre};
        Boolean Erroraudio = false;
        Cursor clm = db.rawQuery(" SELECT uso,audio,base FROM item Where nombre=? limit 1", args);

        if (clm.moveToFirst()) {
            do {

                String uso = clm.getString(0);
                base = clm.getString(2);

                audio = clm.getString(1);
                if (audio != null) {
                    if (!audio.isEmpty()) {
                        Log.i("mostrar  : ", "" + audio + " ,local : " + base);
                        if (base.equals("sdcard")) {
                            File audiorep = new File(audio);
                            if (audiorep.exists()) {
                                playitemaudio.add(audio);
                                localitemaudio.add(base);
                                playnom.add(nombre);
                            } else {
                                Toast.makeText(getBaseContext(), "Grabanción no encontrada Error", Toast.LENGTH_SHORT).show();
                                Erroraudio = true;
                            }
                        } else {
                            playitemaudio.add(audio);
                            playnom.add(nombre);
                            localitemaudio.add(base);
                        }

                    }
                }

                sumauso = Integer.parseInt(uso);
            }
            while (clm.moveToNext());
        }


        if (!Erroraudio) {
            LinearLayout listaitem = (LinearLayout) findViewById(R.id.reproducion);
            LinearLayout linear = new LinearLayout(this);

            linear.setOrientation(LinearLayout.VERTICAL);
            listaitem.addView(linear);

            //crear imagen buton
            ImageButton itemplay = new ImageButton(this);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            itemplay.setImageBitmap(image);
            Random aleatorio = new Random(System.currentTimeMillis());
// Producir nuevo int aleatorio entre 0 y 99
            int intAletorio = aleatorio.nextInt(100);
            int idadd = 0;

            for (int a = 0; a < playimagen.size(); a++) {
                int num1 = 50;
                int num2 = 120;

                for (int i = 0; i < 10; i++) {
                    int num = (int) Math.floor(Math.random() * (num2 - num1 + 1) + (num1));
                    if (playimagen.get(a) == source + 2000 + num) {
                        idadd = source + 3100 + num;
                    } else {
                        idadd = source + 2000 + num;

                    }
                }


            }
            final int idlayout = source + idadd;
            linear.setId(idlayout);


            final String finalAudio = audio;
            final String finalBase = base;

            itemplay.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int camt = playimagen.size();

                    playimagen.remove(Integer.valueOf(idlayout));
                    playitemaudio.remove(finalAudio);
                    localitemaudio.remove(finalBase);
                    try {
                        LinearLayout remove = (LinearLayout) findViewById(idlayout);
                        remove.setVisibility(View.GONE);
                    }catch (Exception noborrado)
                    {
                        noborrado.printStackTrace();
                    }

                    if(camt==1)
                    {
                        Button sendfass=(Button) findViewById(R.id.sendfass);
                        sendfass.setVisibility(View.GONE);
                    }

                    return true;
                }

            });


            playimagen.add(idlayout);
            itemplay.setLayoutParams(lp);
            itemplay.setTag(nombre);
            itemplay.setId(source);
            linear.addView(itemplay);
            //crear texto
            TextView newtext = new TextView(this);
            newtext.setText(nombre);
            newtext.setTextColor(Color.BLACK);
            newtext.setGravity(Gravity.CENTER);
            linear.addView(newtext);


            sumauso++;
            db.execSQL("UPDATE  item SET uso ='" + sumauso + "' where nombre='" + nombre + "'");
            db.close();
            clm.close();
        }

    }


    Dialog dialog2;

    public void elegirfotodialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View Layoutfoto = inflater.inflate(R.layout.elegirfoto, (ViewGroup) findViewById(R.id.contenedorelegirfoto));
        builder.setView(Layoutfoto);
        TextView textoset = (TextView) Layoutfoto.findViewById(R.id.textoset);
        Button cancelarelegir = (Button) Layoutfoto.findViewById(R.id.cancelelegir);

        String font_path = "font/Reef.otf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        textoset.setTypeface(TF);
        cancelarelegir.setTypeface(TF);
        dialog2 = elegirfoto = builder.create();
        elegirfoto.show();

        cancelarelegir.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        dialog2.cancel();
                    }
                }

        );

    }


    public void opencamara(View view) {
        File folder = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORYTEMP);
        boolean success = true;
        if (!folder.exists()) {
            File nomediaFile = new File(folder, ".nomedia");
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            success = folder.mkdirs();
        }
        if (success) {
            String imagename = "temp.jpg";
            mpath = Environment.getExternalStorageDirectory() + folder.separator + MEDIA_DIRECTORYTEMP + folder.separator + imagename;
            File newfile = new File(mpath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photouri = Uri.fromFile(newfile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
            startActivityForResult(intent, Photo_Code);

        }
    }

    public void selecionarimagenf(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Seleciona una imagen"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case Photo_Code:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mpath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String mpath, Uri uri) {
                                    CropImage(uri);
                                    tenerphoto = true;
                                }
                            }
                    );


                    //try {
                     //   Bitmap bmp = rotateImage(mpath);
                       // setimg.setImageBitmap(bmp);


                    //} catch (IOException e) {
                     //   Log.w("TAG", "-- Error in setting image");
                 //   } catch (OutOfMemoryError oom) {
                   //     Log.w("TAG", "-- OOM Error in setting image");
            //        }

                    // setimg.setImageBitmap(rotatedBitmap);


                    dialog2.dismiss();

                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    CropImage(path);
                    tenerphoto = true;
                    elegirfoto.dismiss();
                    break;
                case REQUEST_IMAGE_CROP:
                    OutputStream outStream = null;
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap)extras.get("data");
                    setimg.setImageBitmap(imageBitmap);
                    String mpaths = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORYTEMP + File.separator + "temp.jpg";
                    File sources = new File(mpaths);

                    try {
                        outStream = new FileOutputStream(sources);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    try {
                        outStream.flush();
                        outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i("recortador ","true");
                    break;
            }

        }

    }


    public void CropImage( Uri uri) {
        Intent CropIntent;
        try {
        CropIntent = new Intent("com.android.camera.action.CROP");
        CropIntent.setDataAndType(uri,"image/*");

        CropIntent.putExtra("crop","true");
        CropIntent.putExtra("outputX",192);
        CropIntent.putExtra("outputY",192);
        CropIntent.putExtra("aspectX",1);
        CropIntent.putExtra("aspectY",1);
        CropIntent.putExtra("scaleUpIfNeeded",true);
        CropIntent.putExtra("return-data",true);

        startActivityForResult(CropIntent,1);
        }
        // respond to users whose devices do not support the crop action
        catch ( Exception croppe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();

            ExifInterface exif = null;
            int angle = 0;File file2= new File (uri.toString());
            File tofile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORYTEMP + File.separator + "/temp.jpg");
            try {
                exif = new ExifInterface(file2.getAbsolutePath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);


                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fOut;Bitmap fileitem = BitmapFactory.decodeFile(uri.toString());
            try {





                assert exif != null;
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));

                try {
                    exif.saveAttributes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fOut = new FileOutputStream(tofile2);
                int width = fileitem.getWidth();
                int height = fileitem.getHeight();
                int newWidth = 192;
                int newHeight = 192;
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;

                Matrix matrix = new Matrix();
                matrix.postRotate(angle);
                matrix.postScale(scaleWidth, scaleHeight);
                // Bitmap out = Bitmap.createScaledBitmap(fileitem, 192, 192, false);
                Bitmap out = Bitmap.createBitmap(fileitem, 0, 0, width, height, matrix, true);
                out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

                fOut.flush();
                fOut.close();
                fileitem.recycle();
                out.recycle();
            } catch (Exception ignored) {

            }


        }


    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public Bitmap rotateImage(String mpath) throws IOException {
        File f = new File(mpath);
        Bitmap bmp = null;
        if (f.exists()) {
            ExifInterface exif = new ExifInterface(f.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            bmp = BitmapFactory.decodeStream(new FileInputStream(f),
                    null, options);
            try {
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                        bmp.getHeight(), mat, true);
                ByteArrayOutputStream outstudentstreamOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100,
                        outstudentstreamOutputStream);

            } catch (Exception excepcion) {
                Context context = getApplicationContext();
                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.fsc_image_drop);
                Toast.makeText(getBaseContext(), "Error en la imagen", Toast.LENGTH_SHORT).show();
                excepcion.printStackTrace();
            }

        }
        return bmp;

    }

    public void additem(String nombre, String foto, String audio) {
        if (!nombre.isEmpty() || !foto.isEmpty() || !audio.isEmpty()) {


            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                    "VALUES ('" + selecionadacategoria + "','" + nombre + "' , '" + foto + "' ,'" + audio + "','sdcard','0')");
            Log.i("ingresado item:  ", "categoria: " + selecionadacategoria + "nombre: " + nombre + " y foto: " + foto + ",audio" + audio);
            db.close();


            Bitmap bmps = BitmapFactory.decodeFile(foto);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Bitmap resizedBitmap = null;
            switch (metrics.densityDpi) {
                case DisplayMetrics.DENSITY_XHIGH: //MDPI
                    resizedBitmap = Bitmap.createScaledBitmap(bmps, 96, 96, false);
                    break;
                case DisplayMetrics.DENSITY_XXHIGH: //MDPI
                    resizedBitmap = Bitmap.createScaledBitmap(bmps, 144, 144, false);
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH: //MDPI
                    resizedBitmap = Bitmap.createScaledBitmap(bmps, 192, 192, false);
                    break;
                case DisplayMetrics.DENSITY_HIGH: //HDPI

                    resizedBitmap = Bitmap.createScaledBitmap(bmps, 72, 72, false);
                    break;
                case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                    resizedBitmap = Bitmap.createScaledBitmap(bmps, 48, 48, false);
                    break;

            }
            int cont = lettersList.size() - 1;
            Log.i("datos vacios:  ", "" + cont + "");
            lettersList.remove(cont);
            lettersIcon.remove(cont);

            cont = lettersList.size() - 1;
            Log.i("datos vacios:  ", "" + cont + "");
            lettersIcon.add(resizedBitmap);
            lettersList.add(nombre);
            Bitmap tempxxv = BitmapFactory.decodeResource(getResources(), R.drawable.icon_add);
            lettersList.add("Nuevo");
            lettersIcon.add(tempxxv);
            if (bmps != null) {
                bmps.recycle();
                bmps = null;
            }

            mostrarobjet(selecionadacategoria, false);
        }
    }

    Boolean cancelrecord = false;
    public void addcateg(final String nom, String ruta) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO categorias (nombre,photo,base) " +
                "VALUES ('" + nom + "' , '" + ruta + "' , 'sdcard')");
        int cont = 0;
        Cursor ce = db.rawQuery(" SELECT nombre,photo,base FROM categorias  ", null);
        if (ce.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros

            do {
                cont = ce.getCount();

            } while (ce.moveToNext());
        }
        cont++;
        ce.close();
        db.close();
        LinearLayout listacategorias = (LinearLayout) findViewById(R.id.listacategorias);
        final LinearLayout btnLO = new LinearLayout(this);
        LinearLayout.LayoutParams paramsLO = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnLO.setOrientation(LinearLayout.VERTICAL);
        paramsLO.setMargins(6, 6, 6, 6);
        btnLO.setLayoutParams(paramsLO);
        listacategorias.addView(btnLO);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageButton categorinew = new ImageButton(this);
        Bitmap bmp = BitmapFactory.decodeFile(ruta);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Bitmap resizedBitmap = null;
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_HIGH: //HDPI

                resizedBitmap = Bitmap.createScaledBitmap(bmp, 72, 72, false);
                break;
            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                resizedBitmap = Bitmap.createScaledBitmap(bmp, 48, 48, false);
                break;
            case DisplayMetrics.DENSITY_XHIGH: //MDPI
                resizedBitmap = Bitmap.createScaledBitmap(bmp, 96, 96, false);
                break;
            case DisplayMetrics.DENSITY_XXHIGH: //MDPI
                resizedBitmap = Bitmap.createScaledBitmap(bmp, 144, 144, false);
                break;
            case DisplayMetrics.DENSITY_XXXHIGH: //MDPI
                resizedBitmap = Bitmap.createScaledBitmap(bmp, 192, 192, false);
                break;

        }
        final LinearLayout linear = new LinearLayout(this);
        categorinew.setImageBitmap(resizedBitmap);

        if (bmp != null) {
            bmp.recycle();
            bmp = null;
        }

        int idlayout = cont + 99;
        categorinew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridView gridViews = (GridView) findViewById(R.id.gridview);
                int oldid = idcateg;

                LinearLayout delete = (LinearLayout) findViewById(oldid);
                delete.setVisibility(View.INVISIBLE);

                idcateg = linear.getId();
                selecionadacategoria = nom;

                linear.setVisibility(View.VISIBLE);
                gridViews.invalidateViews();
                lettersIcon.clear();
                lettersList.clear();
                mostrarobjet(selecionadacategoria, true);

            }
        });
        categorinew.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editarcateg(view,nom,btnLO);
                return true;
            }
        });
        categorinew.setLayoutParams(lp);
        categorinew.setTag(44);
        btnLO.addView(categorinew);
        TextView newtext = new TextView(this);
        newtext.setText(nom);
        newtext.setTextColor(Color.BLACK);
        newtext.setGravity(Gravity.CENTER);
        btnLO.addView(newtext);
        //


        final float scale = getResources().getDisplayMetrics().density;
        int dpHeightInPx = (int) (2 * scale);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpHeightInPx);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.setLayoutParams(params);
        linear.setBackgroundColor(Color.GREEN);
        linear.setVisibility(View.INVISIBLE);
        linear.setId(idlayout);
        btnLO.addView(linear);

    }
    public void mostrar(View v, final Boolean modoeditar, String nombrex) {
//sedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.agregar, (ViewGroup) findViewById(R.id.contenedoragregar));
        builder.setView(Layout);
        Button cancelar_item = ((Button) Layout.findViewById(R.id.cancelitem));
        Button button_crear_categoria = ((Button) Layout.findViewById(R.id.butoncrearitems));
        // Add on
        TextView texto5 = (TextView) Layout.findViewById(R.id.textfont1);
        final TextView textinfo = (TextView) Layout.findViewById(R.id.infotext);
        String font_path = "font/Reef.otf";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        texto5.setTypeface(TF);
        cancelar_item.setTypeface(TF);
        button_crear_categoria.setTypeface(TF);

        textinfo.setTypeface(TF);
        cancelar_item.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        dialog.dismiss();

                    }
                }

        );


        setimg = (ImageView) Layout.findViewById(R.id.setimage);

        if (modoeditar) {
            final SQLiteDatabase db = mDbHelper.getWritableDatabase();
            final String[] args = new String[]{nombrex};
            Bitmap bmps = null;
            Cursor c = db.rawQuery(" SELECT foto,base FROM item  WHERE nombre=?", args);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros

                do {

                    String foto6 = c.getString(0);
                    int foto5 = c.getInt(0);
                    String base = c.getString(1);
                    EditText placenom = (EditText) Layout.findViewById(R.id.textobjet);
                    placenom.setText(nombrex);
                    if (base.equals("local")) {
                        bmps = BitmapFactory.decodeResource(getResources(), foto5);
                        setimg.setImageBitmap(bmps);
                    }
                    if (base.equals("sdcard")) {

                        File fotos = new File(foto6);
                        if (fotos.exists()) {
                            bmps = BitmapFactory.decodeFile(foto6);

                            setimg.setImageBitmap(bmps);


                        } else {
                            bmps = BitmapFactory.decodeResource(getResources(), R.drawable.caution);
                            setimg.setImageBitmap(bmps);
                        }

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        Bitmap resizedBitmap = null;
                        switch (metrics.densityDpi) {

                            case DisplayMetrics.DENSITY_XHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 96, 96, false);
                                break;
                            case DisplayMetrics.DENSITY_XXHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 144, 144, false);
                                break;
                            case DisplayMetrics.DENSITY_XXXHIGH: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 192, 192, false);
                                break;
                            case DisplayMetrics.DENSITY_HIGH: //HDPI

                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 72, 72, false);
                                break;
                            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 48, 48, false);
                                break;

                        }

                    }


                }
                while (c.moveToNext());

            }
        }
            // Add onClickListener to exit_button
            final String finalNombrex = nombrex;
            button_crear_categoria.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            texto1 = (EditText) Layout.findViewById(R.id.textobjet);
                            String objeto = texto1.getText().toString();
                            if (objeto.isEmpty()) {
                                Toast.makeText(getBaseContext(), "Falta Nombre", Toast.LENGTH_SHORT).show();
                            } else {

                                if (tenerphoto && teneraudio) {
                                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                    int compare = 0;
                                    String nombre = objeto.replace("'", "!");
                                    if (!modoeditar) {
                                        String[] args = new String[]{nombre};
                                        Cursor cx = db.rawQuery(" SELECT nombre,categ FROM item  WHERE nombre=?", args);
                                        if (cx.moveToFirst()) {
                                            //Recorremos el cursor hasta que no haya más registros

                                            do {
                                                compare = cx.getCount();

                                            } while (cx.moveToNext());

                                        }
                                        cx.close();
                                        db.close();
                                    }
                                    if (compare == 1) {
                                        Toast.makeText(getBaseContext(), "Ya existe el nombre", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //si foto y audio y texto estan llenos se procede a crear carpeta en caso de que no existe
                                        File folder = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY + File.separator + selecionadacategoria);
                                        boolean success = true;
                                        if (!folder.exists()) {

                                            success = folder.mkdirs();
                                            File nomediaFile = new File(folder, ".nomedia");
                                            try {
                                                nomediaFile.createNewFile();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (success) {

                                            int angle = 0;
                                            File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORYTEMP + File.separator + "/temp.ogg");
                                            File tofile1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + selecionadacategoria + "/" + nombre + ".ogg");
                                            File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORYTEMP + File.separator + "/temp.jpg");
                                            File tofile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + selecionadacategoria + "/" + nombre + ".jpg");

                                            if (file1.renameTo(tofile1) && file2.renameTo(tofile2)) {

                                                String fileaudio = Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + selecionadacategoria + "/" + nombre + ".ogg";
                                                String fitem = Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + selecionadacategoria + "/" + nombre + ".jpg";
                                                Bitmap fileitem = BitmapFactory.decodeFile(fitem);

                                                if (!modoeditar) {
                                                    additem(nombre, fitem, fileaudio);

                                                    Toast.makeText(getBaseContext(), "Creado Pictograma: " + nombre, Toast.LENGTH_SHORT).show();
                                                    teneraudio = false;
                                                    tenerphoto = false;
                                                    dialog.cancel();
                                                } else {
                                                    db = mDbHelper.getWritableDatabase();
                                                    db.execSQL("UPDATE item  SET nombre='" + nombre + "',foto='"+fitem+"',audio='"+fileaudio+"',base='sdcard' WHERE nombre='" + finalNombrex + "' ");
                                                    Toast.makeText(getBaseContext(), "editado Pictograma: " + nombre, Toast.LENGTH_SHORT).show();
                                                    teneraudio = false;
                                                    tenerphoto = false;
                                                    mostrarobjet(selecionadacategoria,false);
                                                    editarclear.dismiss();
                                                    dialog.cancel();
                                                }
                                            }


                                        }
                                    }

                                } else {
                                    if (!teneraudio || !tenerphoto) {
                                        Toast.makeText(getBaseContext(), "Falta foto o sonido", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });

            dialog = builder.create();
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    // do something
                    // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
            });

            //boton audio
            final Button boton = (Button) Layout.findViewById(R.id.buttonrecord);

            boton.setOnTouchListener(new View.OnTouchListener() {
                @Override

                public boolean onTouch(View v, MotionEvent event) {
                    boton.performClick();
                    int action = MotionEventCompat.getActionMasked(event);
                    switch (action) {
                        case (MotionEvent.ACTION_DOWN):
                            //Aqui guardas en una variable privada de clase las coordenadas del primer toque:
                            cancelrecord = false;
                            tiempoInicio = System.currentTimeMillis();
                            File folder = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORYTEMP);
                            boolean success = true;
                            if (!folder.exists()) {
                                File nomediaFile = new File(folder, ".nomedia");
                                try {
                                    nomediaFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                success = folder.mkdirs();
                            }
                            if (success) {
                                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                                vib.vibrate(100);
                                boton.setBackgroundResource(R.drawable.save_microphone);
                                MediaPlayer sond = MediaPlayer.create(MainActivity.this, R.raw.record);
                                sond.start();

                                mRecorder = new MediaRecorder();
                                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                                mRecorder.setAudioChannels(1);
                                mRecorder.setAudioEncodingBitRate(128000);
                                mRecorder.setAudioSamplingRate(44100);
                                // Record to the external cache directory for visibility
                                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();

                                //getExternalCacheDir().getAbsolutePath();

                                mFileName += File.separator + "/Fassycom/temp/" + File.separator + "temp.ogg";
                                mRecorder.setOutputFile(mFileName);


                                try {
                                    mRecorder.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                sond.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                    public void onCompletion(MediaPlayer arg0) {
                                        totalTiempo = System.currentTimeMillis() - tiempoInicio;
                                        if (totalTiempo > 500 && !cancelrecord) {

                                            if (mRecorder != null) {
                                                Toast.makeText(getBaseContext(), "Grabando", Toast.LENGTH_SHORT).show();
                                                mRecorder.start();
                                            }

                                        }

                                    }

                                });

                            }

                            break;

                        case (MotionEvent.ACTION_UP):
                            //boton liberado

                            totalTiempo = System.currentTimeMillis() - tiempoInicio;
                            if (totalTiempo < 500) {
                                cancelrecord = true;
                                MediaPlayer sond2 = MediaPlayer.create(MainActivity.this, R.raw.cancelrecord);
                                sond2.start();

                                boton.setBackgroundResource(R.drawable.fsc_microphone);
                                Toast.makeText(getBaseContext(), "Manten presionado para grabar,suelta para enviar", Toast.LENGTH_SHORT).show();
                                if (null != mRecorder) {
                                    try {
                                        mRecorder.stop();
                                        File file = new File(mFileName);
                                        file.delete();
                                    } catch (RuntimeException ex) {
                                        //Ignore
                                    } finally {
                                        mRecorder.release();
                                        mRecorder = null;
                                    }
                                }

                            }

                            if (totalTiempo > 500) {
                                Toast.makeText(getBaseContext(), "Grabado con éxito", Toast.LENGTH_SHORT).show();
                                MediaPlayer sond2 = MediaPlayer.create(MainActivity.this, R.raw.finalrecordsound);
                                teneraudio = true;
                                sond2.start();
                                if (null != mRecorder) {
                                    try {
                                        mRecorder.stop();
                                    } catch (RuntimeException ex) {
                                        //Ignore
                                    } finally {
                                        mRecorder.release();
                                        mRecorder = null;
                                    }
                                }

                                boton.setBackgroundResource(R.drawable.save_save);
                                final MediaPlayer mps = new MediaPlayer();
                                try {
                                    mps.setDataSource(mFileName);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mps.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (mps != null) {
                                    mps.start();

                                }
                            }

                            break;

                    }
                    return true;
                }
            });
        }

    ArrayList<Uri> uris = new ArrayList<Uri>();
    MediaPlayer mp;
    int currentposition = 0;
    Boolean questionplay = false;
    int quest = 0;

    public void borrar(View view) {
        if (mp != null) {
            if (!playitemaudio.isEmpty()) {
                try {
                    if (mp.isPlaying()) {

                        mp.stop();


                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

                Button pause = (Button) findViewById(R.id.pause);
                Button play = (Button) findViewById(R.id.play);
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            }
        }

        LinearLayout question = (LinearLayout) findViewById(R.id.question);
        LinearLayout listaitemborrar = (LinearLayout) findViewById(R.id.reproducion);
        listaitemborrar.removeAllViews();
        localitemaudio.clear();
        playitemaudio.clear();
        playimagen.clear();
        playnom.clear();
        uris.clear();
        questionplay = false;
        question.setVisibility(View.GONE);
        quest = 0;
        currentposition = 0;
        Button sendfass=(Button)findViewById(R.id.sendfass);
        sendfass.setVisibility(View.GONE);

    }


    Boolean pausa = false;


    public void question(View view) {
        quest++;
        LinearLayout question = (LinearLayout) findViewById(R.id.question);

        playimagen.add(R.id.question);
        if (quest == 1 && playitemaudio.size() == 0 && !questionplay) {
            question.setVisibility(View.VISIBLE);
            playitemaudio.add(String.valueOf(R.raw.unapregunta));
            localitemaudio.add("local");
            questionplay = true;

        }
        if (quest >= 2 && playitemaudio.size() > 1 && questionplay) {
            question.setVisibility(View.GONE);
            quest = 0;
            questionplay = false;
            playitemaudio.remove(0);
            localitemaudio.remove(0);

        }
    }

    public void play(View view) {

        reproducir();
    }

    @SuppressLint("ResourceAsColor")
    public void reproducir() {

        if (!playitemaudio.isEmpty()) {

            int position = playitemaudio.size();
            if (currentposition >= position) {
                currentposition = 0;
            } else {
                pausa = false;
                Button play = (Button) findViewById(R.id.play);
                Button pause = (Button) findViewById(R.id.pause);
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);

                Log.i("data  : ", "local : " + localitemaudio.get(currentposition));
                mp = new MediaPlayer();


                if (localitemaudio.get(currentposition).equals("local")) {
                    Integer ogg = Integer.parseInt(playitemaudio.get(currentposition));
                    mp = MediaPlayer.create(this, ogg);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // mp.setLooping(false);
                    if (mp != null) {
                        if (currentposition != 0) {
                            int clear = currentposition - 1;
                            int imagenclear = playimagen.get(clear);
                            LinearLayout set = (LinearLayout) findViewById(imagenclear);
                            set.setBackgroundColor(Color.TRANSPARENT);
                        }
                        int imagenid = playimagen.get(currentposition);
                        String idimage= String.valueOf(imagenid);
                        if(!idimage.isEmpty())
                        {
                            LinearLayout set = (LinearLayout) findViewById(imagenid);
                            set.setBackgroundColor(Color.parseColor("#9ded69"));
                        }


                        mp.start();
                    }
                }


                if (localitemaudio.get(currentposition).equals("sdcard")) {
                    File sound= new File(playitemaudio.get(currentposition));
                    if(sound.exists()) {
                    try {
                        mp.setDataSource(String.valueOf(playitemaudio.get(currentposition)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                        if (mp != null) {
                            mp.start();
                            if (currentposition != 0) {
                                int clear = currentposition - 1;
                                int imagenclear = playimagen.get(clear);
                                LinearLayout set = (LinearLayout) findViewById(imagenclear);
                                set.setBackgroundColor(Color.TRANSPARENT);
                            }
                            int imagenid = playimagen.get(currentposition);
                            LinearLayout set = (LinearLayout) findViewById(imagenid);
                            set.setBackgroundColor(Color.parseColor("#9ded69"));
                        }

                }
                // Setup listener so next song starts automatically
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer arg0) {
                        mp.release();
                        nextplay();
                    }

                });


            }
        }

    }


    public void nextplay() {
        int position = playitemaudio.size() - 1;
        if (currentposition >= position) {
            currentposition = 0;
            mp = null;
            if (!playimagen.isEmpty()) {
                int clear = playimagen.size() - 1;
                int imagenclear = playimagen.get(clear);
                LinearLayout set = (LinearLayout) findViewById(imagenclear);
                set.setBackgroundColor(Color.TRANSPARENT);
            }
            Button pause = (Button) findViewById(R.id.pause);
            Button play = (Button) findViewById(R.id.play);
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            HorizontalScrollView hScrollView = (HorizontalScrollView) findViewById(R.id.scrollplay);
            hScrollView.scrollTo(0, 0);
        } else {
            if (!pausa) {

                currentposition++;
                reproducir();
                HorizontalScrollView hScrollView = (HorizontalScrollView) findViewById(R.id.scrollplay);
                int dopx = currentposition * 30;
                hScrollView.smoothScrollBy(dopx, 0);
            }
        }

    }

    public void pause(View view) {
        pausa = true;
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();

                Button pause = (Button) findViewById(R.id.pause);
                Button play = (Button) findViewById(R.id.play);
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            }
        }

    }

    public void touchlist(View view) {
        int oldid = idcateg;

        LinearLayout delete = (LinearLayout) findViewById(oldid);
        delete.setVisibility(View.INVISIBLE);

        LinearLayout showlineatouch = (LinearLayout) findViewById(R.id.lineatouch);
        showlineatouch.setVisibility(View.VISIBLE);
        idcateg = R.id.lineatouch;
        lettersList.clear();
        lettersIcon.clear();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final GridView gridViews = (GridView) findViewById(R.id.gridview);
        Cursor cl = db.rawQuery(" SELECT nombre,foto,base FROM item where NOT uso==0  ORDER BY uso DESC LIMIT 12", null);
        if (cl.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre = cl.getString(0);
                int foto = cl.getInt(1);
                String foto1 = cl.getString(1);
                String base = cl.getString(2);
                lettersList.add(nombre);
                Bitmap bmps;
                if (base.equals("local")) {
                    Bitmap tempBMP = BitmapFactory.decodeResource(getResources(), foto);
                    lettersIcon.add(tempBMP);
                }
                if (base.equals("sdcard")) {
                    File fotos = new File(foto1);
                    if (fotos.exists()) {
                        bmps = BitmapFactory.decodeFile(foto1);


                    } else {
                        bmps = BitmapFactory.decodeResource(getResources(), R.drawable.caution);
                    }
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    Bitmap resizedBitmap = null;
                    switch (metrics.densityDpi) {
                        case DisplayMetrics.DENSITY_XHIGH: //MDPI
                            resizedBitmap = Bitmap.createScaledBitmap(bmps, 96, 96, false);
                            break;
                        case DisplayMetrics.DENSITY_XXHIGH: //MDPI
                            resizedBitmap = Bitmap.createScaledBitmap(bmps, 144, 144, false);
                            break;
                        case DisplayMetrics.DENSITY_XXXHIGH: //MDPI
                            resizedBitmap = Bitmap.createScaledBitmap(bmps, 192, 192, false);
                            break;
                        case DisplayMetrics.DENSITY_HIGH: //HDPI

                            resizedBitmap = Bitmap.createScaledBitmap(bmps, 72, 72, false);
                            break;
                        case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                            resizedBitmap = Bitmap.createScaledBitmap(bmps, 48, 48, false);
                            break;

                    }

                    lettersIcon.add(resizedBitmap);
                    if (fotos.exists()) {
                        if (bmps != null) {
                            bmps.recycle();
                            bmps = null;
                        }
                    }
                }
            } while (cl.moveToNext());
        }
        cl.close();
        db.close();
        GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), lettersIcon, lettersList);
        gridViews.setAdapter(gridAdapter);


        gridViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String nombreitem = (String) adapterView.getItemAtPosition(position);

                String source = (Long.valueOf(id)).toString();
                int cont = lettersList.size() - 1;

                int numEntero = Integer.parseInt(source);
                Bitmap image = lettersIcon.get(numEntero);
                showimageplay(nombreitem, image, numEntero);
            }
        });
        gridViews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String nombreitem = (String) adapterView.getItemAtPosition(position);
                String source = (Long.valueOf(id)).toString();
                int numEntero = Integer.parseInt(source);
                int cont = lettersList.size() - 1;
                String count = String.valueOf(cont);
                if (source.equals(count)) {
                } else {
                    createDialog(view, nombreitem, numEntero, true);
                }

                return true;
            }
        });
    }

    public void pasos(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("UPDATE usuario SET pasos='1'");
        db.close();
        LinearLayout paso = (LinearLayout) findViewById(R.id.paso1);
        paso.setVisibility(View.GONE);
        HorizontalScrollView categ = (HorizontalScrollView) findViewById(R.id.categscroll);
        categ.smoothScrollBy(80, 0);
    }


    int visiblemenu = 0;




    public void onClick(View v) {
        LinearLayout menu = (LinearLayout) findViewById(R.id.menu);
        visiblemenu++;
        menu.setVisibility(View.VISIBLE);
        if (visiblemenu == 2) {
            menu.setVisibility(View.GONE);
            visiblemenu = 0;
        }
        switch (v.getId()) {

            case R.id.facebook:
                //Start a new Activity MyCards.java
                menu.setVisibility(View.GONE);
                Uri uri = Uri.parse("https://www.facebook.com/Fassycom-1928923944094690/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.creditos: // AlerDialog when click on Exit
                menu.setVisibility(View.GONE);
                break;

            case R.id.tablets: // AlerDialog when click on Exit
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                menu.setVisibility(View.GONE);
                break;
            case R.id.phone: // AlerDialog when click on Exit
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                menu.setVisibility(View.GONE);
                break;
        }
    }

    public void sendfassy(View v) {  String MEDIA_shared = "/Fassycom/Shared";
        Intent compartiraudio = new Intent(Intent.ACTION_SEND_MULTIPLE);
        compartiraudio.setType("audio/*"); //el tipo es un audio

        for (int x = 0; x <= playitemaudio.size() - 1; x++) {
            String paths = null;

            String pathhout=Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_shared + File.separator  + playnom.get(x)+ ".mp3";
            File soundsend= new File(pathhout);
            if(soundsend.exists())
            {

            }else {
                if (localitemaudio.get(x).equals("sdcard")) {
                    paths = playitemaudio.get(x); //aquí selecciona el path dónde se almacena el audio y su extensión

                    File file1 = new File(paths);


                    File tofile1 = new File(pathhout);
                    try {
                        copy(file1, tofile1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


                if (localitemaudio.get(x).equals("local")) {
                    InputStream inputStream;

                    FileOutputStream fileOutputStream;
                    try {
                        Integer ogg = Integer.parseInt(playitemaudio.get(x));
                        inputStream = getResources().openRawResource(ogg);
                        fileOutputStream = new FileOutputStream(
                                new File(pathhout));

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, length);
                        }

                        inputStream.close();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            uris.add(Uri.parse(pathhout));
            Log.i("arraysend",uris.get(x).toString());

        }

        compartiraudio.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(compartiraudio, "Comparte un archivo de audio"));


    }
    public void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    public Dialog splashmodal()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.splash, (ViewGroup) findViewById(R.id.infosplash));
        TextView titlesplash=(TextView) Layout.findViewById(R.id.titlesplash);
        TextView link1=(TextView) Layout.findViewById(R.id.links1);
        TextView link2=(TextView) Layout.findViewById(R.id.links2);
        TextView forcilsa=(TextView) Layout.findViewById(R.id.forcilsa);
        TextView titleappsplash=(TextView) Layout.findViewById(R.id.titleappsplash);
        String font_path = "font/Reef.otf";
        String font_path2 = "font/AllerDisplay.ttf";
        Typeface TF2 = Typeface.createFromAsset(getAssets(), font_path2);
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
         titlesplash.setTypeface(TF);
        link1.setTypeface(TF);
        link2.setTypeface(TF);
        forcilsa.setTypeface(TF);
        titleappsplash.setTypeface(TF2);
        builder.setView(Layout);
        Dialog splash = builder.create();
        splash.show();
        return splash;
    }

    public  void  nexcateg(View view){
        HorizontalScrollView hScrollcateg = (HorizontalScrollView) findViewById(R.id.categscroll);
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth()-114;

        hScrollcateg.smoothScrollBy(screenWidth, 0);
        int scrollcateg=hScrollcateg.getScrollX();
        float maxscroll=hScrollcateg.getWidth();
        if (scrollcateg>=maxscroll)
        {
            Button nexcateg=(Button) findViewById(R.id.butonnext);
            nexcateg.setVisibility(View.GONE);
            Log.i("scrollimfo", "funciona");
        }else
        {
            Log.i("scrollimfo", String.valueOf(scrollcateg));
            Log.i("scrollimfo2", String.valueOf(maxscroll));
        }
    }


}