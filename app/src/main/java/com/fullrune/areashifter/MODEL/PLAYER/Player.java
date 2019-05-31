package com.fullrune.areashifter.MODEL.PLAYER;

import com.fullrune.areashifter.MODEL.Pieces.Line;
import com.fullrune.areashifter.MODEL.Model;
import com.fullrune.areashifter.MODEL.Pieces.Point;
import com.fullrune.areashifter.MODEL.Pieces.Unit;

import java.util.ArrayList;

/**
 * Created by Marcus on 2017-04-24.
 */

public class Player extends Unit {
    private static final long serialVersionUID = -29238982928391L;
    private PlayerState state;
    private Model model;
    private Input direction;
    private int velocity;
    private int lives;

    public int getLives(){
        return lives;
    }

    public void setLives(int lives){
        this.lives = lives;
    }

    private ArrayList<Line> riskLines;

    public ArrayList<Line> getRiskLines() {
        return riskLines;
    }

    public Input getDirection() {
        return direction;
    }

    public void setState(PlayerState newState) {
        if(newState == null)
            return;
        state = newState;
        state.enter(this);
    }

    public void registerLines() {
        model.addLines(riskLines);
        riskLines.clear();
    }

    public void setDirection(Input direction) {
        this.direction = direction;
        if (direction == null) {
            setxVel(0);
            setyVel(0);
        } else
            switch (direction) {
                case MOVE_UP:
                    setxVel(0);
                    setyVel(-velocity);
                    break;
                case MOVE_DOWN:
                    setxVel(0);
                    setyVel(velocity);
                    break;
                case MOVE_LEFT:
                    setxVel(-velocity);
                    setyVel(0);
                    break;
                case MOVE_RIGHT:
                    setxVel(velocity);
                    setyVel(0);
                    break;
            }

    }

    public boolean isMoveRisky() {
        return model.isMoveRisky();
    }

    public boolean isOnLine() {
        return model.isPlayerOnline();
    }

    public Model getModel(){
        return model;
    }

    public boolean isMovePossible(){
        return model.isMovePossible();
    }

    public boolean hitRiskLine() {
        int x2 = getX();
        int y2 = getY();
        int x1 = x2 + getxVel();
        int y1 = y2 + getyVel();
        Line playerLine = new Line(x1, y1, x2, y2);
        for (Line l : riskLines) {
            Point point = playerLine.intersect(l);
            if (point != null) {
                return true;
            }
        }
        return false;
    }

    //public void moveToEdge(){
    //   model.movePlayerToEdge();
    //}

    public Player(int x, int y, int radius, Model model) {
        super(x, y, radius);
        riskLines = new ArrayList<Line>();
        alive = true;
        movable = false;
        state = new WaitingState();
        state.enter(this);
        this.model = model;
        velocity = 1;

    }

    public boolean hasHitWall() {
        if (model.playerHitWall()) {
            return true;
        }
        return false;
    }

    private boolean alive;
    private boolean movable;

    public boolean isAlive(){
        return alive;
    }

    public boolean isMovable(){
        return movable;
    }

    public void setMovable(boolean movable){
        this.movable = movable;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public void handleInput(Input input) {
        setState(state.handleInput(this, input));
    }

    public void update(long timeDiff) {
        state.update(this, timeDiff);
    }


}
