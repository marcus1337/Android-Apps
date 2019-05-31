package com.fullrune.halfrobot.MISC;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Marcus on 2017-09-21.
 */

public class IOManager {

    private static final String gameFileN = "savegamefile.SAVE";


    public static void save(Context context , SaveFile saveFile){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(gameFileN, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(saveFile);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public static SaveFile load(Context context){
        SaveFile saveFile = null;

        File file = context.getFileStreamPath(gameFileN);
        if (!file.exists())
            return null;
        FileInputStream fos;
        ObjectInputStream ois;

        try {
            fos = context.openFileInput(gameFileN);
            ois = new ObjectInputStream(fos);
            saveFile = ((SaveFile) ois.readObject());
            ois.close();
            fos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return saveFile;
    }

}
