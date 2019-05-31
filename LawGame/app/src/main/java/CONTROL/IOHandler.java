package CONTROL;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import MODEL.Save;

public class IOHandler {
    private static final String currentgameFile = "mysave1.LAW";
    private static final String autogameFile = "myautosave.LAW";
    private Context context;

    public IOHandler(Context context) {
        this.context = context;
    }

    public void saveGame(Save save) {
        if(save == null)
            return;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(currentgameFile, context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(save);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Save loadGame() {
        File file = context.getFileStreamPath(currentgameFile);
        Save model = null;
        if (!file.exists())
            return null;
        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = context.openFileInput(currentgameFile);
            ois = new ObjectInputStream(fos);
            model = ((Save) ois.readObject());

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

    public Save loadAutoGame() {
        File file = context.getFileStreamPath(autogameFile);
        Save model = null;
        if (!file.exists())
            return null;
        FileInputStream fos = null;
        ObjectInputStream ois = null;

        try {
            fos = context.openFileInput(autogameFile);
            ois = new ObjectInputStream(fos);
            model = ((Save) ois.readObject());

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

    public ArrayList<String> loadEpisode(int episode){
        ArrayList<String> story = new ArrayList<String>();

        AssetManager am = context.getAssets();

        try {
            InputStream is = am.open("episode" + Integer.toString(episode) + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null)
                story.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return story;
    }

}
