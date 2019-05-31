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


public class InternetManager extends AsyncTask<String, Void, User> {

    private static final String URL = "http://104.155.16.16:3306";

    private Model mModel;
    private ArrayList<User> friendList;
    private Connection con;
    private java.sql.Statement st;

    public InternetManager(Model mModel){
        this.mModel = mModel;
        friendList = mModel.getUser().getFriends();
    }

    public ArrayList<User> getFriendList(){
        return friendList;
    }

    private void getFriendLocation() throws SQLException {
        java.sql.ResultSet rs = st.executeQuery("select * from table");

        while (rs.next()) {
            String field = rs.getString("field");
            for(User u: friendList){
                if(u.getName().equals(field)){
                    u.setLocation(123, 123);
                }
            }
        }//while

        mModel.getUser().setFriends(friendList);
    }

    private void putMylocation() throws SQLException {

        String na = mModel.getUser().getName();
        String x = String.valueOf(mModel.getUser().getLat());
        String y = String.valueOf(mModel.getUser().getLon());

        st.executeQuery("set user" + na + "location" + x + y);


    }

    private void addFriend(String newFriendName) throws SQLException {
        String na = mModel.getUser().getName();
        st.executeQuery("set " + na + " " + newFriendName);


    }

    private void removeFriend(String deletedFriendName) throws SQLException {
        String na = mModel.getUser().getName();
        st.executeQuery("delete " + na + " " + deletedFriendName);

    }

    @Override
    protected User doInBackground(String... params) {

        String tmp = params[0];

        try {
            con = DriverManager.getConnection(URL, "user", "pass");
            st = con.createStatement();

            if (tmp.equals("getLocation")) {
                getFriendLocation();
            } else if (tmp.equals("sendLocation")) {
                putMylocation();
            } else if (tmp.endsWith("#")) {
                tmp = tmp.substring(0, tmp.length() - 1);
                addFriend(tmp);
            } else if (tmp.endsWith("¤")) {
                tmp = tmp.substring(0, tmp.length() - 1);
                removeFriend(tmp);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if(!st.isClosed())
                    st.close();
                if(!con.isClosed())
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
