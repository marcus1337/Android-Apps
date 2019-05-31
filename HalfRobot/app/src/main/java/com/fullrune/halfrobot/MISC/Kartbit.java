package com.fullrune.halfrobot.MISC;

import android.graphics.Bitmap;

/**
 * Created by Marcus on 2017-09-25.
 */

public class Kartbit {

    public Kartbit(){
        karta = new char[300];
        _neighbors = new int[]{-1,-1,-1,-1};
    }

    public char[] getKarta(){
        return karta;
    }

    public void setName(int name) {
        _name = name;
    }

    public int getName() {
        return _name;
    }

    public void setNeighbors(int neighbors[]) {
        for (int i = 0; i < 4; i++)
            _neighbors[i] = neighbors[i];
    }

    public int[] getNeighbors() {
        return _neighbors;
    }

    private char[] karta;
    private int _name;
    private int[] _neighbors;

}
