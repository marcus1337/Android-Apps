package com.fullrune.halfrobot.LOGIC.ENTITY;

import android.graphics.Bitmap;

import com.fullrune.halfrobot.LOGIC.GRAPHICS.Graphics;
import com.fullrune.halfrobot.LOGIC.INPUT.playerinputset.EntityInput;
import com.fullrune.halfrobot.LOGIC.Model;
import com.fullrune.halfrobot.LOGIC.PHYSICS.Physics;

/**
 * Created by Marcus on 2017-09-26.
 */

public abstract class Entity {

    Entity(Physics physics, Graphics graphics, EntityInput input){
        _physics = physics;
        _graphics = graphics;
        _input = input;
        _x = _y = _velX = _velY = 0;
        _width = 32;
        _height = 32;
        _maxHp = _hp = 100;
        _startedDying = false;
        _dead = false;
        _direction = 0;
    }


    public void update(Model model){

        if (isDead() || startedDying())
            return;

        _input.handleInput(this);
        _physics.update(model, this);

        if (isFiring()) {
            int diff = (int)((model.getTicks()-pointTime));
            if (diff > _milliSecondTimer) {
                setFiring(false);
            }

        }
        stime = model.getTicks();
    }

    public void render(Bitmap renderer, float lag, int xPos, int yPos){
        _graphics.render(lag, this, renderer, xPos, yPos);
    }
    public void render2(Bitmap renderer, float lag, int xPos, int yPos, long ticks){
        _graphics.render2(lag, this, renderer, xPos, yPos, ticks);
    }

    Physics getPhysics(){
        return _physics;
    }

    Graphics getGraphics(){
        return _graphics;
    }

    public EntityInput getInput(){
        return _input;
    }

    public int getX(){
        return _x;
    }

    public int getY(){
        return _y;
    }

    public int getVelocityX(){
        return _velX;
    }

    public int getVelocityY(){
        return _velY;
    }

    public int getX2() {
        return _x + _width;
    }

    public int getY2() {
        return _y + _height;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public void setWidthHeight(int width, int height) {
        _width = width;
        _height = height;
    }

    public void setWidth(int width) {
        _width = width;
    }

    public void setHeight(int height) {
        _height = height;
    }

    public void setVelocityXY(int xVel, int yVel){
        _velX = xVel;
        _velY = yVel;
    }

    public void setVelX(int xVel) {
        _velX = xVel;
    }

    public void setVelY(int yVel) {
        _velY = yVel;
    }

    public void setXY(int x, int y) {
        _x = x; _y = y;
    }
    public void setY(int y){
        _y = y;
    }
    public void setX(int x) {
        _x = x;
    }

    public void addXY(int x, int y){
        _x += x; _y += y;
    }

    public void setDirection(int _dir) { // true == right
        _direction = _dir;
    }

    ///***
    // @param direction, 0 = left, 1 = right, 2 = up, 3 = down
    // */
    public int getDirection() {
        return _direction;
    }

    public void setFiring(boolean firing) {
        pointTime = stime;
        _firing = firing;
    }

    public boolean isFiring() {
        return _firing;
    }

    public boolean isDead() {
        return _dead;
    }

    public boolean startedDying() {
        if (getHP() <= 0)
            setStartedDying(true);
        return _startedDying;
    }

    public void setStartedDying(boolean startedDying) {
        _startedDying = startedDying;
    }

    public void setDead(boolean dead) {
        _dead = dead;
        if (_dead) {
            deadTime = stime;
        }

    }

    public int timeDead() {
        return (int)(stime - deadTime);
    }

    public int getMaxHP() {
        return _maxHp;
    }

    public void setMaxHP(int maxHp) {
        _maxHp = maxHp;
        _hp = maxHp;
    }

    public void setHP(int hp) {
        _hp = hp;
        if (_hp > _maxHp)
            _hp = _maxHp;
    }

    public int getHP() {
        return _hp;
    }

    public void setWantFire(boolean wantFire) {
        _wantFire = wantFire;
    }

    public boolean isWantFire() {
        return _wantFire;
    }

    public void setMilliSecondTimer(int millisecondTimer) {
        _milliSecondTimer = millisecondTimer;
    }

    public void setID(int id) {
        _id = id;
    }

    public int getID() {
        return _id;
    }

    public void setDamaged(boolean damaged) {
        _damaged = damaged;
    }

    public boolean isDamaged() {
        return _damaged;
    }

    public void setDamagedTime(long stime) {
        this.stime = stime;
    }

    public int getDamagedTime(long nowTime) {
        int diff = (int)(nowTime - stime);
        return diff;
    }

    protected Physics _physics;
    protected Graphics _graphics;
    protected EntityInput _input;

    protected boolean checkCollision(Entity e){
        if (getX() > e.getX() && getX() < e.getX2() && getY() > e.getY() && getY() < e.getY2())
            return true;
        if (getX2() > e.getX() && getX2() < e.getX2() && getY() > e.getY() && getY() < e.getY2())
            return true;
        if (getX() > e.getX() && getX() < e.getX2() && getY2() > e.getY() && getY2() < e.getY2())
            return true;
        if (getX2() > e.getX() && getX2() < e.getX2() && getY2() > e.getY() && getY2() < e.getY2())
            return true;

        int midy1 = getY() + getHeight() / 2;
        if (getX() > e.getX() && getX() < e.getX2() && midy1 > e.getY() && midy1 < e.getY2())
            return true;
        if (getX2() > e.getX() && getX2() < e.getX2() && midy1 > e.getY() && midy1 < e.getY2())
            return true;

        return false;
    }

    private long stime;

    private int _direction = 0;
    private int _x, _y, _width, _height;
    private int _velX, _velY;
    private int _hp = 0;
    private int _maxHp;
    private boolean _firing = false;
    private boolean _wantFire = false;
    private boolean _dead = false;
    private boolean _startedDying;
    private boolean _damaged = false;
    private int _id = 0;


    private long deadTime;
    private long pointTime;
    private int _milliSecondTimer = 30;

}
