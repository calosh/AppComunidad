package com.example.calosh.appcomunidad;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calosh.appcomunidad.clases.HttpRequest;
import com.example.calosh.appcomunidad.clases.User;
import com.example.calosh.appcomunidad.clases.UserLocalStore;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin, bRegister;
    EditText etUsername, etPassword;
    TextView tvRegistroLink;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegistroLink=(TextView)findViewById(R.id.tvRegistroLink);
        bRegister=(Button)findViewById(R.id.bRegister);

        bLogin=(Button)findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);

        tvRegistroLink.setOnClickListener(this);
        bRegister.setOnClickListener(this);

        etUsername=(EditText)findViewById(R.id.etUsername);
        etPassword=(EditText)findViewById(R.id.etPassword);

        userLocalStore=new UserLocalStore(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                new getUser().execute("http://192.168.2.2:8000/rest/ususarios/");
                System.out.println("ListooooooooooooC");
                break;

            case R.id.tvRegistroLink:
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.bRegister:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private class getUser extends AsyncTask<String, Void, String>{
        public String doInBackground(String... params){
            try{
                return HttpRequest.get(params[0]).accept("application/json").body();
            }catch (Exception e){
                return "";
            }
        }

        public void onPostExecute(String result){
            if (result.isEmpty()){
                Toast.makeText(Login.this, "No se entro usuario", Toast.LENGTH_LONG).show();
            }else {
                ArrayList<User> user= User.getUsers(result);

                ArrayList<User> user_aux=new ArrayList<>();

                for(int i=0; i<user.size();i++){

                    if (user.get(i).getUsername().equals(etUsername.getText().toString().trim()) &&
                            user.get(i).getPassword().equals(etPassword.getText().toString().trim())){
                        user_aux.add(user.get(i));
                    }
                }

                if(user_aux.size()!=0){
                    System.out.println("Aqui el usuario"+user_aux.get(0).getUsername());
                    logUserIn(user_aux.get(0));
                }
            }
        }
    }

    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, MainActivity.class));
    }
}
