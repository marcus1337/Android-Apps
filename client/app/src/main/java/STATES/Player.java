package STATES;

import java.util.ArrayList;



public class Player {

    public enum Move{
        DOWN(1), LEFT(2), RIGHT(3), UP(4);

        private int value;

        private Move(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public int recentCounter;
    public int x, y, dir;
    boolean dead;

    public Player(int x, int y) {
        recentCounter = 0;
        this.x = x;
        this.y = y;
        dir = 1;
        dead = false;
        recentMoves = new ArrayList<Move>();
    }

    public void setRecentMove(Move move){
        recentMoves.add(0, move);
        if(recentMoves.size() >= 4){
            recentMoves.remove(3);
        }
    }
    public ArrayList<Move> recentMoves;
}