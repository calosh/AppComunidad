package com.example.calosh.appcomunidad.clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by calosh on 10/30/15.
 */
public class User {
    String id;
    String username;
    String email;
    String first_name;
    String last_name;

    public User(String id, String username, String email, String first_name, String last_name, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
    }
    public User(String username, String password){
        this.username=username;
        this.password=password;
        this.email="";
        this.first_name="";
        this.last_name="";
    }

    public User(){
        this.username="";
        this.password="";
        this.email="";
        this.first_name="";
        this.last_name="";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String password;

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public static ArrayList<User> getUsers(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
