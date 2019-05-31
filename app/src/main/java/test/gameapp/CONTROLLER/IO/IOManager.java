package test.gameapp.CONTROLLER.IO;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import test.gameapp.MODEL.DATA.PlayerInfo;

/**
 * Created by Marcus on 2016-12-27.
 */


/****************************
// Spara spelarens status i form av guld och steg eller ladda denna dataa  från en fil
// Uppdatera spelarens status när uppgraderingar köps
**********************************/

public class IOManager {

    private static final String fileName = "mlgprodata.LOL";
    private Context context;

    public IOManager(Context context){
        this.context = context;
    }

    public void addSteps(long steps){
        try{
            PlayerInfo playerInfo = load();
            long currentGold = playerInfo.getGold();
            playerInfo.setGold(currentGold + steps);
            save(playerInfo);
        }catch(Exception e){

        }
    }

    public boolean buyWeapon(int price, int swordType){
        if(canAfford(price)){
            PlayerInfo tmp = load();
            long gold = tmp.getGold();
            tmp.setGold(gold - price);
            tmp.setSelectedSword(swordType);
            save(tmp);
            return true;
        }
        return false;
    }

    public long getGold(){
        return load().getGold();
    }

    public boolean canAfford(int price){
        PlayerInfo tmp = load();
        if(tmp.getGold() >= price)
            return true;
        return false;
    }

    //save date and model separately
    public void save(PlayerInfo storage){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(storage);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //load model from file
    public PlayerInfo load(){
        File file = context.getFileStreamPath(fileName);
        PlayerInfo storage = null;
        if(!file.exists())
            return null;
        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = context.openFileInput(fileName);
            ois = new ObjectInputStream(fos);
            storage = ((PlayerInfo) ois.readObject());

            ois.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return storage;
    }

}
