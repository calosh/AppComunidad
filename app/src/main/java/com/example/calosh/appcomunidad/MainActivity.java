package com.example.calosh.appcomunidad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.calosh.appcomunidad.clases.User;
import com.example.calosh.appcomunidad.clases.UserLocalStore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    UserLocalStore userLocalStore;
    Button bLogout, btnDenuncia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userLocalStore=new UserLocalStore(this);

        bLogout=(Button)findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);


        btnDenuncia=(Button)findViewById(R.id.btnDenuncia);
        btnDenuncia.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authenticate()==true){
            diaplayUserDetails();
        }else {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }
    private void diaplayUserDetails(){
        User user=userLocalStore.getLoggedInUser();
        //etNombre.setText(user.nombre);
        System.out.println(user.getFirst_name());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;

            case R.id.btnDenuncia:
                startActivity(new Intent(this, Denuncia.class));
                break;
        }
    }
}
