package com.example.marcus.mappy;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Marcus on 2016-01-12.
 */
public class FileManager {

    protected void fileSaver(Context mContext, Model mModel){

        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput("userr.LOL", mContext.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(mModel.getUser());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected  boolean checkFileExist(Context mContext){
        File file = mContext.getFileStreamPath("userr.LOL");
        if(!file.exists())
            return false;

        return true;
    }

    protected boolean loadData(Context mContext, Model mModel) throws Exception{

        File file = mContext.getFileStreamPath("userr.LOL");
        if(!file.exists())
            return false;

        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = mContext.openFileInput("userr.LOL");
            ois = new ObjectInputStream(fos);
            User tmpUser = (User) ois.readObject();
            ois.close();
            fos.close();
            mModel.setUser(tmpUser);
        }
        finally {
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

        return true;
    }

}
