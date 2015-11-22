package com.example.calosh.appcomunidad.clases;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by calosh on 11/1/15.
 */
public class UserLocalStore {
    public static final String SP_NAME="userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase=context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.putString("id", user.id);
        System.out.println(user.id+" ID:");
        System.out.println( user.username+" Username");
        spEditor.putString("first_name", user.getFirst_name());
        spEditor.putString("last_name", user.getLast_name());

        spEditor.putString("username", user.getUsername());
        spEditor.putString("password", user.getPassword());
        spEditor.putString("email", user.getEmail());

        spEditor.commit();

    }

    public User getLoggedInUser(){
        String id = userLocalDatabase.getString("id","");
        String nombre = userLocalDatabase.getString("first_name","");
        String apellido = userLocalDatabase.getString("last_name", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String correo = userLocalDatabase.getString("email", "");

        User storedUser=new User(id, nombre, apellido, username,password, correo);
        return  storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn", false)){
            return true;
        }else{
            return false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();

    }
}
