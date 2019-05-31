package com.fullrune.halfrobot.MISC;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Marcus on 2017-09-25.
 */

public class World {

    public World(Context context) {
        this.context = context;
    }

    public void loadMapNum(int num, Kartbit kartbit) {

        char[] kartpiece = kartbit.getKarta();
        AssetManager am = context.getAssets();
        String path = (w_path + Integer.toString(num) + ".plan");
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            is = new InputStreamReader(am.open(path));
            reader = new BufferedReader(is);
            String line;
            int counter = 0;

            while (counter != 15 && (line = reader.readLine()) != null) {
                String[] rowdata = line.split(",");
                for (int i = 0; i < rowdata.length; i++) {
                    int loc = i + counter * 20;
                    kartpiece[counter] = rowdata[i].charAt(0);
                }
                counter++;
            }

        } catch (IOException e) {
        } finally {
            try {
                if (is != null)
                    is.close();
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
            }
        }


    }

    public void readInfo() {

        String path = (w_path + "specs.plan");

        InputStreamReader is = null;
        BufferedReader reader = null;
        AssetManager am = context.getAssets();

        try {
            is = new InputStreamReader(am.open(path));
            reader = new BufferedReader(is);
            String line;

            line = reader.readLine();
            String[] rowdata = line.split(" ");
            int width; int height;
            width = Integer.parseInt(rowdata[0]);
            height = Integer.parseInt(rowdata[1]);
            worldMap.initArray(width, height);
            while ((line = reader.readLine()) != null){
                rowdata = line.split(" ");
                int name = Integer.parseInt(rowdata[0]);
                int posX = Integer.parseInt(rowdata[1]);
                int posY = Integer.parseInt(rowdata[2]);
                worldMap.placePiece(posX, posY, kartan.get(name-1).getKarta());
            }

        }
        catch (IOException e){
        }finally {try {
            if (is != null)
                is.close();
            if (reader != null)
                reader.close();
        }catch(IOException e){}
    }



    }

    private void initDimension(int w, int h) {
        int realW = w;

        if ((float) h / w >= 0.75f) {
            int x = w;
            int y = (int) ((3.0f / 4.0f) * w);
            w = x;
            h = y;
        } else {
            int y = h;
            int x = (int) ((4.0f / 3.0f) * h);
            w = x;
            h = y;
        }

        kartHeight = h;
        kartWidth = w;

        midX = (realW - w) / 2;
    }

    public void init(int w, int h, int w_id) {

        w_path = "bin/save/world" + Integer.toString(w_id) + "/";
        initDimension(w,h);

        for (int i = 0; i < 10; i++) {
            Kartbit piece = new Kartbit();
            piece.setName(i);
            loadMapNum(i + 1, piece);
            kartan.add(piece);
        }

        readInfo();
        worldMap.initWorld();
    }

    public static int getKartW() {
        return kartWidth;
    }

    public static int getKartH() {
        return kartHeight;
    }

    public static int getKartMidX() {
        return midX;
    }

    private ArrayList<Kartbit> kartan;
    private static int midX;
    private static int kartHeight;
    private static int kartWidth;
    private Worldmap worldMap;

    private String w_path;
    private Context context;

}
