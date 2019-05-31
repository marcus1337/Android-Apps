package com.fullrune.areashifter.CONTROLLER;

import android.content.Context;
import android.util.Log;

import com.fullrune.areashifter.CONTROLLER.FileObjects.HighScore;
import com.fullrune.areashifter.CONTROLLER.FileObjects.Purchases;
import com.fullrune.areashifter.MODEL.Model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Marcus on 2017-04-24.
 */

public class IOManager {

    private static final String currentgameFile = "currentgame24985.EL";
    private static final String highscoresFile = "highscores555.EL";
    private static final String purchasesFile = "purchases555.EL";
    private Context context;

    public IOManager(Context context) {
        this.context = context;
    }

    public Purchases loadPurchases(){
        File file = context.getFileStreamPath(purchasesFile);
        Purchases purchases = null;
        if (!file.exists())
            return null;
        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = context.openFileInput(purchasesFile);
            ois = new ObjectInputStream(fos);
            purchases = ((Purchases) ois.readObject());

            ois.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        return purchases;
    }

    public void savePurchases(Purchases purchases){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(purchasesFile, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(purchases);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHighScore(HighScore highScore){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(highscoresFile, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(highScore);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HighScore loadHighScore(){
        File file = context.getFileStreamPath(highscoresFile);
        HighScore highScore = null;
        if (!file.exists())
            return null;
        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = context.openFileInput(highscoresFile);
            ois = new ObjectInputStream(fos);
            highScore = ((HighScore) ois.readObject());

            ois.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        return highScore;
    }

    public void saveModel(Model model) {
        if(model == null)
            return;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(currentgameFile, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(model);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Model loadModel() {
        File file = context.getFileStreamPath(currentgameFile);
        Model model = null;
        if (!file.exists())
            return null;
        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = context.openFileInput(currentgameFile);
            ois = new ObjectInputStream(fos);
            model = ((Model) ois.readObject());

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
        return model;
    }

}
