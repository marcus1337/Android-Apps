package SINGLE;

import android.util.Pair;

import java.util.ArrayList;

public class SingPlayer {
    public int x, y, dir;
    boolean dead;
    int maxLen;
    int curLen;
    int score;

    public void setMaxLen(int maxLen){
        this.maxLen = maxLen;
    }

    public int getMaxLen(){
        return maxLen;
    }

    public Pair<Integer, Integer> getDeletedPos(){
        return delPos;
    }

    Pair<Integer, Integer> delPos;

    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }

    public SingPlayer(int x, int y) {
        score = 0;
        delPos = null;
        this.x = x;
        this.y = y;
        dir = SingleGame.Move.UP.getValue();
        dead = false;
        maxLen = 5;
        curLen = 0;
        recentMoves = new ArrayList<SingleGame.Move>();
        positions = new ArrayList<Pair<Integer, Integer>>();
    }

    public void setRecPos(int _x, int _y){
        if(curLen < maxLen){
            curLen++;
            delPos = null;
        }else{
            delPos = positions.get(0);
            positions.remove(0);
        }
        positions.add(new Pair<Integer, Integer>(_x, _y));
    }

    public void setRecentMove(SingleGame.Move move){
        recentMoves.add(0, move);
        if(recentMoves.size() >= 4){
            recentMoves.remove(3);
        }
    }
    public ArrayList<Pair<Integer, Integer>> getPositions(){
        return new ArrayList<Pair<Integer, Integer>>(positions);
    }

    ArrayList<SingleGame.Move> recentMoves;
    private ArrayList<Pair<Integer, Integer>> positions;
}
