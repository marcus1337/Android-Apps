package com.example.marcus.mappy;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class InternetManager extends AsyncTask<String, Void, User> {

    private static final String URL = "http://104.155.16.16:3306";

    private User friend;
    private double friendLatLoc;
    private User localUser;
    private ArrayList<User> friendList;

    public InternetManager(User localUser){
        this.localUser = localUser;
        friendList = new ArrayList<User>();
    }

    public ArrayList<User> getFriendList(){
        return friendList;
    }

    private static void getFriendLocation(){
        JSONObject json = null;
        try {
            json = readJsonFromUrl("http://www.javascriptkit.com/dhtmltutors/javascriptkit.json");
            Log.i("woop", json.toString());
            Log.i("woop2", (String) json.get("title"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        User obj = new User("Marcus");
        Gson gson = new Gson();
        String json22 = gson.toJson(obj);
    }

    private static void putMylocation(){
        User obj = new User("Marcus");
        Gson gson = new Gson();
        String json22 = gson.toJson(obj);
    }

    private static void addFriend(String newFriendName){

        String jsonMessage = null;
    }

    private static void removeFriend(String deletedFriendName){

        String jsonMessage = null;
    }

    /*funktion hämtad från stackoverflow*/
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /*funktion hämtad från stackoverflow*/
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }


    @Override
    protected User doInBackground(String... params) {

        String tmp = params[0];

        if (tmp.equals("getLocation")){
            getFriendLocation();
        }
        else if(tmp.equals("sendLocation")){
            putMylocation();
        }
        else if(tmp.endsWith("#")){
            tmp = tmp.substring(0, tmp.length()-1);
            addFriend(tmp);
        }
        else if(tmp.endsWith("¤")){
            tmp = tmp.substring(0, tmp.length()-1);
            removeFriend(tmp);
        }

        return null;
    }
}
