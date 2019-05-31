package com.fullrune.areashifter.MODEL;

import com.fullrune.areashifter.MODEL.PLAYER.Input;
import com.fullrune.areashifter.MODEL.PLAYER.Player;
import com.fullrune.areashifter.MODEL.PLAYER.WaitingState;
import com.fullrune.areashifter.MODEL.Pieces.Enemy;
import com.fullrune.areashifter.MODEL.Pieces.Line;
import com.fullrune.areashifter.MODEL.Pieces.Point;
import com.fullrune.areashifter.MODEL.Pieces.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Marcus on 2017-04-24.
 */

public class Model implements Serializable {

    private Player player;
    private int w, h;
    private ArrayList<Line> safeLines;
    private ArrayList<Line> anyLines;
    private ArrayList<Point> filledPoints;
    private ArrayList<Enemy> enemies;
    private int standardRadius;

    private Level level;
    private long totalScore;
    private int extraScore, levelScore;
    private Logic logic;
    private int[][] gameB;
    private boolean lvlStarted;

    private static final long serialVersionUID = -29238982928391L;

    public Model() {
        safeLines = new ArrayList<Line>();
        filledPoints = new ArrayList<Point>();
        enemies = new ArrayList<Enemy>();
        anyLines = new ArrayList<Line>();
        totalScore = 0;
        extraScore = 0;
        lvlStarted = false;
        gameOver = false;
        gameWon = false;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public int percTaken() {
        return (int) (((float) levelScore / (w * h)) * 100);
    }

    public boolean isLevelStarted() {
        return lvlStarted;
    }

    private void nextLevel() {
        levelScore = 0;
        gameB = new int[w][h];
        logic.setMap(gameB);
        player.setX(0);
        player.setY(0);

        safeLines.clear();
        filledPoints.clear();
        enemies.clear();
        anyLines.clear();
        ArrayList<Line> startLines = new ArrayList<Line>();
        startLines.add(new Line(0, 0, w - 1, 0)); //upp-höger-vänster
        startLines.add(new Line(0, h - 1, w - 1, h - 1)); //ner-höger-vänster
        startLines.add(new Line(0, 0, 0, h - 1)); //upp-ner-vänster
        startLines.add(new Line(w - 1, 0, w - 1, h - 1));//upp-ner-höger
        addLines(startLines);

        level.nextLevel();
    }

    public int getGoal() {
        return level.getGoal();
    }

    public int getLevel() {
        if (level.isGoalAchieved(percTaken()))
            return level.getLevel()+1;
        return level.getLevel();
    }

    public void init(int width, int height) {
        w = width;
        h = height;
        gameB = new int[w][h];
        standardRadius = (int) (h * 0.02f);
        levelScore = 0;

        safeLines.clear();
        filledPoints.clear();
        enemies.clear();
        anyLines.clear();

        player = new Player(0, 0, standardRadius, this);
        player.setLives(5);

        logic = new Logic(gameB, w, h, player, safeLines, anyLines, filledPoints, enemies, this, standardRadius);
        level = new Level(this, logic, enemies);

        nextLevel();
    }

    private boolean pauseGame = true;

    public void setPaused(boolean pauseGame) {
        this.pauseGame = pauseGame;
        if(!pauseGame){
            lvlStarted = true;
        }
    }

    public void beginNextLevel(){
        if (level.isGoalAchieved(percTaken())) {
            lvlStarted = true;
            pauseGame = false;
            nextLevel();
        }
    }

    public boolean isPaused(){
        return pauseGame;
    }
    public boolean isGameOver(){return gameOver;}
    public boolean isGameWon(){
        if(level.getGoal() <= 4 && level.isGoalAchieved(percTaken())) {
            gameWon = true;
        }
        return gameWon;
    }

    private boolean gameWon;
    private boolean gameOver;

    public void restart(){
        totalScore -= levelScore;
        levelScore = 0;

        gameB = new int[w][h];
        logic.setMap(gameB);
        player.setState(new WaitingState());
        player.getRiskLines().clear();
        player.setLives(player.getLives()-1);
        player.setX(0);
        player.setY(0);

        safeLines.clear();
        filledPoints.clear();
        anyLines.clear();
        ArrayList<Line> startLines = new ArrayList<Line>();
        startLines.add(new Line(0, 0, w - 1, 0)); //upp-höger-vänster
        startLines.add(new Line(0, h - 1, w - 1, h - 1)); //ner-höger-vänster
        startLines.add(new Line(0, 0, 0, h - 1)); //upp-ner-vänster
        startLines.add(new Line(w - 1, 0, w - 1, h - 1));//upp-ner-höger
        addLines(startLines);
    }

    public void update(long timeDiff) {

        if(pauseGame)
            return;

        if (level.isGoalAchieved(percTaken())) {
            lvlStarted = false;
            pauseGame = true;
        }

        for (Enemy e : enemies) {
            e.update(timeDiff);
            if (logic.collision(e)) {
                player.handleInput(Input.KILLED);
            }
        }

        if(player.getLives() < 0){
            gameOver = true;
            pauseGame = true;
        }

        player.update(timeDiff);
    }


    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public ArrayList<Line> getSafeLines() {
        ArrayList<Line> tmp = new ArrayList<Line>(safeLines);
        safeLines.clear();
        return tmp;
    }

    public ArrayList<Point> getFilledPoints() {
        if (filledPoints.size() != 0)
            extraScore += filledPoints.size();
        return filledPoints;
    }

    public int getExtraScore() {
        int tmp = extraScore;
        extraScore = 0;
        totalScore += tmp;
        levelScore += tmp;
        return tmp;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public boolean inBounds(int x, int y) {
        return logic.inBounds(x, y);
    }

    public void addLines(ArrayList<Line> lines) {
        for (Line l : lines)
            logic.lineToGameB(l);
        safeLines.addAll(lines);
        anyLines.addAll(lines);
    }


    public boolean isPlayerOnline() {
        return logic.isPlayerOnline();
    }

    public boolean isPointOnLine(int x, int y) {
        return logic.isPointOnLine(x, y);
    }

    public int[][] getMap() {
        return gameB;
    }

    public boolean isMovePossible() {
        return logic.isMovePossible();
    }

    public boolean isMoveRisky() {
        return logic.isMoveRisky();
    }

    public boolean playerHitWall() {
        return logic.playerHitWall();
    }


    public Player getPlayer() {
        return player;
    }


}
