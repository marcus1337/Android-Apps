package com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER;


import java.util.ArrayList;

import com.fullrune.areashiftertwo.MODEL.Model;
import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.Pieces.Unit;

public class Player extends Unit{

    private Model model;
    private Input direction;

    private PlayerState state;

    private boolean alive;
    private boolean movable;
    private int lives;

    private boolean riskComplete;
    private boolean riskFailed;

    private Input dirRiskEnd;

    private boolean shouldClearRiskLines;

    public boolean isShouldClearRiskLines(){
        return shouldClearRiskLines;
    }

    public void setShouldClearRiskLines(boolean shouldClearRiskLines){
        this.shouldClearRiskLines = shouldClearRiskLines;
    }

    public void setDirRiskEnd(Input dirRiskEnd){
        this.dirRiskEnd = dirRiskEnd;
    }

    public Input getDirRiskEnd(){
        return dirRiskEnd;
    }

    public void setRiskFailed(boolean riskFailed){
        this.riskFailed = riskFailed;
    }

    public boolean isRiskFailed(){
        return riskFailed;
    }

    public void setRiskComplete(boolean riskComplete){
        this.riskComplete = riskComplete;
    }

    public boolean isRiskComplete(){
        return riskComplete;
    }

    private ArrayList<Point> lineTrack;

    public int getLives(){
        return lives;
    }

    public void setLives(int lives){
        this.lives = lives;
    }

    public ArrayList<Point> getLineTrack(){
        return lineTrack;
        //return new ArrayList<Point>(lineTrack);
    }

    public void setLineTrack(ArrayList<Point> lineTrack){
        this.lineTrack = lineTrack;
    }

    public void addLineToTrack(Point point){
        lineTrack.add(point);
    }

    public void clearLineTrack(){
        lineTrack.clear();
    }

    public void setModel(Model model){
        this.model = model;
    }
    public Model getModel(){
        return model;
    }

    public Input getDirection() {
        return direction;
    }

    public Player(int x, int y, int radius){
        super(x,y,radius);
        init();
    }

    private void init(){
        setxyVel(5, 5);
        lives = 5;
        alive = true;
        movable = true;
        lineTrack = new ArrayList<Point>();
        state = new WaitingState();
        setBaseVelocity(5);
    }

    public void setState(PlayerState newState) {
        if(newState == null)
            return;
        state.end(this);
        state = newState;
        state.enter(this);
    }

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

    public void update() {
        state.update(this);
    }

    public void setDirectionAdjustVelocity(Input direction){
        setDirection(direction);
        PlayerHelp.adjustVelocityFromDirection(this);
    }

    public void setDirection(Input direction) {
        this.direction = direction;
    }

}
