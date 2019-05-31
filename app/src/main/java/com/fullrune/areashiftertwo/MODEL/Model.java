package com.fullrune.areashiftertwo.MODEL;


import android.util.Log;

import com.fullrune.areashiftertwo.MODEL.LOGIC.LevelHandler;
import com.fullrune.areashiftertwo.MODEL.LOGIC.PathHelp;
import com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES.Enemy;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Player;
import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.SAVING.Save;

import java.util.ArrayList;

public class Model {

    private Save save;
    private int score, tempscore;
    private Player player;
    private int[][] map;
    private int rows, cols;
    private int level;

    private boolean boardChanged;
    private LevelHandler levelHandler;
    private ArrayList<Enemy> enemies;

    private MapShift mapShift;

    private ArrayList<Point> oldRiskLines;
    private boolean onlyLineChange;

    private long gameTicks;

    public int getLevel(){
        return level;
    }

    public int getTempscore(){
        return tempscore;
    }

    public int getLives(){
        return player.getLives();
    }

    public long getGameTicks(){
        return gameTicks;
    }

    public boolean isOnlyLineChange(){
        return onlyLineChange;
    }
    public int getBoxLeft(){
        return PathHelp.boxLeft;
    }
    public int getBoxRight(){
        return PathHelp.boxRight;
    }
    public int getBoxTop(){
        return PathHelp.boxTop;
    }
    public int getBoxBottom(){
        return PathHelp.boxBottom;
    }

    public void setOldRiskLines(ArrayList<Point> oldRiskLines1){
        this.oldRiskLines = new ArrayList<Point>(oldRiskLines1);
    }

    public ArrayList<Point> getOldRiskLines(){
        return oldRiskLines;
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

    public Model(){
        player = new Player(0,0,6);
        player.setLives(5);
        player.setModel(this);
        player.setBaseVelocity(3);
        oldRiskLines = new ArrayList<Point>();
    }

    public void process(){
        gameTicks++;
        player.update();

        for(Enemy e : enemies){
            e.process(this);
        }

        if(player.isShouldClearRiskLines()){
            setOldRiskLines(player.getLineTrack());
            boardChanged = true;

            mapShift.removeLinesToEmpty(player.getLineTrack());
            onlyLineChange = true;

            player.setRiskComplete(false);
            player.getLineTrack().clear();
            player.setShouldClearRiskLines(false);
        }

        if(player.isRiskComplete()){

            ArrayList<Point> badPoints = PathHelp.getBadPoints(getMap(), enemies);

          //  Log.i("TEST_ABC", " BAD X: " + badPoints.get(0).x + " Y: " + badPoints.get(0).y);
            Point p = PathHelp.getGreedyFreePoint(getPlayer(),getMap(), badPoints);
           // Log.i("TEST_ABC2" , "A " + p.x + " B " + p.y);

            setOldRiskLines(player.getLineTrack());
            mapShift.shiftWalls(p);
            mapShift.removeLinesAndEdges(player.getLineTrack());
            setScore(getScore()+mapShift.getWallCounter());
            tempscore = mapShift.getWallCounter();
            mapShift.setWallCounter(0);

            if(p == null)
                onlyLineChange = true;
            else
                onlyLineChange = false;

            boardChanged = true;
            player.setRiskComplete(false);
            player.getLineTrack().clear();
        }

    }

    public boolean isBoardChanged(){
        return boardChanged;
    }

    public void setBoardChanged(boolean boardChanged){
        this.boardChanged = boardChanged;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setInput(Input input){
        player.handleInput(input);
    }

    public Player getPlayer(){
        return player;
    }

    public int[][] getMap(){
        return map;
    }
    public int getMapWidth(){
        return cols;
    }
    public int getMapHeight(){
        return rows;
    }

    public boolean isGameLost(){
        return getLives() < 0;
    }
    public boolean isGameWon(){
        return levelHandler.isGameWon(percTaken());
    }

    public void initLevel(int level){
        this.level = level;
        levelHandler = new LevelHandler(level, this);
        levelHandler.init();
        map = levelHandler.getMap();
        rows = map.length;
        cols = map[0].length;

        mapShift = new MapShift(map, rows, cols);

        enemies = levelHandler.getEnemies();

    }

    public void initSave(Save save){

    }


    public int getScoreProcentGoal() {
        return levelHandler.getGoalProc();
    }

    public int percTaken() {
        return Math.round((float)getScore()/(getMapWidth()*getMapHeight())*100);
    }
}
