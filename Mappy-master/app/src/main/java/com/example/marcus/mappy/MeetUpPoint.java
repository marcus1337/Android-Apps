package com.example.marcus.mappy;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Marcus on 2016-01-12.
 */
public class MeetUpPoint implements Serializable {

    private boolean visible;
    private double pointLat, pointLon;
    private String pointName;
    private final String pointID;

    public MeetUpPoint(double pointLat, double pointLon){
        this.pointLat = pointLat;
        this.pointLon = pointLon;
        pointName = "description1337";
        visible = true;
        pointID = generateString();
    }

    public String getId(){
        return pointID;
    }

    public double getLat(){
        return pointLat;
    }

    public double getLon(){
        return pointLon;
    }

    public String getName(){
        return pointName;
    }

    public void setName(String pointName){
        this.pointName = pointName;
    }

    public void setVisibility(boolean b){
        visible = b;
    }

    //stackoverflow, modified
    private static String generateString()
    {
        Random rng = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        char[] text = new char[30];
        for (int i = 0; i < 30; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

}
