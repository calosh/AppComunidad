package com.example.calosh.appcomunidad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calosh.appcomunidad.clases.User;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    User user;
    EditText etNombre, etApellido, etUsername, etCorreo, etPassword;
    Button bRegistro;


    String id_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //INicializar variables
        etNombre=(EditText)findViewById(R.id.etNombre);
        etApellido=(EditText)findViewById(R.id.etApellido);
        etUsername=(EditText)findViewById(R.id.etUsername);
        etCorreo=(EditText)findViewById(R.id.etCorreo);
        etPassword=(EditText)findViewById(R.id.etPassword);

    }

    public void btn_clickGuardarUser(View view){
        user = new User();
        user.setUsername(etUsername.getText().toString().trim());
        user.setFirst_name(etNombre.getText().toString().trim());
        user.setLast_name(etApellido.getText().toString().trim());
        user.setEmail(etCorreo.getText().toString().trim());
        user.setPassword(etPassword.getText().toString().trim());

        new InsertarUser().execute();


    }

    private class InsertarUser extends AsyncTask<Void, Void, Boolean>{
        public Boolean doInBackground(Void... params){
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://192.168.0.106:8000/rest/ususarios/");
            httpPost.setHeader("Content-Type", "application/json");

            JSONObject jsonObject=new JSONObject();

            try{
                jsonObject.put("username", user.getUsername());
                jsonObject.put("first_name", user.getFirst_name());
                jsonObject.put("last_name", user.getLast_name());
                jsonObject.put("email", user.getEmail());
                jsonObject.put("password", user.getPassword());

                StringEntity stringEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                httpClient.execute(httpPost);

                startActivity(new Intent(Register.this, Login.class));

                return true;


            }catch (org.json.JSONException e){
                return false;
            }catch (java.io.UnsupportedEncodingException e){
                return false;
            }catch (org.apache.http.client.ClientProtocolException e){
                return false;
            }catch (java.io.IOException e){
                return false;
            }

        }

        public void onPostExecute(Boolean result){
            if(result){
                Toast.makeText(Register.this, "Registro Correcto", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(Register.this, "Problemas al Registrarse", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ActualizarPersona extends AsyncTask<Void, Void, Boolean>{
        public Boolean doInBackground(Void... params){
            HttpClient httpClient=new DefaultHttpClient();
            HttpPut httpPut=new HttpPut("http://192.168.2.2:8000/rest/ususarios/"+id_user+"/");
            httpPut.setHeader("Content-Type", "application/json");

            JSONObject jsonObject=new JSONObject();
            try{
                jsonObject.put("first_name", user.getFirst_name());
                jsonObject.put("last_name", user.getLast_name());

                StringEntity stringEntity=new StringEntity(jsonObject.toString());
                httpPut.setEntity(stringEntity);
                httpClient.execute(httpPut);
                return true;
            }catch (JSONException e){
                e.printStackTrace();
                return false;
            }catch (java.io.UnsupportedEncodingException e){
                return false;
            }catch (java.io.IOException e){
                return false;
            }
        }

        public void onPostExecute(Boolean result){
            String msj;
            if(result){
                msj = "Actualizado correctamente";
            }else{
                msj= "Problemas al actualizar";
            }
            Toast.makeText(Register.this, msj, Toast.LENGTH_SHORT).show();
        };
    }
}
