package com.android.fassycom;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.appcilsa.BuildConfig;
import android.appcilsa.R;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {
    RecyclerView.LayoutManager mLayaoutmangaer;
    RecyclerView.Adapter mAdapter;
    RecyclerView.Adapter mAdapterPlay;
    GridAdapter gridAdapter;
    @BindView(R.id.viewnewcateg) RecyclerView mRecycleView;
    @BindView(R.id.viewnewPlay) RecyclerView mRecyclePlay;
    @BindView(R.id.play) Button play;
    @BindView(R.id.pause) Button pause;
    @BindView(R.id.menuSetting) Button menu;
    @BindView(R.id.gridview)  GridView gridViews;
    Boolean delte=false;
    View holdview;
    ArrayList<CategoriaObject> mCateg=new ArrayList<>();
    ArrayList<Pictograma> pictoGridview=new ArrayList<>();
    ArrayList<Pictograma> pictoPlay=new ArrayList<>();
    ArrayList <Uri> uris = new ArrayList <> ();
    Boolean modoeditar=false;
    Boolean modoplay=false;
    String selecionadacategoria;
    DataSQL mDbHelper = new DataSQL(this);
    SQLiteDatabase db;
    Dialog editarclear;
    Dialog editar = null;
    Dialog mas_foto = null;
    Dialog pictogramashow;
    Dialog categoriashow;
    ImageView setimg;
    private EditText texto1;
    private String mpath;
    private final int Photo_Code = 200;
    private final int Requestsetting=78;
    private final int SELECT_PICTURE = 100;
    private Dialog elegirfoto;
    private MediaRecorder mRecorder = null;
    private boolean tenerphoto = false;
    private boolean teneraudio = false;
    Uri saveimag=null;
    Activity actividad=this;
    final int pCode = 12321;
    final int pCodeX = 12336;
    String instalacion = "";
    boolean repro;
    boolean edit;
    int Tamaño=0;
    int   sizecateg=0;
    Boolean pausa = false;
    boolean imagenlocal;
    int currentposition = 0;
    int quest = 0;
    MediaPlayer mp;
    Dialog closet;
    LinearLayout [] tutox=new LinearLayout [8];
    Boolean seleccateg=false;
    String nompictomov;
    int extraimg;
     static String rutaprincipal= Environment.getExternalStorageDirectory()+"/Android/data/";
    String rutavieja=Environment.getExternalStorageDirectory()+File.separator+"Fassycom";
    String rutafassycom;
    private static String MEDIA_DIRECTORYTEMP =rutaprincipal+"/.temp";
    private static String mFileName =MEDIA_DIRECTORYTEMP + File.separator + "temp.ogg";
    File fotos;
    Object fpicto;
    Boolean cancelrecord = false;
    Boolean startRecord = false;
    String accionError="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
       Tamaño = Getdpi();
        db = mDbHelper.getWritableDatabase();
        String packageName = getApplicationContext().getPackageName();
        rutafassycom = rutaprincipal + packageName + "/Fassycom/";
        //codigo para que el usuario mande error
        Thread.setDefaultUncaughtExceptionHandler(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        final int value = getResources().getConfiguration().orientation;
        if (value == Configuration.ORIENTATION_PORTRAIT) {
            closet = splashModal();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //obtener dato de pasos de tuturial y instalacion
        Cursor us = db.rawQuery("SELECT instalacion,pasos,repro,edit FROM usuario", null);
        if (us.moveToFirst()) {
            do {
                int pasos = us.getInt(1);
                instalacion = us.getString(0);
                repro = Boolean.parseBoolean(us.getString(2));
                edit = Boolean.parseBoolean(us.getString(3));
                tutox[1] = findViewById(R.id.pas1);
                tutox[2] = findViewById(R.id.pas2);
                tutox[3] = findViewById(R.id.pas3);
                tutox[4] = findViewById(R.id.pas4);
                tutox[5] = findViewById(R.id.pas5);
                tutox[6] = findViewById(R.id.pas6);
                tutox[7] = findViewById(R.id.pas7);
                if (pasos == 7) {
                    tutox[7].setVisibility(View.GONE);
                } else {
                    for (int m=1;m<7;m++)
                       if(m==pasos){
                           tutox[m].setVisibility(View.VISIBLE);
                       }
                }

            } while (us.moveToNext());
        }
        us.close();

        //Adaptador de reproducion
        mRecyclePlay.setHasFixedSize(true);
        mLayaoutmangaer =new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclePlay.setLayoutManager(mLayaoutmangaer);
        mAdapterPlay=new RecyclePlaydapter(pictoPlay,Tamaño);
        mRecyclePlay.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycleView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
            @Override
            public void onLongClick(View view, int position) {
                // si mantiene apretado sobre el pictograma de la lista de reproducion se borrara
              accionError="Borrando pictograma reproducion";
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vib != null) {
                    vib.vibrate(100);
                }
                if(position>=pictoPlay.size()) {
                    pictoPlay.remove(pictoPlay.size()-1);
                    mAdapterPlay.notifyDataSetChanged();
                }else {
                    pictoPlay.remove(position);
                    mAdapterPlay.notifyDataSetChanged();
                }
            }
        }));
        mRecyclePlay.setAdapter(mAdapterPlay);

        //Adaptador de categorias
       int cachecateg= generarCategorias(true);
        mRecycleView.setHasFixedSize(true);
        mLayaoutmangaer =new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecycleView.setLayoutManager(mLayaoutmangaer);
        mAdapter=new RecycleCategdapter(mCateg,Tamaño);
        mRecycleView.setItemViewCacheSize(cachecateg);
        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycleView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                      accionError="Toca categoria ";
                        View bar=view.findViewById(position);
                        if(delte){
                          holdview.setVisibility(View.INVISIBLE);
                          delte=false;
                        }
                        bar.setVisibility(View.VISIBLE);
                        holdview=bar;
                        delte=true;
                       String nombrecateg= mCateg.get(position).nombre;
                        String tipo= mCateg.get(position).tipo;
                        accionesCategoria(nombrecateg,tipo);

                    }
                    @Override
                    public void onLongClick(View view, int position) {
                      accionError="Mantiene apretado categoria";
                        String tipo= mCateg.get(position).tipo;
                        String nombrex= mCateg.get(position).nombre;
                        if (tipo.equals("ninguno")) {
                            editarCateg(nombrex,position);
                        }
                    }

                }));
        mRecycleView.setAdapter(mAdapter);

        // Mostrar categoria cuando inicia la app
        String nombrecateg= mCateg.get(0).nombre;
        String tipo= mCateg.get(0).tipo;
        accionesCategoria(nombrecateg,tipo);

        if (!instalacion.equals("True")) {

            if (Build.VERSION.SDK_INT >= 23) {
                isStoragePermissionGranted();
                if (isStoragePermissionGranted()) {
                    createFolderApp();
                    if (checkedoldfassycom(db)) {
                        Log.i("permiso concedidos", "sisi");
                        String savedata = rutafassycom + "/install";
                        File savedat = new File(savedata);
                        if (!savedat.exists()) {
                            savedat.mkdirs();
                        }
                    } else {
                        String savedata = rutafassycom + "/install";
                        File savedat = new File(savedata);
                        if (!savedat.exists()) {
                            savedat.mkdirs();
                        }
                    }
                }
            } else {
                if (checkedoldfassycom(db)) {
                    Log.i("permiso concedidos", "sisi");
                    String savedata = rutafassycom + "/install";
                    File savedat = new File(savedata);
                    if (!savedat.exists()) {
                        savedat.mkdirs();
                    }
                } else {
                    String savedata = rutafassycom + "/install";
                    File savedat = new File(savedata);
                    if (!savedat.exists()) {
                        savedat.mkdirs();
                    }
                }
                Boolean result = createFolderApp();
                if (!result) {
                    Log.i("instalcion cs falla: ", rutafassycom + "Import");

                }
                Log.i("instalcion carpetas", String.valueOf(result));
            }
        }
        // Splash
        Thread timerTread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (value == Configuration.ORIENTATION_PORTRAIT) {
                        closet.dismiss();
                    }

                    runOnUiThread(new Runnable() {
                        public void run() {}
                    });
                }
            }
        };
        timerTread.start();


        menu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vib != null) {
                    vib.vibrate(60);
                }
                Intent intentx = new Intent(getApplicationContext(), SettingMenu.class);
                startActivityForResult(intentx, Requestsetting);
                pasos(7);
                return false;
            }
        });
        if (repro) {
            modoplay = true;
        }
        if (edit) {
            modoeditar = true;
        }
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        }
    }
    public void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {

            modalCreatePictogram(0, modoeditar, null);
            // Update UI to reflect image being shared
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setRequestedSize(192, 192)
                    .start(actividad);
            tenerphoto = true;
        }
    }
    public Boolean createFolder(String rutafolder) {
        boolean success = false;
        File folder = new File(rutafolder);
        if (Build.VERSION.SDK_INT >= 23) {
            if (isStoragePermissionGranted()) {
                if (!folder.exists()) {
                    success = folder.mkdir();
                }
            } else {
                Toasty.warning(getApplicationContext(), "Permiso denagado", Toast.LENGTH_LONG,true).show();
            }
        } else {
            if (!folder.exists()) {

                success = folder.mkdirs();
            }
        }
        return success;
    }

    public Boolean createFolderApp() {
        Boolean install = false;
        String MEDIA_import = rutafassycom + "Import";
        String MEDIA_shared = rutafassycom + "Shared";
        String MEDIA_categ = rutafassycom + "Categories";
        if (createFolder(MEDIA_shared) && createFolder(MEDIA_import) && createFolder(MEDIA_categ)) {
            install = true;
            SQLiteDatabase dba = mDbHelper.getWritableDatabase();
            dba.execSQL("UPDATE usuario SET instalacion='true'");
            dba.close();
        }
        return install;
    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, pCode);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    public boolean MicrophonePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                Manifest.permission.RECORD_AUDIO
                        }, pCodeX);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    public boolean CameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                Manifest.permission.CAMERA
                        }, pCodeX);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public int  generarCategorias(Boolean nuevo) {
        if(nuevo){  mCateg.add(new CategoriaObject("Frecuentes",R.drawable.uso,"frecuentes"));}
        Cursor c = db.rawQuery(" SELECT nombre,photo,base FROM categorias", null);
        if (c.moveToFirst()) {
            do {
                sizecateg=c.getCount();
                String nombre = c.getString(0);
                String fotocateg = c.getString(1);

                if (isnumeric(fotocateg)) {
                    int fotol = c.getInt(1);
                    mCateg.add(new CategoriaObject(nombre,fotol,"ninguno"));
                } else {
                    mCateg.add(new CategoriaObject(nombre,fotocateg,"ninguno"));
                }
            }
            while (c.moveToNext());
        }
        c.close();
        if(nuevo){ mCateg.add(new CategoriaObject("Nuevo",R.drawable.icon_add,"nuevo"));}
        return sizecateg;
    }
    public void accionesCategoria(String nombre,String tipo){


        if (tipo.equals("ninguno")) {
            pasos(1);
            selecionadacategoria = nombre;
            Log.i("abrircateg", selecionadacategoria);
            gridViews.invalidateViews();
            pictoGridview.clear();
            if (seleccateg) {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.execSQL(" UPDATE item SET categ='" + nombre + "' WHERE nombre='" + nompictomov + "'");
                Toasty.success(getApplicationContext(), nompictomov + " Movido a categoria " + nombre, Toast.LENGTH_SHORT, true).show();
                seleccateg = false;
            }
            pictogramas(selecionadacategoria, false);
        }
        if (tipo.equals("frecuentes")) {
            pictogramas("", true);
            pasos(5);
        }
        if (tipo.equals("nuevo")) {
            if (!modoeditar) {
                crearCategorias(selecionadacategoria,0);
            } else {
                Toasty.warning(getApplicationContext(), "Desactive  editar", Toast.LENGTH_SHORT, true).show();
            }
        }
    }


    public void editarCateg(final String nombre, final int position) {
        if (modoeditar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View Layout = inflater.inflate(R.layout.setpictograma, (ViewGroup) findViewById(R.id.editon));
            builder.setView(Layout);

            LinearLayout editarcateg = Layout.findViewById(R.id.editarcateg);
            editarcateg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  accionError="Edita categoria";
                    crearCategorias( nombre,position);
                }
            });
            LinearLayout movercateg = Layout.findViewById(R.id.mover);
            movercateg.setVisibility(View.GONE);
            LinearLayout delete = Layout.findViewById(R.id.tacho);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                    adb.setTitle("¿Quiere borrar Categoria: " + " " + nombre + "?");
                    adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            accionError="borra categoria";
                            db=mDbHelper.getWritableDatabase();
                            mDbHelper.Deletecategoria(db, nombre, getApplicationContext());
                            mCateg.remove(position);
                            mAdapter.notifyDataSetChanged();
                            editar.dismiss();
                            //listaitemborrar.removeAllViews();
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

    } Object fcateg;
    public void crearCategorias(final String nombrecateg, final int positionx) {

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.nuevo_categoria, (ViewGroup) findViewById(R.id.agregarcategnew));
        builder.setView(Layout);
        categoriashow = builder.create();
        categoriashow.show();
        setimg = Layout.findViewById(R.id.setimagen2);
        if (modoeditar) {
            db=mDbHelper.getWritableDatabase();
            Cursor c = db.rawQuery(" SELECT nombre,photo,base FROM categorias  WHERE nombre='" + nombrecateg + "'", null);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String foto6 = c.getString(1);
                    int foto5;
                    EditText placenom = Layout.findViewById(R.id.textobjet);
                    placenom.setText(nombrecateg);
                    if (isnumeric(foto6)) {
                        foto5 = c.getInt(1);
                       fcateg = foto5;
                        Glide.with(this).load(foto5).apply(new RequestOptions().centerCrop()).into(setimg);
                    } else {
                        Uri urix = Uri.parse(foto6);
                        fotos= new File(foto6);
                        fcateg=foto6;
                        saveimag = urix;
                        Glide.with(this).load(foto6).apply(new RequestOptions().centerCrop()).into(setimg);
                    }
                }
                while (c.moveToNext());
            }
            c.close();
            db.close();
            tenerphoto = true;
        }
        Button button_cancelar_categoria = Layout.findViewById(R.id.cancelarcrearcategoria);
        Button button_crear_categoria = Layout.findViewById(R.id.butoncrearitems);
        // Add onClickListener to exit_button
        button_cancelar_categoria.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        categoriashow.dismiss();
                    }
                }
        );
        categoriashow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {}
        });
        // Add onClickListener to exit_button
        button_crear_categoria.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {accionError="Apreta para crear categoria";
                        texto1 = Layout.findViewById(R.id.textobjet);
                        String nombrenuevito = texto1.getText().toString();
                        String nombre = nombrenuevito.replace("'", "!");
                        if (nombrenuevito.isEmpty()) {
                            Toasty.warning(getBaseContext(), "Falta ingresar nombre.", Toast.LENGTH_SHORT, true).show();
                        } else {
                            if (tenerphoto) {
                                //pic33 foto y audio y texto estan llenos se procede a crear carpeta en caso de que no existe
                            File folder = new File(rutafassycom + "Categories" + File.separator + nombre);
                                boolean success = true;
                                if (!folder.exists()) {
                                    Log.i("foldererror", "no existe");
                                    Log.i("foldererrorruta", String.valueOf(folder));
                                    success = folder.mkdirs();
                                }
                                if (!success) {
                                    Toasty.error(getBaseContext(), "Error al crear la carpeta.", Toast.LENGTH_SHORT, true).show();
                                }
                                if (success || folder.exists()) {
                                    File tempFolder = new File(MEDIA_DIRECTORYTEMP);
                                    if (!tempFolder.exists()) {
                                        tempFolder.mkdirs();
                                    }
                                    if (saveimag != null) {
                                        saveFile(saveimag);
                                    }


                                        File file2 = new File(tempFolder + "/temp.jpg");
                                    if(file2.exists()){
                                        File tofile2new = new File(rutafassycom + "Categories" + File.separator + nombre +System.currentTimeMillis()+".jpg");

                                        if (file2.renameTo(tofile2new)) {
                                            if (saveimag != null) {
                                                saveimag = null;
                                            }
                                        }
                                        File nomediaFile = new File(folder, ".nomedia");
                                        try {
                                            nomediaFile.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        fcateg  = String.valueOf(tofile2new);
                                    }


                                    if(imagenlocal){
                                        fcateg=extraimg;
                                    }

                                    db=mDbHelper.getWritableDatabase();
                                        if (modoeditar) {
                                          accionError="Modifica categoria";
                                                File foldernew = new File(rutafassycom + "Categories" + File.separator + nombre);
                                                db.execSQL("UPDATE categorias  SET nombre='" + nombre + "' ,photo='" + fcateg + "' WHERE nombre='" + nombrecateg + "'");
                                                db.execSQL("UPDATE item  SET categ='" + nombre+"' WHERE categ='" + nombrecateg+"' ");
                                            Toasty.success(getBaseContext(), "Editada  : " + nombrecateg, Toast.LENGTH_SHORT, true).show();
                                                mCateg.set(positionx,new CategoriaObject(nombre,fcateg,"ninguno"));
                                            File itsremove = new File(String.valueOf(fotos));

                                            if(itsremove.exists()){
                                                if( itsremove.delete()){
                                                    fotos=null;
                                                }
                                            }
                                            tenerphoto = false;

                                            mAdapter.notifyDataSetChanged();

                                                if ( editarclear!=null) {
                                                    if(editarclear.isShowing()){
                                                        editarclear.dismiss();
                                                    }

                                                }
                                                if(categoriashow!=null){
                                                    if (categoriashow.isShowing()) {
                                                        categoriashow.cancel();
                                                    }
                                                }

                                        } else {

                                            if(imagenlocal){
                                             fcateg=extraimg;
                                            }
                                          accionError="Crea categoria";
                                            db.execSQL("INSERT INTO categorias (nombre,photo) " +
                                                    "VALUES ('" + nombre + "' , '" + fcateg + "')");
                                            tenerphoto = false;
                                            Toasty.success(getBaseContext(), "Creada : " + nombre, Toast.LENGTH_SHORT, true).show();
                                           int removeadd=mCateg.size()-1;
                                           mCateg.remove(removeadd);
                                            mCateg.add(new CategoriaObject(nombre,fcateg,"ninguno"));
                                            mCateg.add(new CategoriaObject("Nuevo",R.drawable.icon_add,"nuevo"));
                                            mAdapter.notifyDataSetChanged();
                                            if (categoriashow.isShowing()) {
                                                categoriashow.cancel();
                                            }
                                        }imagenlocal=false;
                                    }
                                }
                            else {Toasty.warning(getBaseContext(), "Se requiere la imagen  ", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }
                }

        );


    }

    public void pictogramas(String categoria, final Boolean frecuente) {

        db = mDbHelper.getReadableDatabase();
        pictoGridview.clear();
        Cursor cl;
        if (frecuente) {
            cl = db.rawQuery(" SELECT nombre,foto,base FROM item WHERE  NOT uso==0  ORDER BY uso DESC LIMIT 20", null);
        } else {
            cl = db.rawQuery(" SELECT nombre,foto,base FROM item Where categ='" + categoria + "'", null);
        }
        if (cl.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre = cl.getString(0);
                int foto = cl.getInt(1);
                String foto1 = cl.getString(1);
               // String base = cl.getString(2);
                if(isnumeric(foto1))
                {
                    pictoGridview.add ( new Pictograma(null,nombre,foto,null,null));
                }else {
                    pictoGridview.add ( new Pictograma(null,nombre,foto1,null,null));
                }
            } while (cl.moveToNext());
        }
        cl.close();
        db.close();
        if (!frecuente) {
            String newx= getResources().getString(R.string.nuevo);
            pictoGridview.add ( new Pictograma(null,newx,R.drawable.icon_add,null,null));
        }
        gridViews.invalidateViews();
       gridAdapter = new GridAdapter(getApplicationContext(),pictoGridview, Tamaño+72);
        gridAdapter.notifyDataSetChanged();
        gridViews.setAdapter(gridAdapter);
        gridViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > adapterView, View view, int position, long id) {

                pasos(2);
                String source = (Long.valueOf(id)).toString();
                int cont =   pictoGridview.size()-1;;
                String nombreitem =pictoGridview.get(position).getNombrepic();
                accionError="Apreta pictograma "+nombreitem;
                String count = String.valueOf(cont);
                if (source.equals(count)) {
                    if (!modoeditar) {
                      if(!frecuente){
                        modalCreatePictogram(0,modoeditar, null);
                      }


                    } else {
                        Toasty.warning(getApplicationContext(), "Desactive Modo editar", Toast.LENGTH_SHORT, true).show();
                    }

                } else {
                    int numEntero = Integer.parseInt(source);
                    Object image = pictoGridview.get(numEntero).getFoto();
                    addPictogramReproductor(nombreitem, image);
                    if (modoplay) {
                        autoPlaySound();
                    }
                }


            }
        });
        gridViews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView < ? > adapterView, View view, int position, long id) {
              accionError="Mantiene apretado pictograma";
                if (!frecuente) {
                    String source = (Long.valueOf(id)).toString();
                    String nombrepic =pictoGridview.get(position).getNombrepic();
                    int numEntero = Integer.parseInt(source);

                    int cont = pictoGridview.size() - 1;
                    String count = String.valueOf(cont);
                    if (!source.equals(count)) {
                        editarPictograma(position, nombrepic, numEntero, true);
                    }
                }
                return true;
            }
        });
    }
    public void editarPictograma(final int positionx, final String borrar, final int source, final Boolean item) {
        if (modoeditar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View Layout = inflater.inflate(R.layout.setpictograma, (ViewGroup) findViewById(R.id.editon));
            builder.setView(Layout);

            LinearLayout editarcateg = Layout.findViewById(R.id.editarcateg);
            editarcateg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accionError="Toca para actualizar pictograma";
                    modalCreatePictogram(positionx,modoeditar, borrar);
                }
            });

            LinearLayout delete = Layout.findViewById(R.id.tacho);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accionError="Aprieta para borrar pictograma";
                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                    adb.setTitle("¿Quiere borrar Pictograma: " + " " + borrar + "?");
                    adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (item) {
                                accionError="Borra pictograma";
                                db=mDbHelper.getWritableDatabase();
                                mDbHelper.DeleteItem(db, borrar, getApplicationContext(), borrar);
                                pictoGridview.remove(source);
                                pictogramas(selecionadacategoria,false);
                                editarclear.dismiss();
                            }
                        }
                    });

                    adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alertDialog = adb.create();
                    alertDialog.show();

                }
            });
            LinearLayout savepic = Layout.findViewById(R.id.mover);
            savepic.setVisibility(View.VISIBLE);
            savepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  accionError="Borra pictograma";
                    // mover pictograma a la categoria selecionada
                    Toasty.info(getApplicationContext(), "Seleciona   la categoria", Toast.LENGTH_SHORT, true).show();
                    seleccateg = true;
                    nompictomov = borrar;
                    editarclear.dismiss();
                }
            });

            editarclear = builder.create();
            editarclear.show();

        }

    }
    int playauto=0;
    public void addPictogramReproductor(String nombre, Object image) {
     //   mRecycleView.setItemViewCacheSize(cachecateg);
        int sumauso = 0;
        String base;
        String audio;
        String[] args = new String[] {
                String.valueOf(nombre)
        };
        db = mDbHelper.getReadableDatabase();
        Cursor clm = db.rawQuery(" SELECT uso,audio,base FROM item Where nombre=?  limit 1", args);
        if (clm.moveToFirst()) {
            do {
                // se Obtiene sonido de la base de datos
                String uso = clm.getString(0);
                base = clm.getString(2);
                audio = clm.getString(1);
                if(isnumeric(audio)){
                    int audio1 = clm.getInt(1);
                    pictoPlay.add(new Pictograma(null,nombre,image,audio1,base));
                }else{
                    if (audio != null) {
                        if (!audio.isEmpty()) {
                            File audiorep = new File(audio);
                            if (!audiorep.exists()) {
                                Toasty.error(getBaseContext(), "Grabación no encontrada", Toast.LENGTH_SHORT, true).show();
                                image=R.drawable.caution;
                            }
                            // se agrega a  pictoplay el sonido , la imagen y nombre
                            pictoPlay.add(new Pictograma(null,nombre,image,audio,base));
                        }
                    }
                }

                sumauso = Integer.parseInt(uso);
            }
            while (clm.moveToNext());
        }
        // se actualiza el adaptador de reproducion con los nuevos pictogramas tocados
        mAdapterPlay.notifyDataSetChanged();
        //al actualizarse se mueve el scroll
      if(pictoPlay.size()>2){
        mRecyclePlay.smoothScrollToPosition(pictoPlay.size());
      }
        playauto++;
        sumauso++;
        db.execSQL("UPDATE  item SET uso ='" + sumauso + "' where nombre='" + nombre + "'");
        db.close();
        clm.close();
    }

    public void getPhotoDialog(View v) {
        CameraPermissionGranted();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View Layoutfoto = inflater.inflate(R.layout.elegirfoto, (ViewGroup) findViewById(R.id.contenedorelegirfoto));
        builder.setView(Layoutfoto);
        Button cancelarelegir = Layoutfoto.findViewById(R.id.cancelelegir);
        LinearLayout take = Layoutfoto.findViewById(R.id.sacarfoto);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CameraPermissionGranted()) {
                    openCamara();
                }
            }
        });
        elegirfoto = builder.create();
        elegirfoto.show();
        cancelarelegir.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        elegirfoto.cancel();
                    }
                }

        );

    }

    ProgressDialog carga;
    public void openCamara() {

        File folder = new File(MEDIA_DIRECTORYTEMP);
        boolean success = true;
        if (!folder.exists()) {

            success = folder.mkdirs();
        }
        if (success) {
            File nomediaFile = new File(folder, ".nomedia");
            try {
                nomediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imagename = "temp.jpg";
            mpath = MEDIA_DIRECTORYTEMP + File.separator + imagename;
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            File newfile = new File(mpath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photouri = Uri.fromFile(newfile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
            startActivityForResult(intent, Photo_Code);
            carga = new ProgressDialog(MainActivity.this);
            carga.setMessage("Cargando editor de fotos....");
            carga.setTitle("Fassycom");
            carga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            carga.show();
        }
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Seleciona una imagen"), SELECT_PICTURE);
        carga = new ProgressDialog(MainActivity.this);
        carga.setMessage("Cargando editor de fotos....");
        carga.setTitle("Fassycom");
        carga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        carga.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // CropImage(uri);
            switch (requestCode) {
                case Photo_Code:
                    MediaScannerConnection.scanFile(this,
                            new String[] {
                                    mpath
                            }, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String mpath, Uri uri) {
                                    if (carga != null) {
                                        if (carga.isShowing()) {
                                            carga.dismiss();
                                        }
                                    }
                                    CropImage.activity(uri)
                                            .setAspectRatio(1, 1)
                                            .setRequestedSize(192, 192)
                                            .start(actividad);
                                    tenerphoto = true;
                                }
                            }
                    );
                    elegirfoto.dismiss();
                    break;
                case SELECT_PICTURE:
                    if (carga != null) {
                        if (carga.isShowing()) {
                            carga.dismiss();
                        }
                    }
                    //obtengo uri de la imagen selecionada
                    Uri path = data.getData();
                    saveimag = path;
                    // se pasa la imagan al recortador de fotos
                    CropImage.activity(path)
                            .setAspectRatio(1, 1)
                            .setRequestedSize(192, 192)
                            .start(actividad);
                    //aprobado imagen cargada para crear pictograma
                    //cierro diagolo
                    elegirfoto.dismiss();
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    imagenlocal=false;
                    if (carga != null) {
                        if (carga.isShowing()) {
                            carga.dismiss();
                        }
                    }
                    //obtengo el  uri de la foto cortada
                    Uri resultUri = result.getUri();
                    //guardo la imagen  en una variable
                    saveimag = resultUri;
                    //muestro imagen
                    tenerphoto = true;
                    Glide.with(this).load(resultUri).apply(new RequestOptions().centerCrop()).into(setimg);
                    if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                        if (carga.isShowing()) {
                            carga.dismiss();
                        }
                    }
                    break;
                case Requestsetting:
                    modoeditar = data.getBooleanExtra("edit", false);
                    Log.i("modoedit", String.valueOf(modoeditar));
                    modoplay = data.getBooleanExtra("repro", false);
                    String rotate = data.getStringExtra("rotate");
                    if (rotate.equals("LANDSCAPE")) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);ButterKnife.bind(this);
                    }
                    if (rotate.equals("PORTRAIT")) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
            }

        }

    }

    public void saveFile(Uri sourceuri) {
        String sourceFilename = sourceuri.getPath();

        String destinationFilename = MEDIA_DIRECTORYTEMP + File.separator + "temp.jpg";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void addItem(String nombre, Object foto, String audio) {
        if (!nombre.isEmpty() || foto!=null || !audio.isEmpty()) {
            db=mDbHelper.getWritableDatabase();
            mDbHelper.Insertpictograma(db,this,selecionadacategoria,nombre,foto,audio);
            int cont =pictoGridview.size() - 1;
            db.close();
            pictoGridview.remove(cont);
            pictoGridview.add(new Pictograma(null,nombre,foto,null,null));
            pictoGridview.add(new Pictograma(null,"Nuevo",R.drawable.icon_add,null,null));
            gridAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void modalCreatePictogram(final int positionx, final Boolean modoeditar, final String nombrex) {
        MicrophonePermissionGranted();
        extraimg=0;
        //sedOrientation(ActivityInfo.SCREEN_ORIENTATION_
        // LOCKED);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.agregar, (ViewGroup) findViewById(R.id.contenedoragregar));
        builder.setView(Layout);
        Button cancelar_item = Layout.findViewById(R.id.cancelitem);
        Button button_crear_categoria = Layout.findViewById(R.id.butoncrearitems);
        // Add on
        cancelar_item.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Loguear...
                        pictogramashow.dismiss();

                    }
                }

        );
        setimg = Layout.findViewById(R.id.setimage);
        if (modoeditar) {
            tenerphoto = true;
            final String[] args = new String[] {
                    nombrex
            };
            db=mDbHelper.getWritableDatabase();
            Cursor c = db.rawQuery(" SELECT foto,base FROM item  WHERE nombre=?", args);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String foto6 = c.getString(0);
                    int foto5 = c.getInt(0);
                    //buscar pictograma y mostrarlo en el editor
                    if (isnumeric(foto6)) {
                        fpicto = foto5;
                        Glide.with(this).load(foto5).apply(new RequestOptions().fitCenter()).into(setimg);
                    }
                  else  {
                      fotos = new File(foto6);
                        if (fotos.exists()) {
                            Glide.with(this).load(foto6).apply(new RequestOptions().fitCenter()).into(setimg);
                            Uri urix = Uri.parse(foto6);
                            fpicto=foto6;
                            saveimag = urix;

                        } else {
                            Glide.with(this).load(R.drawable.caution).apply(new RequestOptions().centerCrop()).into(setimg);
                            extraimg = R.drawable.caution;
                        }
                    }
                    EditText placenom = Layout.findViewById(R.id.textobjet);
                    placenom.setText(nombrex);
                }
                while (c.moveToNext());
            }
            c.close();
        }
        // Add onClickListener to exit_button

        button_crear_categoria.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      accionError="Toca para crear  pictograma";
                        texto1 = Layout.findViewById(R.id.textobjet);
                        String nombreobtenido = texto1.getText().toString();

                        if (nombreobtenido.isEmpty()) {
                            Toasty.warning(getBaseContext(), "Falta ingresar nombre", Toast.LENGTH_SHORT, true).show();
                        } else {

                            if (teneraudio && tenerphoto && !modoeditar || teneraudio && modoeditar) {
                                int compare = 0;
                                String nombrenuevo = nombreobtenido.replace("'", "!");
                                Log.i("nombre", nombrenuevo);
                                if (!modoeditar) {

                                    String[] args = new String[] {
                                            nombrenuevo
                                    };
                                   db=  mDbHelper.getReadableDatabase();
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
                                    Toasty.warning(getBaseContext(), "Ya existe el Pictograma", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    String nombrecateg = selecionadacategoria;
                                    //pic33 foto y audio y texto estan llenos se procede a crear carpeta en caso de que no existe
                                    File folder = new File(rutafassycom + "Categories" + File.separator + nombrecateg);
                                    boolean success;
                                    if (!folder.exists()) {
                                        //pic33 la carpeta no existe se crea
                                        success = folder.mkdirs();
                                        File nomediaFile = new File(folder, ".nomedia");
                                        try {
                                            nomediaFile.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        success = true;
                                    }
                                    if (success || folder.exists()) {
                                        File tempFolder = new File(MEDIA_DIRECTORYTEMP);
                                        if (!tempFolder.exists()) {
                                            tempFolder.mkdirs();
                                        }
                                        if (saveimag != null) {
                                            saveFile(saveimag);
                                        }
                                        //archivos en Temporarios
                                        Log.i("ruta1", tempFolder + "/temp.jpg");

                                        File file1 = new File(tempFolder + "/temp.ogg");
                                        File file2 = new File(tempFolder + "/temp.jpg");
                                        if(!imagenlocal){  if(file2.exists()){
                                            File tofile2new = new File(rutafassycom + "Categories" + File.separator + nombrecateg + "/" + nombrenuevo+System.currentTimeMillis()+".jpg");
                                            file2.renameTo(tofile2new);
                                            fpicto=tofile2new;
                                        }
                                        }
                                        //Destino a su carpeta
                                        File tofile1new = new File(rutafassycom + "Categories" + File.separator + nombrecateg + "/" + nombrenuevo + ".ogg");

                                        if (file1.renameTo(tofile1new)) {
                                        } else {
                                            Toasty.error(getBaseContext(), "Error al mover los archivos : " + nombrex, Toast.LENGTH_SHORT, true).show();
                                        }
                                        String fileaudio = String.valueOf(tofile1new);

                                        if(imagenlocal){
                                            Log.i("fotopicto", String.valueOf(fpicto));
                                            fpicto=extraimg;
                                        }
                                            if (!modoeditar) {
                                              accionError="Creando pictograma";
                                                addItem(nombrenuevo, fpicto, fileaudio);
                                                Toasty.success(getBaseContext(), "Creado Pictograma: " + nombrenuevo, Toast.LENGTH_SHORT, true).show();
                                                teneraudio = false;
                                                tenerphoto = false;
                                                pictogramashow.dismiss();
                                            } else {  accionError="actualizando pictograma";
                                                    pictoGridview.get(positionx).setNombrepic(nombrenuevo).setFoto(fpicto).setAudio(fileaudio);
                                                    db.execSQL("UPDATE item  SET nombre='" + nombrenuevo + "',foto='" + fpicto+ "',audio='" + fileaudio + "' WHERE nombre='" + nombrex + "' ");
                                                File itsremove = new File(String.valueOf(fotos));
                                                Log.i("delete", String.valueOf(fotos));
                                                if(itsremove.exists()){
                                                    if( itsremove.delete()){
                                                        fotos=null;
                                                    }
                                                }
                                                    Toasty.success(getBaseContext(), "Editado Pictograma: " + nombrex, Toast.LENGTH_SHORT, true).show();
                                                    teneraudio = false;
                                                    tenerphoto = false;

                                                    //listaitemborrar.removeAllViews();
                                                if (saveimag != null) {
                                                    Log.i("distinto saveimg pic", String.valueOf(saveimag));
                                                    saveimag = null;
                                                }
                                                gridAdapter.notifyDataSetChanged();
                                                    editarclear.dismiss();
                                                    pictogramashow.cancel();
                                            }
                                            imagenlocal=false;
                                        extraimg=0;
                                    } else {
                                        Toasty.error(getBaseContext(), "Error no se creo la carpeta de categoria", Toast.LENGTH_SHORT, true).show();
                                    }
                                }

                            } else {
                                if (!tenerphoto) {
                                    Toasty.warning(getBaseContext(), "Falta Imagen", Toast.LENGTH_SHORT, true).show();
                                }
                                if (!teneraudio) {
                                    Toasty.warning(getBaseContext(), "Falta Grabación", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }
                    }
                });

        pictogramashow = builder.create();
        pictogramashow.show();
        pictogramashow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // do something
                // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        });

        //boton audio
        final Button boton =Layout.findViewById(R.id.buttonrecord);

        boton.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
              accionError="Toca para grabar pictograma";
                boton.performClick();
                int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        //Cuando el usuario toca el boton de grabar
                            accionError="Aprieta grabando";
                            createFolder(MEDIA_DIRECTORYTEMP);
                            getApplicationContext();
                            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            if (vib != null) {
                                vib.vibrate(100);
                            }

                                final MediaPlayer sond = MediaPlayer.create(MainActivity.this, R.raw.record);
                                sond.start();
                                int audioEncoder;
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    // versiones con android 6.0 o superior
                                    audioEncoder = MediaRecorder.AudioEncoder.HE_AAC;
                                } else { // para versiones anteriores a android 6.0
                                    audioEncoder = MediaRecorder.AudioEncoder.AAC;
                                }
                                mRecorder = new MediaRecorder();
                                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                mRecorder.setAudioEncoder(audioEncoder);
                                mRecorder.setAudioChannels(1);
                                mRecorder.setAudioEncodingBitRate(128000);
                                mRecorder.setAudioSamplingRate(44100);
                                // Record to the external cache directory for visibility
                                mRecorder.setOutputFile(mFileName);
                                try {
                                    mRecorder.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                sond.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                    public void onCompletion(MediaPlayer arg0) {
                                        // cuando se termina de reproducir el sonidp de inicio de grabacion activar finishsound;
                                        startRecord = true;
                                        // totalTiempo = System.currentTimeMillis() - tiempoInicio;
                                        if (!cancelrecord) {
                                            if (mRecorder != null) {
                                                try {
                                                    mRecorder.start();
                                                    Toasty.info(getBaseContext(), "Grabando", Toast.LENGTH_SHORT, true).show();
                                                    boton.setBackgroundResource(R.drawable.save_microphone);
                                                    sond.release();
                                                } catch (Exception error) {
                                                    Toasty.error(getBaseContext(), "Error no se puede grabar ", Toast.LENGTH_SHORT, true).show();
                                                }
                                            }
                                        }
                                    }
                                });
                                break;


                    case (MotionEvent.ACTION_UP):
                      accionError="Suelta  grabando";
                        if (!startRecord) {
                            // si suelta el boton y se cancelo reproducir sonido de canselacion;;
                            boton.setBackgroundResource(R.drawable.save_microphone);

                            cancelrecord = true;
                            final MediaPlayer sond2 = MediaPlayer.create(MainActivity.this, R.raw.cancelrecord);
                            sond2.start();
                            sond2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    sond2.release();
                                    cancelrecord =false;
                                    startRecord = false;
                                    boton.setBackgroundResource(R.drawable.fsc_microphone);
                                }
                            });
                            Toasty.warning(getBaseContext(), "Manten presionado para grabar", Toast.LENGTH_SHORT, true).show();
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

                        if (startRecord && !cancelrecord) {
                            // si termina el sonido de grabacion exitosa se reproduce la grabacion del pictograma
                            startRecord = false;
                            Toasty.success(getBaseContext(), "Grabado con éxito", Toast.LENGTH_SHORT, true).show();
                            final MediaPlayer sond3 = MediaPlayer.create(MainActivity.this, R.raw.finalrecordsound);
                            teneraudio = true;
                            sond3.start();
                            boton.setBackgroundResource(R.drawable.save_save);
                            sond3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    playSound(mFileName);
                                    mp.stop();
                                    mp.release();
                                }

                            });

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
                        }

                        break;
                }
                return true;
            }
        });
    }




    void playSound(String dir) {
        File f = new File(dir);
        if (f.exists()) {
            Log.i("entro", "sonido");

            Uri myUri = Uri.fromFile(f); // initialize Uri here
            final MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {Log.i("audiosound",myUri.toString());
                mediaPlayer.setDataSource(getApplicationContext(), myUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });


        } else {
            Toasty.error(getBaseContext(), "Error No se encuentra la Grabación ", Toast.LENGTH_SHORT, true).show();
            File ruta = new File(MEDIA_DIRECTORYTEMP);
            if (!ruta.exists()) {
                Toasty.error(getBaseContext(), "Carpeta Temp no existe", Toast.LENGTH_SHORT, true).show();
            }
        }

    }
    public void btnDelete(View view) {
      accionError="toca para Borra lista de reproducion";
      // cuando se toca ell boton de play , vaciar las variables y actualizar adaptadores de reproducion
        if (mp != null) {
            if (!pictoPlay.isEmpty()) {
                try {
                    if (mp.isPlaying()) {

                        mp.stop();
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            }
        }
        pasos(4);
        currentposition = 0;
        pictoPlay.clear();
        mAdapterPlay.notifyDataSetChanged();
        LinearLayout hh=findViewById(6500+currentposition);
        if(hh!=null){
            hh.setBackgroundColor(Color.TRANSPARENT);
        }
        uris.clear();
        quest = 0;
        playauto=0;
        Button sendfass = findViewById(R.id.sendfass);
        sendfass.setVisibility(View.GONE);

    }




    public void btnQuestion(View view) {
      accionError="Toca para hacer pregunta ";
        quest++;
        pasos(6);
        // cuando apreta el boton de pregunat se agrega el pictograma pregunta y se actualiza adaptador de reproducion
        pictoPlay.add(new Pictograma(null,"Pregunta",R.drawable.question,R.raw.unapregunta,"local"));
        mAdapterPlay.notifyDataSetChanged();
        playauto++;
        autoPlaySound();

    }

    public void btnPlay(View view) {
      accionError="Toca para reproducir";
        pasos(3);
        mRecyclePlay.scrollToPosition(0);
        playSound();
    }

    @SuppressLint("ResourceAsColor")
    public void playSound() {
        if(currentposition>pictoPlay.size()){
            currentposition=0;
        }
        if (!pictoPlay.isEmpty()) {
                pausa = false;
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                mp = new MediaPlayer();
            int positionold=currentposition-1;

            if(currentposition>0 && currentposition != positionx){

                Log.i("currentconditionhold",""+6500+positionold);
                LinearLayout hh=findViewById(6500+positionold);
              if(hh!=null){
                  hh.setBackgroundColor(Color.TRANSPARENT);
              }
            }
            if(currentposition<pictoPlay.size()){


                LinearLayout hx=findViewById(6500+currentposition);
                if(hx!=null){
                    hx.setBackgroundColor(Color.LTGRAY);
                }else{Log.i("error",""+6500+currentposition);

                }

            }
            Log.i("sonidox",""+currentposition);
            Log.i("sonidoxsize",""+pictoPlay.size());
                if (isnumeric(pictoPlay.get(currentposition).audio.toString())) {

                    int ogg =Integer.parseInt(pictoPlay.get(currentposition).audio.toString());

                    mp = MediaPlayer.create(this, ogg);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // mp.setLooping(false);
                    if (mp != null) {
                        mp.start();
                    }
                }else{
                    File sound = new File(String.valueOf(pictoPlay.get(currentposition).audio));
                    if (sound.exists()) {
                        try {
                            mp.setDataSource(String.valueOf(pictoPlay.get(currentposition).audio));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mp.prepareAsync();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                if (mp != null) {
                                    mp.start();
                                }
                            }
                        });

                    }


                }


                // Setup listener so next song starts automatically
            if (mp != null) {
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer arg0) {
                        mp.release();
                        currentposition++;
                        nextPlaySound();
                    }

                });
            }


        }

    }

    public void autoPlaySound() {
        if(modoplay){
          accionError="Auto reproduciendo";
            if (pictoPlay != null ) {
                pausa = false;
                mp = new MediaPlayer();
                int number= playauto-1;
                mRecyclePlay.scrollToPosition(number);
                    if(isnumeric(pictoPlay.get(number).audio.toString()))
                    {
                    Integer ogg = Integer.parseInt(pictoPlay.get(number).audio.toString());
                    mp = MediaPlayer.create(this, ogg);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // mp.setLooping(false);
                    if (mp != null) {
                        mp.start();

                    }
                }else{
                    File sound = new File(pictoPlay.get(number).audio.toString());
                    if (sound.exists()) {
                        try {
                            mp.setDataSource(pictoPlay.get(number).audio.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mp.prepareAsync();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                if (mp != null) {
                                    Log.i("entro", "pic33");
                                    mp.start();
                                }
                            }
                        });

                    }}
            }
        }


    } int positionx;
    boolean finisnexplay=false;
    public void nextPlaySound() {
         positionx = pictoPlay.size();
        if (currentposition == positionx) {
            currentposition = 0;
            mp = null;
            Log.i("currentconditiondelete",""+6500+positionx);
            LinearLayout hz=findViewById(6500+positionx-1);
            hz.setBackgroundColor(Color.TRANSPARENT);
            mRecyclePlay.smoothScrollToPosition(currentposition);
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            mRecyclePlay.scrollTo(0,0);
            finisnexplay=true;
        } else {
            if (!pausa) {
              //  mRecyclePlay.scrollTo(dopx,0);
                mRecyclePlay.smoothScrollToPosition(currentposition+1);
                finisnexplay=false;
                playSound();

            //    hScrollplay.smoothScrollBy(dopx, 0);
            }
        }

    }

    public void btnPause(View view) {
      accionError="Toca para pausar ";
        pausa = true;
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            }
        }

    }
    int pasox =1;
    public void pasos(int lastpaso) {
        db = mDbHelper.getWritableDatabase();
        Cursor cl = db.rawQuery(" SELECT pasos FROM usuario", null);

        if (cl.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                pasox = cl.getInt(0);
            } while (cl.moveToNext());
        }

        cl.close();
        Log.i("paso", String.valueOf(pasox));
        Log.i("lastpaso", String.valueOf(lastpaso));
        if (pasox == lastpaso) {
            Log.i("pasoentro", "");
          pasox++;
            db.execSQL("UPDATE usuario SET pasos='" + pasox+ "'");
            db.close();

           for(int x=1;x<9;x++){
               if(lastpaso==x){
                   if(lastpaso==4){
                       mRecycleView.smoothScrollToPosition(0);
                   }
                   tutox[x].setVisibility(View.GONE);
                   if(lastpaso<7){
                       int z=x+1;
                       tutox[z].setVisibility(View.VISIBLE);
                   }

               }
            }
        }
    }


    public Dialog splashModal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.splash, (ViewGroup) findViewById(R.id.infosplash));
        builder.setView(Layout);
        Dialog splash = builder.create();
        splash.show();
        return splash;
    }
    boolean cargar=false;
    public ArrayList < Integer > lettersIcons = new ArrayList < > ();
    public void imagesExtra(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View Layout = inflater.inflate(R.layout.mas_foto, (ViewGroup) findViewById(R.id.contenedormas));
        builder.setView(Layout);
        if(!cargar){
            for (int x = 1; x <= 72; x++) {
                String imageName = "img" + x;
                int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                lettersIcons.add(resID);
            }
            cargar=true;
        }
        ExtraAdapter gridAdapters = new ExtraAdapter(getApplicationContext(), lettersIcons);
        GridView gridextra=Layout.findViewById(R.id.gridviewextra);
        gridextra.setAdapter(gridAdapters);
        gridextra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > parent, View view, int position, final long id) {
              accionError="Toca para elegir imagen extra";
                String source = (Long.valueOf(id)).toString();
                int number = Integer.parseInt(source) + 1;
                String obtenernom = "img" + number;
                extraimg = getResources().getIdentifier(obtenernom, "drawable", getPackageName());
                imagenlocal=true;
                tenerphoto = true;
                mas_foto.dismiss();
                elegirfoto.dismiss();
                Glide.with(getApplicationContext()).load(extraimg).apply(new RequestOptions().fitCenter().signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))).into(setimg);
            }
        });
        mas_foto = builder.create();
        mas_foto.show();
    }



    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        Log.d(TAG, "called for " + ex.getClass());
        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        String LINE_SEPARATOR = "\n";
        String errorReport =
                "\n************ Date OF ERROR ************\n" +
                        LINE_SEPARATOR +
                        "Fecha: " + giveDate() +
                        LINE_SEPARATOR + "Hora:" + givehora() +
                        "\n************ Causa de error ************\n" +
                        stackTrace.toString() +
                        "\n************ DEVICE INFORMATION ***********\n" +
                        "Brand: " +
                        Build.BRAND +
                        LINE_SEPARATOR +
                        "Device: " +
                        Build.DEVICE +
                        LINE_SEPARATOR +
                        "Model: " +
                        Build.MODEL +
                        LINE_SEPARATOR +
                        "Id: " +
                        Build.ID +
                        LINE_SEPARATOR +
                        "Product: " +
                        Build.PRODUCT +
                        LINE_SEPARATOR +
                        "\n************ FIRMWARE ************\n" +
                        "SDK: " +
                        Build.VERSION.SDK +
                        LINE_SEPARATOR +
                        "Android : " +
                        Build.VERSION.RELEASE +
                        LINE_SEPARATOR +
                        "Version APP: " +
                        BuildConfig.VERSION_NAME+
                        LINE_SEPARATOR +
                        "Accion: " +
                        accionError;
        Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("error", errorReport);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);

    }
    public String giveDate() {
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        return thisDate;
    }
    public String givehora() {
        SimpleDateFormat currentDate = new SimpleDateFormat("h:mm a");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        return thisDate;
    }
    public static Boolean isnumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }


    }
    public boolean checkedoldfassycom(SQLiteDatabase db) {
        boolean createdata = false;
        File rutaold = new File(rutavieja + "/Categories");
        File checkinstaall = new File(rutafassycom + "/install");
        if (!checkinstaall.exists()) {
            Log.d("listo para", "agregar");
        }
        if (rutaold.exists() && !checkinstaall.exists()) {

            Log.d("fassycomold", "existe");
            Log.d("Files", "Path: " + rutaold);

            File[] lista_Archivos = rutaold.listFiles();
            Log.d("Files", "Size: " + lista_Archivos.length);
            if (lista_Archivos != null) {
                if (lista_Archivos.length >= 1) {
                    for (int i = 0; i < lista_Archivos.length; i++) {

                        if (lista_Archivos[i].isDirectory() && !lista_Archivos[i].isHidden()) {

                            File directoryold = new File(rutavieja + "/Categories/" + lista_Archivos[i].getName());
                            if (directoryold.exists()) {
                                File[] files = directoryold.listFiles();
                                if (files != null) {
                                    if (files.length == 1) {
                                        Log.d("fassycomold", "vaciofiles");
                                    } else {
                                        if (!createdata) {
                                            createdata = true;
                                            int img = R.drawable.backup;
                                            Log.d("fassycomold", "se creao recuperado");
                                            db.execSQL("INSERT INTO categorias (nombre,photo,base) " +
                                                    "VALUES ('Recuperado' ,'" + img + "', 'local')");
                                            Toasty.info(getApplicationContext(), "Archivos recuperados reinicia fassycom", Toast.LENGTH_SHORT, true).show();
                                        }
                                        File carpetadestino = new File(rutafassycom + "/Categories/Recuperado");
                                        if (!carpetadestino.exists()) {
                                            carpetadestino.mkdirs();
                                        }
                                        for (int x = 0; x < files.length; x++) {
                                            if (files[x].getName().endsWith(".jpg")) {
                                                String archivox = files[x].getName().replace(".jpg", "");
                                                String fileorigen = directoryold + File.separator + archivox;
                                                String filedestino = rutafassycom + "Categories" + File.separator + "Recuperado" + File.separator + archivox;
                                                FileCopy(fileorigen + ".jpg", filedestino + ".jpg");
                                                FileCopy(fileorigen + ".ogg", filedestino + ".ogg");
                                                Log.i("datafileo", fileorigen);
                                                Log.i("datafiled", filedestino);
                                                db.execSQL("INSERT INTO item (categ,nombre,foto,audio,base,uso) " +
                                                        "VALUES ('Recuperado','" + archivox + "' , '" + filedestino + ".jpg" + "','" + filedestino + ".ogg" + "','sdcard','0')");
                                            }
                                        }

                                    }
                                }


                            }
                        }
                    }
                }
            } else {
                Log.i("rutaold", "datosvacios");
            }


        }
        return createdata;
    }
    public int Getdpi() {
        int densidad = 72;
        double density = getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            densidad = 216;

        } else if (density >= 3.0 && density < 4.0) {
            densidad = 192;
        } else if (density >= 2.0) {
            densidad = 144;
        } else if (density >= 1.5 && density < 2.0) {
            densidad = 96;
        } else if (density >= 1.0 && density < 1.5) {
            densidad = 72;
        }
        return densidad;
    }
    public void FileCopy(String sourceFile, String destinationFile) {
        System.out.println("Desde: " + sourceFile);
        System.out.println("Hacia: " + destinationFile);

        try {
            File inFile = new File(sourceFile);
            File outFile = new File(destinationFile);

            FileInputStream in = new FileInputStream(inFile);
            FileOutputStream out = new FileOutputStream(outFile);

            int c;
            while ((c = in .read()) != -1)
                out.write(c);

            in .close();
            out.close();
        } catch (IOException e) {
            System.err.println("Hubo un error de entrada/salida!!!");
        }
    }}