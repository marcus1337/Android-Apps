package SINGLE;

import android.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SingleGame {

    private boolean done;

    SingPlayer player;
    int[][] board;
    int w, h;

    long startTime, goTime;
    int startDelayMilli;
    boolean gameStarted;

    private ArrayList<Apple> apples;

    int lvl;

    public int getLevel(){
        return lvl;
    }

    private void calcLevel(){
        if(getScore() >= 30){
            lvl = 1;
            int tmp = getScore();
            if(tmp / 30 >= 1){
                tmp -= 30;
                lvl++;
            }

        }
        if(lvl >= 50)
            lvl = 50;
    }

    public ArrayList<Apple> getApples() {
        //System.out.println("TEST: " + lvl);
        ArrayList<Apple> tmp = new ArrayList<Apple>();
        for(int i = 0; i < getLevel(); i++){
            tmp.add(apples.get(i));
        }
        return tmp;
    }

    public int[][] getBoard() {
        return board;
    }

    public SingPlayer getPlayer() {
        return player;
    }

    public int getScore(){
        return player.getScore();
    }

    public void setDir(int _dir) {
        player.dir = _dir;
    }

    public SingleGame() {
        gameStarted = false;
        startTime = 0;
        goTime = 0;
        done = false;
        startDelayMilli = 3000;
        player = new SingPlayer(25, 15);
        w = 34;
        h = 20;
        lvl = 1;
        board = new int[w][h];
        apples = new ArrayList<Apple>();

        for(int i = 0; i < 50; i++){
            apples.add(new Apple(player, w, h));
        }
    }

    public boolean isDone() {
        return done;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public long millisLeft(){
        return goTime - System.currentTimeMillis();
    }
    public long secondsLeft(){
        if(goTime == 0)
            return 3;
        return TimeUnit.MILLISECONDS.toSeconds(goTime - System.currentTimeMillis());
    }

    public enum Move {
        DOWN(1), LEFT(2), RIGHT(3), UP(4);
        private int value;

        private Move(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public void updatePlayerMove(int direction) {
        if (direction == 1 && player.dir != 4) {
            player.dir = direction;
        } else if (direction == 2 && player.dir != 3) {
            player.dir = direction;
        } else if (direction == 3 && player.dir != 2) {
            player.dir = direction;
        } else if (direction == 4 && player.dir != 1) {
            player.dir = direction;
        }
    }

    void movePlayer(SingPlayer player) {
        if (player.dir == Move.UP.getValue()) {
            player.y -= 1;
            player.setRecentMove(Move.UP);
            player.setRecPos(player.x, player.y);
        }
        if (player.dir == Move.LEFT.getValue()) {
            player.x -= 1;
            player.setRecentMove(Move.LEFT);
            player.setRecPos(player.x, player.y);
        }
        if (player.dir == Move.RIGHT.getValue()) {
            player.x += 1;
            player.setRecentMove(Move.RIGHT);
            player.setRecPos(player.x, player.y);
        }
        if (player.dir == Move.DOWN.getValue()) {
            player.y += 1;
            player.setRecentMove(Move.DOWN);
            player.setRecPos(player.x, player.y);
        }
    }

    public void checkCollision(SingPlayer player) {
        if(player.x == 0 && player.dir == Move.LEFT.value){
            player.dead = true;
        }
        if(player.x == w-1 && player.dir == Move.RIGHT.value){
            player.dead = true;
        }
        if(player.y == 0 && player.dir == Move.UP.value){
            player.dead = true;
        }
        if(player.y == h-1 && player.dir == Move.DOWN.value){
            player.dead = true;
        }
        if (board[player.x][player.y] != 0 && board[player.x][player.y] != 9) {
            player.dead = true; //Hit himself or other player
        }
    }

    public void process() {

        calcLevel();
        if (done) {
            return;
        }

        if (!gameStarted) {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
                goTime = startTime + startDelayMilli;
            } else if (System.currentTimeMillis() > goTime) {
                gameStarted = true;
            }
            return;
        }

        checkCollision(player);

        for(int i = 0; i < getLevel(); i++){
            Apple apple = apples.get(i);
            if(apple.eaten){
                player.maxLen += lvl;
                player.maxLen += 2;
                apple.init(player, w, h);
            }
        }

        if (player.dead) {
            done = true;
            return;
        }

        board[player.x][player.y] = 1;
        movePlayer(player);

        for(int i = 0; i < getLevel(); i++){
            Apple apple = apples.get(i);
            if(!apple.eaten && player.x == apple.x && player.y == apple.y){
                apple.eaten = true;
                player.score += 10;
            }
        }

        Pair<Integer, Integer> deletedPos = player.getDeletedPos();
        if (deletedPos != null) {
            board[deletedPos.first][deletedPos.second] = 0;
        }

    }


}
