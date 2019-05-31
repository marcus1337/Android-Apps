package com.fullrune.areashifter.MODEL;

import com.fullrune.areashifter.MODEL.Pieces.Enemy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marcus on 2017-05-03.
 */

public class Level implements Serializable {

    private Model model;
    private Logic logic;
    private ArrayList<Enemy> enemies;
    private int level;
    private int goal;
    private static final long serialVersionUID = -29238982928391L;

    public Level(Model model, Logic logic, ArrayList<Enemy> enemies){
        this.enemies = enemies;
        this.model = model;
        this.logic = logic;
        level = 0;
        goal = 70;
        counter = 0;
    }

    private int counter;

    public void nextLevel(){
        level++;
        goal-=2;

        counter++;
        if(counter >= 3){
            counter = 0;
            model.getPlayer().setLives(model.getPlayer().getLives()+1);
        }

        for(int i = 0; i < level; i++)
            logic.generateEnemy();

    }

    public boolean isGoalAchieved(int percentage){
        return percentage >= goal;
    }

    public int getLevel(){
        return level;
    }

    public int getGoal(){
        return goal;
    }

}
