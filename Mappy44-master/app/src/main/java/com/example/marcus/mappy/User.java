package com.example.marcus.mappy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marcus on 2016-01-12.
 */
public class User implements Serializable {

    private String name;
    private double userLat, userLon;
    private ArrayList<User> pals;
    private MeetUpPoint target;

    public User(String name){
        this.name = name;
        pals = new ArrayList<User>();
    }

    public void setLocation(double userLat, double userLon){
        this.userLat = userLat;
        this.userLon = userLon;
    }

    public double getLat(){
        return userLat;
    }

    public double getLon(){
        return userLon;
    }

    public void addFriend(User friend){
        if(!pals.contains(friend))
            pals.add(friend);
    }

    public String getName(){
        return name;
    }

    public void removeFriend(User friend){
        pals.remove(friend);
    }

    public ArrayList<User> getFriends(){
        return pals;
    }

    public void setFriends(ArrayList<User> newFriendsInfo){
        pals = newFriendsInfo;
    }

    public void setSelectedPoint(MeetUpPoint target){
        this.target = target;
    }

    public void modifyPointName(String str){
        target.setName(str);
    }

    public MeetUpPoint getSelectedPoint(){
        return target;
    }

}
