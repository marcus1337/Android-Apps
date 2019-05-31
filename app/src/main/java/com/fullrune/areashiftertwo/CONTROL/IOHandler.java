package com.fullrune.areashiftertwo.CONTROL;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.fullrune.areashiftertwo.MODEL.SAVING.MiscInfo;
import com.fullrune.areashiftertwo.MODEL.SAVING.Progress;
import com.fullrune.areashiftertwo.MODEL.SAVING.Save;

public class IOHandler {
    private static final String gameFile1 = "game_save_file_area2";
    private static final String gameInfoFile1 = "mygameinfo1.areshift";
    private static final String gameInfoFile2 = "mygameinfo2.areshift";
    private static final String gameFile2 = "mygamedata2.areshift";
    private static final String progressFile = "prog_save_file_area22";
    private static final String miscInfoFile = "misc_save_file_area22";

    private Context context;

    public IOHandler(Context context) {
        this.context = context;
    }

    public void saveMiscInfo(MiscInfo miscInfo){
        saveObject(miscInfo, miscInfoFile);
    }

    public MiscInfo loadMiscInfo(){
        return (MiscInfo) loadObject(miscInfoFile);
    }

    public void saveProgress( Progress progress){
        saveObject(progress, progressFile);
    }

    public Progress loadProgress(){
        return (Progress) loadObject(progressFile);
    }

    public Save loadSave(){
        return (Save) loadObject(gameFile1);
    }

    public void saveGame(Save save) {
       saveObject(save, gameFile1);
    }

    public Object loadObject(String locationOfFile) {
        File file = context.getFileStreamPath(locationOfFile);
        Object filedata = null;
        if (!file.exists())
            return null;
        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = context.openFileInput(locationOfFile);
            ois = new ObjectInputStream(fos);
            filedata = (ois.readObject());

            ois.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null)
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
        return filedata;
    }

    private void saveObject(Object object, String locationOfFile){
        if(object == null)
            return;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(locationOfFile, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(object);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
