package desixweb.appcilsa;


import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {


    public ArrayList<Bitmap> lettersIcon = new ArrayList<>();
    public ArrayList<String> lettersList = new ArrayList<>();
    public ArrayList<String> playitemaudio = new ArrayList<>();
    public ArrayList<String> localitemaudio = new ArrayList<>();

    String selecionadacategoria = "";
    int idcateg = R.id.lineatouch;

    categoriaSQL mDbHelper = new categoriaSQL(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textitle=(TextView)findViewById(R.id.titleapp);
        String font_path = "font/Reef.otf";
        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);
        textitle.setTypeface(TF);

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

                //Si hemos abierto correctamente la base de datos

                //Insertamos 9 categoria de ejemplo

                LinearLayout listacategorias = (LinearLayout) findViewById(R.id.listacategorias);

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
                            Log.i("no entra porque   ", "base=" + base);
                        }

                        if (base.equals("sdcard")) {
                            String fotocategsdcard = c.getString(1);
                            Bitmap bmp = BitmapFactory.decodeFile(fotocategsdcard);
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

                            }
                            categorinew.setImageBitmap(resizedBitmap);

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
                                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                                adb.setTitle("¿Quiere borrar Categoria: " + " " + nombre + "?");
                                adb.setIcon(android.R.drawable.ic_dialog_alert);
                                adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                        mDbHelper.Deletecategoria(db, nombre,getApplicationContext());
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
                Log.i("valorinstalacionitem : ", String.valueOf(installcateg));
                View v1 = getWindow().getDecorView().getRootView();
                touchlist(v1);


                c.close();
                db.close();


            }


        }
    }

    ImageView setimg;

    public void addcateg(final String nom, String ruta) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO categorias (nombre,photo,base) " +
                "VALUES ('" + nom + "' , '" + ruta + "' , 'sdcard')");

        db.close();
        LinearLayout listacategorias = (LinearLayout) findViewById(R.id.listacategorias);
        final LinearLayout btnLO = new LinearLayout(this);
        LinearLayout.LayoutParams paramsLO = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnLO.setOrientation(LinearLayout.VERTICAL);
        paramsLO.setMargins(6, 6, 6, 6);
        btnLO.setLayoutParams(paramsLO);
        listacategorias.addView(btnLO);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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

        }
        final LinearLayout linea = new LinearLayout(this);
        categorinew.setImageBitmap(resizedBitmap);

        if (bmp != null) {
            bmp.recycle();
            bmp = null;
        }


        categorinew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridView gridViews = (GridView) findViewById(R.id.gridview);
                int oldid = idcateg;

                LinearLayout delete = (LinearLayout) findViewById(oldid);
                delete.setVisibility(View.INVISIBLE);

                idcateg = linea.getId();
                selecionadacategoria = nom;

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
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("¿Quiere borrar Categoria: " + " " + nom + "?");
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        mDbHelper.Deletecategoria(db, nom,getApplicationContext());
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
        int idlayout = 800;

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
    }

    private static String MEDIA_DIRECTORYTEMP = "/Fassycom/temp";
    private static String MEDIA_DIRECTORY = "/Fassycom/";
    private EditText texto1;

    private String mpath;
    private final int Photo_Code = 200;
    private final int SELECT_PICTURE = 100;
    private Dialog elegirfoto;
    long totalTiempo;
    long tiempoInicio;
    private static String mFileName = null;
    private MediaRecorder mRecorder = null;
    private boolean tenerphoto = false;
    private boolean teneraudio = false;


    public void mostrarobjet(String categoria, Boolean update) {
        final GridView gridViews = (GridView) findViewById(R.id.gridview);

        if (update) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cl = db.rawQuery(" SELECT nombre,foto,base FROM item Where categ='" + categoria + "'", null);
            if (cl.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    int catego = cl.getCount();
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
                        try {
                            bmps = rotateImage(foto1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        Bitmap resizedBitmap = null;
                        switch (metrics.densityDpi) {
                            case DisplayMetrics.DENSITY_HIGH: //HDPI

                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 72, 72, false);
                                break;
                            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                                resizedBitmap = Bitmap.createScaledBitmap(bmps, 48, 48, false);
                                break;

                        }
                        if (bmps != null) {
                            bmps.recycle();
                            bmps = null;
                        }
                        lettersIcon.add(resizedBitmap);


                    }
                    Log.i("item :  ", "nombre" + nombre + ",foto" + foto1 + ",base" + base);
                    Log.i("item total:  ", String.valueOf(catego));


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
                    mostrar(view);
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

    public void createDialog(final View view, final String borrar, final int source, final Boolean item) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        //adb.setView(Main.this);

        adb.setTitle("¿Quiere borrar Pictograma: " + " " + borrar + "?");
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (item) {

                    lettersList.remove(source);
                    lettersIcon.remove(source);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    mDbHelper.DeleteItem(db, borrar,selecionadacategoria,getApplicationContext());

                    mostrarobjet(selecionadacategoria, false);
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

    public void showimageplay(String nombre, Bitmap image, int source) {
        LinearLayout listaitem = (LinearLayout) findViewById(R.id.reproducion);
        LinearLayout linear = new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);
        listaitem.addView(linear);

        //crear imagen buton
        ImageButton itemplay = new ImageButton(this);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        itemplay.setImageBitmap(image);

        itemplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor clm = db.rawQuery(" SELECT uso,audio,base FROM item Where nombre='" + nombre + "'limit 1", null);
        int sumauso = 0;
        if (clm.moveToFirst()) {
            do {
                String uso = clm.getString(0);
                String base = clm.getString(2);
                String audio;
                audio = clm.getString(1);
                if (audio != null) {
                    if (!audio.isEmpty()) {
                        Log.i("mostrar  : ", "" + audio + " ,local : " + base);
                        playitemaudio.add(audio);
                        localitemaudio.add(base);
                    }
                }

                sumauso = Integer.parseInt(uso);
            }
            while (clm.moveToNext());
        }
        sumauso++;
        db.execSQL("UPDATE  item SET uso ='" + sumauso + "' where nombre='" + nombre + "'");
        db.close();
        clm.close();
    }

    Dialog dialog;

    public void crearcategoria(View v) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.nuevo_categoria, (ViewGroup) findViewById(R.id.agregarcategnew));
        builder.setView(Layout);
        dialog = elegirfoto = builder.create();
        elegirfoto.show();
        setimg = (ImageView) Layout.findViewById(R.id.setimagen2);
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
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
                        if (objeto.isEmpty()) {
                            Toast.makeText(getBaseContext(), "Campo de texto vacio", Toast.LENGTH_SHORT).show();
                        } else {

                            if (tenerphoto) {

                                //si foto y audio y texto estan llenos se procede a crear carpeta en caso de que no existe
                                File folder = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY + File.separator + objeto);
                                boolean success = true;
                                if (!folder.exists()) {

                                    success = folder.mkdirs();
                                }
                                if (success) {


                                    File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORYTEMP + File.separator + "/temp.jpg");
                                    File tofile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + objeto + "/" + objeto + ".jpg");

                                    if (file2.renameTo(tofile2)) {
                                        String fcateg = Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + objeto + "/" + objeto + ".jpg";
                                        File nomediaFile = new File(folder, ".nomedia");

                                        try {
                                            nomediaFile.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        addcateg(objeto, fcateg);
                                        Toast.makeText(getBaseContext(), "Creada : " + objeto, Toast.LENGTH_SHORT).show();
                                        tenerphoto = false;
                                        dialog.cancel();
                                    }


                                }

                            }
                        }

                    }
                }

        );

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

                                }
                            }
                    );

                    //  Bitmap image = BitmapFactory.decodeFile(mpath);
                    try {
                         Bitmap bmp=  rotateImage(mpath);
                        setimg.setImageBitmap(bmp);

                        tenerphoto = true;
                    } catch (IOException e) {
                        Log.w("TAG", "-- Error in setting image");
                    } catch (OutOfMemoryError oom) {
                        Log.w("TAG", "-- OOM Error in setting image");
                    }

                    // setimg.setImageBitmap(rotatedBitmap);


                    dialog2.dismiss();


                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();


                    try {
                        File file = new File(getPath(path));

                        Bitmap fotoselect =rotateImage(String.valueOf(file));
                        setimg.setImageBitmap(fotoselect);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    try {InputStream is;
                        is = getContentResolver().openInputStream(path);
                        Bitmap bitnmapx = BitmapFactory.decodeStream(is);


                        convertBitmapToFile(bitnmapx);
                        if (bitnmapx != null) {
                            bitnmapx.recycle();
                            bitnmapx = null;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    tenerphoto = true;
                    elegirfoto.dismiss();
                    break;
            }

        }

    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    public static Bitmap rotateImage(String mpath) throws IOException {
        File f = new File(mpath);
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

        Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f),
                null, options);
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), mat, true);
        ByteArrayOutputStream outstudentstreamOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100,
                outstudentstreamOutputStream);
        return  bmp;
    }

    private static void convertBitmapToFile(Bitmap bitmap) {
        String mpath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORYTEMP + File.separator + "temp.jpg";
        File imageFile = new File(mpath);

        OutputStream os;
        try {
            imageFile.delete();
            if (imageFile.exists()) {
            } else {
                os = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            }
        } catch (Exception e) {

        }
    }

    public void additem(String nombre, String foto, String audio) {
        if (nombre.isEmpty() || foto.isEmpty() || audio.isEmpty()) {

        } else {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                    "VALUES ('" + selecionadacategoria + "','" + nombre + "' , '" + foto + "' ,'" + audio + "','sdcard','0')");
            Log.i("ingresado item:  ", "categoria: " + selecionadacategoria + "nombre: " + nombre + " y foto: " + foto + ",audio" + audio);
            db.close();


            Bitmap bmps = null;
            try {
                bmps = rotateImage(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Bitmap resizedBitmap = null;
            switch (metrics.densityDpi) {
                case DisplayMetrics.DENSITY_HIGH: //HDPI

                    resizedBitmap = Bitmap.createScaledBitmap(bmps, 72, 72, false);
                    break;
                case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                    resizedBitmap = Bitmap.createScaledBitmap(bmps, 48, 48, false);
                    break;

            }
            int cont = lettersList.size() - 1;
            Log.i("datos vacios:  ", ""+cont+"");
            lettersList.remove(cont);
            lettersIcon.remove(cont);

            cont = lettersList.size() - 1;
            Log.i("datos vacios:  ", ""+cont+"");
            lettersIcon.add(resizedBitmap);
            lettersList.add(nombre);
            Bitmap tempxxv = BitmapFactory.decodeResource(getResources(), R.drawable.icon_add);
            lettersList.add("Nuevo");
            lettersIcon.add(tempxxv);
            if (bmps != null) {
                bmps.recycle();
                bmps = null;
            }

            mostrarobjet(selecionadacategoria,false);
        }
    }

 Boolean cancelrecord=false;
    public void mostrar(View v) {
//sedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.agregar, (ViewGroup) findViewById(R.id.contenedoragregar));
        builder.setView(Layout);
        Button cancelar_item = ((Button) Layout.findViewById(R.id.cancelitem));
        Button button_crear_categoria = ((Button) Layout.findViewById(R.id.butoncrearitems));
        // Add on
        TextView texto5=(TextView) Layout.findViewById(R.id.textfont1);
        final TextView textinfo=(TextView)Layout.findViewById(R.id.infotext);
        String font_path = "font/Reef.otf";
        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);
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
        // Add onClickListener to exit_button
        button_crear_categoria.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                texto1 = (EditText) Layout.findViewById(R.id.textobjet);
                String objeto = texto1.getText().toString();
                if (objeto.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Nombre  vacio", Toast.LENGTH_SHORT).show();
                } else {

                    if (tenerphoto && teneraudio) {
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        int compare=0;
                        Cursor cx = db.rawQuery(" SELECT nombre FROM item  WHERE nombre='"+objeto+"'", null);
                        if (cx.moveToFirst()) {
                            //Recorremos el cursor hasta que no haya más registros

                            do {
                                compare=cx.getCount();

                            }while (cx.moveToNext());

                            } cx.close(); db.close(); if(compare==1){ Toast.makeText(getBaseContext(), "Ya existe el nombre", Toast.LENGTH_SHORT).show();}
                            else{
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

                            File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORYTEMP + File.separator + "/temp.ogg");
                            File tofile1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + selecionadacategoria + "/" + objeto + ".ogg");
                            File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORYTEMP + File.separator + "/temp.jpg");
                            File tofile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + selecionadacategoria + "/" + objeto + ".jpg");

                            if (file1.renameTo(tofile1) && file2.renameTo(tofile2)) {
                                String fileaudio = Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + selecionadacategoria + "/" + objeto + ".ogg";
                                String fitem = Environment.getExternalStorageDirectory().getAbsolutePath() + MEDIA_DIRECTORY + File.separator + selecionadacategoria + "/" + objeto + ".jpg";

                                additem(objeto, fitem, fileaudio);

                                Toast.makeText(getBaseContext(), "Creado Pictograma: " + objeto, Toast.LENGTH_SHORT).show();
                                teneraudio = false;
                                tenerphoto = false;
                                dialog.cancel();
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
                        cancelrecord=false;
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
                        if (success) { Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
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
                                    if (totalTiempo> 500 && !cancelrecord)
                                    {

                                        if(mRecorder!=null)
                                        {
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
                            cancelrecord=true;
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
                            Toast.makeText(getBaseContext(), "grabado", Toast.LENGTH_SHORT).show();
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
    Boolean questionplay=false;int quest=0;
    public void borrar(View view) {
        LinearLayout question=(LinearLayout)findViewById(R.id.question);
        LinearLayout listaitemborrar = (LinearLayout) findViewById(R.id.reproducion);
        listaitemborrar.removeAllViews();
        localitemaudio.clear();
        playitemaudio.clear();
        questionplay=false;
        question.setVisibility(View.GONE);
        quest=0;

    }
    MediaPlayer mp;
    int currentposition=0;
    Boolean pausa=false;


public void question(View view)
{quest++;
    LinearLayout question=(LinearLayout)findViewById(R.id.question);

    if(quest==1&&playitemaudio.size()==0&& !questionplay){
    question.setVisibility(View.VISIBLE);
    playitemaudio.add(String.valueOf(R.raw.unapregunta));
    localitemaudio.add("local");
questionplay=true;

}
if(quest>=2&&playitemaudio.size()>1&&questionplay){
        question.setVisibility(View.GONE);
        quest=0;
        questionplay=false;
        playitemaudio.remove(0);
        localitemaudio.remove(0);

    }
}
    public void play(View view) {

        reproducir();
    }

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

                    Log.i("data  : ","local : "+localitemaudio.get(currentposition));
                    mp = new MediaPlayer();


                    if(localitemaudio.get(currentposition).equals("local")){
                        Integer ogg=Integer.parseInt(playitemaudio.get(currentposition));
                        mp = MediaPlayer.create(this,ogg);
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                       // mp.setLooping(false);
                        if (mp != null) {
                        mp.start();}}


                    if(localitemaudio.get(currentposition).equals("sdcard"))
                    {
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
                        if (mp != null) {
                            mp.start();
                        }}
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
            mp=null;
            Button pause = (Button) findViewById(R.id.pause);
            Button play = (Button) findViewById(R.id.play);
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            HorizontalScrollView hScrollView= (HorizontalScrollView) findViewById(R.id.scrollplay);
            hScrollView.scrollTo(0, 0);
        } else {
            if(!pausa) {

                currentposition++;
                reproducir();
                HorizontalScrollView hScrollView= (HorizontalScrollView) findViewById(R.id.scrollplay);
                    int dopx=currentposition*30;
                hScrollView.smoothScrollBy(dopx, 0);
            }
        }

    }
    public  void pause(View view)
    {
        pausa=true;
        if(mp!=null){
        if(mp.isPlaying())
    {
        mp.stop();

        Button pause = (Button) findViewById(R.id.pause);
        Button play = (Button) findViewById(R.id.play);
        pause.setVisibility(View.INVISIBLE);
        play.setVisibility(View.VISIBLE);
    }
        }

    }
    public void touchlist(View view) {
        int oldid=idcateg;

            LinearLayout delete = (LinearLayout) findViewById(oldid);
            delete.setVisibility(View.INVISIBLE);

        LinearLayout showlineatouch=(LinearLayout)findViewById(R.id.lineatouch);
        showlineatouch.setVisibility(View.VISIBLE);
        idcateg=R.id.lineatouch;
        lettersList.clear();
        lettersIcon.clear();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final GridView gridViews = (GridView) findViewById(R.id.gridview);
        Cursor cl = db.rawQuery(" SELECT nombre,foto,base FROM item where NOT uso==0  ORDER BY uso DESC LIMIT 8", null);
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
                    Bitmap bmps = BitmapFactory.decodeFile(foto1);
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    Bitmap resizedBitmap = null;
                    switch (metrics.densityDpi) {
                        case DisplayMetrics.DENSITY_HIGH: //HDPI

                            resizedBitmap = Bitmap.createScaledBitmap(bmps, 72, 72, false);
                            break;
                        case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                            resizedBitmap = Bitmap.createScaledBitmap(bmps, 48, 48, false);
                            break;

                    }

                    lettersIcon.add(resizedBitmap);
                }
            }                 while (cl.moveToNext());
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
                    createDialog(view, nombreitem, numEntero,true);
                }

                return true;
            }
        });
        }

        public void pasos(View view)
        {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            db.execSQL("UPDATE usuario SET pasos='1'");
            LinearLayout paso=(LinearLayout)findViewById(R.id.paso1);
            paso.setVisibility(View.GONE);
            HorizontalScrollView categ=(HorizontalScrollView) findViewById(R.id.categscroll);

            categ.smoothScrollBy(42, 0);
        }


int visiblemenu=0;
    public void onClick(View v) {
LinearLayout menu=(LinearLayout)findViewById(R.id.menu);
        visiblemenu++;
        menu.setVisibility(View.VISIBLE);
        if(visiblemenu==2)
        {
            menu.setVisibility(View.GONE);
            visiblemenu=0;
        }
        switch(v.getId()){

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
        }
    }


    }

