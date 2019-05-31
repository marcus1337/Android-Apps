package com.fullrune.areashiftertwo.MODEL.LOGIC;

import com.fullrune.areashiftertwo.MODEL.Model;
import com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES.Enemy;
import com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES.EnemyA;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Player;
import com.fullrune.areashiftertwo.MODEL.Pieces.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class LevelHandler implements Serializable {

    private int lvl, goalProc;
    private ArrayList<Enemy> enemies;
    private Player player;
    private Model model;
    private LevelCreator levelCreator;

    public ArrayList<Enemy> getEnemies(){
        return new ArrayList<Enemy>(enemies);
    }

    public int getGoalProc(){
        return goalProc;
    }

    public int[][] getMap(){
        return levelCreator.initMap(lvl);
    }

    public LevelHandler(int lvl, Model model){
        levelCreator = new LevelCreator(model);
        levelCreator.initLvlSettings(lvl);
        this.model = model;
        this.player = model.getPlayer();
        goalProc = 90;
        this.lvl = lvl;
        enemies = new ArrayList<Enemy>();

    }

    public void init(){
        player.setLives(5);
        goalProc = 80;

        switch (lvl) {
            case 1:
                break;
            case 2:
                player.setLives(3);
                goalProc = 70;
                break;
            case 3:
                break;

            case 4:
                player.setLives(15);
                player.setRadius(2);
                player.setBaseVelocity(1);
                goalProc = 94;
                break;

            case 5:
                goalProc = 65;
                break;

            case 6:
                player.setLives(10);

                break;

            case 7:
                player.setLives(13);
                break;
            case 8:
                player.setLives(2);
                break;

            case 9:
                player.setLives(20);
                break;

            case 10:
                player.setLives(9);
                goalProc = 50;
                break;

            case 11:
                player.setLives(0);
                break;

                default:

                    break;
        }

        initEnemies(lvl);
    }

    private void initEnemies(int lvl){
        Enemy enemy;
        switch (lvl) {
            case 1:
                enemies.add(new EnemyA(300,300,5));
                break;

            case 2:
                for(int i = 0; i < 3; i++)
                    enemies.add(new EnemyA(getRand(100,300),
                            getRand(50,300),getRand(3,6)));
                break;

            case 3:
                for(int i = 0; i < 5; i++)
                    enemies.add(new EnemyA(getRand(10,350),getRand(30,380),5));
                break;

            case 4:
                enemies.add(new EnemyA(levelCreator.getLvlWidth()/2,levelCreator.getLvlHeight()/2+2,3));
                break;

            case 5:
                enemies.add(new EnemyA(getRand(10,350),getRand(30,110),5));

                for(int i = 0; i < 2; i++){
                    enemy = new EnemyA(getRand(1,355),getRand(160,270),5);
                    enemies.add(enemy);
                }

                for(int i = 0; i < 5; i++){
                    enemy = new EnemyA(getRand(10,350),getRand(310,390),4);
                    enemy.setxyVel(enemy.getxVel()*2, enemy.getyVel()*2);
                    enemies.add(enemy);
                }

                break;

            case 6:
                for(int i = 0; i < 8; i++){
                    enemies.add(new EnemyA(getRand(110,290),getRand(110,290),5));
                }
                Enemy speedyEnemy = new EnemyA(getRand(110,290),getRand(10,50),3);
                speedyEnemy.setxyVel(speedyEnemy.getxVel()*3, speedyEnemy.getyVel()*3);
                enemies.add(speedyEnemy);
                break;

            case 7:
                for(int i = 0; i < 10; i++)
                    enemies.add(new EnemyA(getRand(1,399),getRand(1,399),4));
                break;

            case 8:
                for(int i = 0; i < 10; i++){
                    enemy = new EnemyA(getRand(1,399),getRand(1,399),4);
                    enemy.setxyVel(enemy.getxVel()+1, enemy.getyVel()+1);
                    enemies.add(enemy);
                }
                break;

            case 9:
                for(int i = 0; i < 7; i++){
                    enemy = new EnemyA(getRand(1,399),getRand(1,399),5);
                    enemy.setxyVel(enemy.getxVel()*2, enemy.getyVel()*2);
                    enemies.add(enemy);
                }
                break;

            case 10:

                for(int i = 1; i < 16; i++){
                    int tmpx = (i%4)*100+50;
                    int tmpy = 50;
                    if(i > 3){
                        tmpy += 100;
                    }
                    if(i > 7){
                        tmpy += 100;
                    }
                    if(i > 11){
                        tmpy += 100;
                    }

                    enemy = new EnemyA(tmpx,tmpy,5);
                    enemies.add(enemy);
                }

                enemy = new EnemyA(80,80,4);
                enemy.setxyVel(1,0);
                enemies.add(enemy);

                break;

            case 11:
                enemy = new EnemyA(levelCreator.getLvlWidth()/2+50,levelCreator.getLvlHeight()/2+50,5);
                enemies.add(enemy);
                break;

            case 12:
                player.setLives(1);
                for(int i = 0; i < 2; i++)
                    enemies.add(new EnemyA(getRand(1,399),getRand(1,399),4));
                break;

            default:
                enemy = new EnemyA(levelCreator.getLvlWidth()/2+50,levelCreator.getLvlHeight()/2+50,5);
               // enrand.setxyVel(2,2);
                enemies.add(enemy);

                break;
        }
    }

    private int getRand(int low, int high){
        Random rand = new Random();
        int result = rand.nextInt(high - low) + low;
        return result;
    }

    public boolean isGameWon(int percTaken){

        return goalProc < percTaken;
    }

}
