package com.example.marcus.mappy;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marcus on 2016-01-12.
 */
public class Model {

    private Marker marker;
    private Marker userLocTmp;
    private Polyline line;
    private LatLng userLoc;
    private float[] results = new float[1];
    private User user;
    private Context context;

    public Model(Location l, Context context, String chosenName){
        userLoc = new LatLng(l.getLatitude(), l.getLongitude());
        this.context = context;
        if(!loadUserInfo()){ //no data to load or fail
            user = new User(chosenName);
        }

        user.addFriend(new User("test1"));
        user.addFriend(new User("test2"));
    }

    public Model(Location l, Context context) {
        userLoc = new LatLng(l.getLatitude(), l.getLongitude());
        this.context = context;
        loadUserInfo();
    }

    public void init(GoogleMap mMap){
        mMap.clear();
        userLocTmp = mMap.addMarker(new MarkerOptions().position(userLoc).title("your current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));
      // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(user.getLat(), user.getLon()), 14));
    }

    public void mapClicked(GoogleMap mMap, LatLng latLng){

        if(line != null){
            line.remove();
        }

        line = mMap.addPolyline(new PolylineOptions()
                .add( userLocTmp.getPosition(), latLng)
                .width(5)
                .color(Color.RED));

        Location.distanceBetween(userLocTmp.getPosition().latitude, userLocTmp.getPosition().longitude,
                latLng.latitude, latLng.longitude,
                results);

        if(marker != null){
            marker.remove(); //if remove these you can add many markers!
            marker = null;   //if remove these you can add many markers!
        }

        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("goal: " + getDistanceText()));
        user.setSelectedPoint(new MeetUpPoint(latLng.latitude, latLng.longitude));
        marker.showInfoWindow();
    }

    public void markerClicked(GoogleMap mMap, Marker tmpMark){ //change selected marker

        if(line != null){
            line.remove();
        }

        LatLng tmpLatLng = new LatLng(tmpMark.getPosition().latitude, tmpMark.getPosition().longitude);

        line = mMap.addPolyline(new PolylineOptions()
                .add(userLocTmp.getPosition(), tmpLatLng)
                .width(5)
                .color(Color.RED));

        Location.distanceBetween(userLocTmp.getPosition().latitude, userLocTmp.getPosition().longitude,
                tmpLatLng.latitude, tmpLatLng.longitude,
                results);

      //  user.setSelectedPoint(new MeetUpPoint(tmpLatLng.latitude, tmpLatLng.longitude));


    }

    public void updateMap(GoogleMap mMap, Location userPos){

        if(line != null){
            line.remove();
        }
        userLocTmp.remove();

        userLoc = new LatLng(userPos.getLatitude(), userPos.getLongitude());
        userLocTmp = mMap.addMarker(new MarkerOptions().position(userLoc).title("your current location"));

        if(user.getSelectedPoint() != null){

            double lat = user.getSelectedPoint().getLat();
            double lon = user.getSelectedPoint().getLon();
            LatLng targetPos = new LatLng(lat, lon);
            Location.distanceBetween(userLocTmp.getPosition().latitude, userLocTmp.getPosition().longitude,
                    targetPos.latitude, targetPos.longitude,
                    results);

            line = mMap.addPolyline(new PolylineOptions()
                    .add(userLocTmp.getPosition(), targetPos)
                    .width(5)
                    .color(Color.RED));

            if (marker == null)
                marker = mMap.addMarker(new MarkerOptions().position(targetPos));

            marker.setTitle("goal: " + getDistanceText());
            if(!marker.isInfoWindowShown())
                marker.showInfoWindow();

        }

        user.setLocation(userPos.getLatitude(), userPos.getLongitude());
    }

    public void setLocation(LatLng userLoc){
        this.userLoc = userLoc;
    }

    public LatLng getLocation(){
        return userLoc;
    }

    private String getDistanceText(){

        if(results[0] > 1000 && results[0] < 10000){
            float tmp = results[0] / 1000;
            return String.format("%.2f", tmp) + "Km";
        }
        else if(results[0] > 10000){
            float tmp = results[0] / 10000;
            return String.format("%.1f", tmp) + "Mil";
        }

        return Integer.toString((int) results[0]) + "M";
    }


    public void saveUserInfo(){
        FileManager man = new FileManager();
        man.fileSaver(context, this);
    }

    public boolean loadUserInfo(){
        FileManager man = new FileManager();
        try {
           return man.loadData(context, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void modifyMarkerDescription(String str){
        if(str.length() > 0)
            user.modifyPointName(str);
    }

    public String getMarkerInfo(Marker mar) {

        LatLng tmpLat = mar.getPosition();
        LatLng tmpUser = new LatLng(user.getSelectedPoint().getLat(), user.getSelectedPoint().getLon());

        if(tmpLat.equals(tmpUser)){
            return user.getSelectedPoint().getName();
        }

        return null;
    }

    public void updateFriends(ArrayList<User> newFriends){
        user.setFriends(newFriends);
    }

}
