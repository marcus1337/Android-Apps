package com.fullrune.areashiftertwo.MODEL.LOGIC;

import com.fullrune.areashiftertwo.MODEL.MapValue;
import com.fullrune.areashiftertwo.MODEL.Model;

import static com.fullrune.areashiftertwo.MODEL.MapValue.*;

public class LevelCreator {
    private Model model;

    private int lvlWidth, lvlHeight;

    public int getLvlWidth() {
        return lvlWidth;
    }

    public int getLvlHeight() {
        return lvlHeight;
    }

    public int[][] initMap(int lvl) {
        int[][] map = new int[getLvlWidth()][getLvlHeight()];

        for (int i = 0; i < lvlHeight; i++) {
            map[i][lvlWidth - 1] = EDGE.getValue();
            map[i][0] = EDGE.getValue();
        }

        for (int i = 0; i < lvlWidth; i++) {
            map[lvlHeight - 1][i] = EDGE.getValue();
            map[0][i] = EDGE.getValue();
        }

        switch (lvl) {
            case 5:
                for (int i = 0; i < lvlWidth; i++) {
                    map[i][150] = EDGE.getValue();
                    map[i][300] = EDGE.getValue();
                }
                break;

            case 6:
                makeSquare(getLvlWidth()/2, getLvlHeight()/2, 100, map);
                break;

            case 7:
                int rb = 30;
                for(int i = 0; i < 3; i++){
                    makeSquare(getLvlWidth()/2, getLvlHeight()/2, rb, map);
                    rb += 30;
                }
                break;

            case 10:
                for (int i = 0; i < lvlWidth; i++) {
                    map[i][100] = EDGE.getValue();
                    map[i][200] = EDGE.getValue();
                    map[i][300] = EDGE.getValue();

                    map[100][i] = EDGE.getValue();
                    map[200][i] = EDGE.getValue();
                    map[300][i] = EDGE.getValue();
                }
                break;
        }

        return map;
    }

    public void initLvlSettings(int lvl) {
        switch (lvl) {
            case 4:
                lvlWidth = 100;
                lvlHeight = 100;
                break;
        }
    }

    public LevelCreator(Model model) {
        this.model = model;
        lvlWidth = lvlHeight = 400;
    }

    private void makeSquare(int xm, int ym, int rad, int[][] map) {
        int left = xm - rad;
        int right = xm + rad;
        int up = ym - rad;
        int down = ym + rad;
        for (int i = left; i <= right; i++) {
            map[i][up] = EDGE.getValue();
            map[i][down] = EDGE.getValue();
        }
        for (int i = up; i <= down; i++) {
            map[left][i] = EDGE.getValue();
            map[right][i] = EDGE.getValue();
        }
    }
}
