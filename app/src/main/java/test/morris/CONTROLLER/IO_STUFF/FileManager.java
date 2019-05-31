package test.morris.CONTROLLER.IO_STUFF;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import test.morris.MODELLEN.GameState;

/**
 * Created by Marcus on 2015-11-21.
 */
public class FileManager {

    private static final String fileName = "theData198.LOL";
    private Context context;

    public FileManager(Context context){
        this.context = context;
    }

    //save date and model separately
    public void fileSaver(GameState storage){
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
    public GameState loadData(){
        File file = context.getFileStreamPath(fileName);
        GameState storage = null;
        if(!file.exists())
            return null;
        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = context.openFileInput(fileName);
            ois = new ObjectInputStream(fos);
            storage = ((GameState) ois.readObject());

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