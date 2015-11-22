package com.example.calosh.appcomunidad;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import android.location.Criteria;

import com.example.calosh.appcomunidad.clases.GPSTracker;

public class Denuncia extends AppCompatActivity implements LocationListener {

    //Check Box
    private static RadioGroup radio_g;
    private static RadioButton radio_b;
    private CheckBox check1, check2, check3;

    //Foto
    private ImageButton camara;
    private ImageView imagen;
    private Button upload;
    private Uri output;
    private String foto;
    private File file;

    //GPS
    //http://www.jtech.ua.es/dadm/2011-2012/restringido/sensores/sesion02-apuntes.html
    LocationManager lm;
    String provider;
    Location l;

    double longitude = 0;
    double latitude = 0;

    //3
    Button btnShowLocation;

    GPSTracker gps;

    public String  nombreImagen="";
    public String result="";
    public String tipo="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_denuncia);



        camara=(ImageButton)findViewById(R.id.camara);
        camara.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //if(!nombreImagen.getText().toString().trim().equalsIgnoreCase("")){
                getCamara();
                //}else
                //  Toast.makeText(DenunciaActivity.this, "Debe nombrar el archivo primero",
                //          Toast.LENGTH_LONG).show();
            }

        });
        imagen=(ImageView)findViewById(R.id.imagen);

        upload=(Button)findViewById(R.id.button1);
        upload.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                serverUpdate();
            }

        });

        //GPS
        /**
         manager = (LocationManager)
         this.getSystemService(Context.LOCATION_SERVICE);

         proveedor = manager
         .getProvider(LocationManager.GPS_PROVIDER);

         posicion = manager
         .getLastKnownLocation(LocationManager.GPS_PROVIDER);
         if (posicion!=null){
         longitude = posicion.getLongitude();
         latitude = posicion.getLatitude();
         String locLat = String.valueOf(latitude)+","+String.valueOf(longitude);
         }else{
         System.out.println("Erroooooooosos");
         }

         */

        /*
        lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c=new Criteria();

        provider=lm.getBestProvider(c, true);
        l=lm.getLastKnownLocation(provider);
        if(l!=null)
        {
            longitude=(l.getLatitude());
            latitude=(l.getLongitude());
            System.out.println(l.getLongitude()+" CSCSCS");
        }else{
            Location   getLastLocation = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            longitude = getLastLocation.getLongitude();
            latitude = getLastLocation.getLatitude();
            System.out.println("Sigue sinedo null");
        }

        */


        /*
        btnShowLocation = (Button) findViewById(R.id.button1);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
        */


        gps = new GPSTracker(Denuncia.this);

        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Toast.makeText(
                    getApplicationContext(),
                    "Your Location is -\nLat: " + latitude + "\nLong: "
                            + longitude, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }

    }


    private void getCamara(){
        Random rnd=new Random();

        nombreImagen="imagen_"+rnd.nextInt()+""+rnd.nextInt();
        System.out.println("Nombre de la IMagen "+nombreImagen);

        foto = Environment.getExternalStorageDirectory() +"/"
                +nombreImagen+".jpg";
        System.out.println("La foto"+foto);
        file=new File(foto);
        System.out.println("ARchivo"+file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        output = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ContentResolver cr=this.getContentResolver();
        Bitmap bit = null;
        try {
            bit = android.provider.MediaStore.Images.Media.getBitmap(cr, output);

            //orientation
            int rotate = 0;
            try {
                ExifInterface exif = new ExifInterface(
                        file.getAbsolutePath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bit = Bitmap.createBitmap(bit , 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //imagen.setBackgroundResource(0);
        imagen.setImageBitmap(bit);
    }

    private void uploadFoto(String imag){
        // http://picarcodigo.blogspot.com/2014/05/webservice-subir-imagen-servidor-desde.html
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        //HttpPost httppost = new HttpPost("http://192.168.56.1/~calosh/app_baches/upload.php");
        HttpPost httppost = new HttpPost("http://192.168.2.2/~calosh/app_baches/upload.php");
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody foto = new FileBody(file, "image/jpeg");
        mpEntity.addPart("fotoUp", foto);
        httppost.setEntity(mpEntity);
        try {
            httpclient.execute(httppost);
            httpclient.getConnectionManager().shutdown();
            System.out.println("Dice q si");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("No se subio");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("No se subio 2");
        }
    }
    private boolean onInsert(){
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost("http://192.168.2.2/~calosh/app_baches/insertImagen.php"); // Url del Servidor
        //Añadimos nuestros datos
        //nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs = new ArrayList<NameValuePair>();



        //nameValuePairs.add(new BasicNameValuePair("imagen",nombreImagen.getText().toString().trim()+".jpg"));

        nameValuePairs.add(new BasicNameValuePair("latitud", latitude+""));
        nameValuePairs.add(new BasicNameValuePair("longuitud", longitude+""));

        //Obtengo mis preferencias guardadas
        // http://creandoandroid.es/como-crear-una-pantalla-de-login/
        SharedPreferences prefs = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String id = prefs.getString("id", "");

        nameValuePairs.add(new BasicNameValuePair("imagen", nombreImagen));
        nameValuePairs.add(new BasicNameValuePair("id", id));


        /*
        check1=(CheckBox)findViewById(R.id.checkBox1);
        check2=(CheckBox)findViewById(R.id.checkBox2);
        check3=(CheckBox)findViewById(R.id.checkBox3);

        if(check1.isChecked()==true){
            result=result+"-Congestionamiento Vehicular<br>";
        }
        if(check2.isChecked()==true){
            result=result+"-Dano en Vehiculos<br>";
        }
        if(check3.isChecked()==true){
            result=result+"-Accidentes<br>";

        }

        nameValuePairs.add(new BasicNameValuePair("problemas", result));
        */


        radio_g=(RadioGroup)findViewById(R.id.radioGroup);
        int selected_id = radio_g.getCheckedRadioButtonId();
        radio_b=(RadioButton)findViewById(selected_id);

        tipo=radio_b.getText().toString()+"";
        System.out.println("Tamanio"+tipo);
        nameValuePairs.add(new BasicNameValuePair("tipo", tipo));


        //nameValuePairs.add(new BasicNameValuePair("size", ));


        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
            return true;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private void serverUpdate(){
        if (file.exists())new ServerUpdate().execute();
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude=l.getLongitude();
        latitude=l.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class ServerUpdate extends AsyncTask<String,String,String>{

        ProgressDialog pDialog;
        @Override
        protected String doInBackground(String... arg0) {
            uploadFoto(foto);
            if(onInsert())
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(Denuncia.this, "Éxito al subir la imagen",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Denuncia.this, MainActivity.class));
                    }
                });
            else
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(Denuncia.this, "Sin éxito al subir la imagen",
                                Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Denuncia.this);
            pDialog.setMessage("Actualizando Servidor, espere..." );
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
        }
    }
}