package com.fullrune.snakebattle;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import SINGLE.SingleGame;
import STATES.GameState;
import STATES.Player;

public class OnSwipeListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private GameState gameState;
    private SingleGame singleGame;

    public OnSwipeListener(Context context, GameState gameState) {
        this.gameState = gameState;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public OnSwipeListener(Context context, SingleGame singleGame) {
        this.singleGame = singleGame;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {
        if (gameState != null)
            gameState.setDir(Player.Move.LEFT.getValue());
        if (singleGame != null)
            singleGame.updatePlayerMove(Player.Move.LEFT.getValue());
    }

    public void onSwipeRight() {
        if (gameState != null)
            gameState.setDir(Player.Move.RIGHT.getValue());
        if (singleGame != null)
            singleGame.updatePlayerMove(Player.Move.RIGHT.getValue());
    }

    public void onSwipeUp() {
        if (gameState != null)
            gameState.setDir(Player.Move.UP.getValue());
        if (singleGame != null)
            singleGame.updatePlayerMove(Player.Move.UP.getValue());
    }

    public void onSwipeDown() {
        if (gameState != null)
            gameState.setDir(Player.Move.DOWN.getValue());
        if (singleGame != null)
            singleGame.updatePlayerMove(Player.Move.DOWN.getValue());
    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 80;
        private static final int SWIPE_VELOCITY_THRESHOLD = 80;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
                return true;
            } else if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceY > 0)
                    onSwipeDown();
                else
                    onSwipeUp();
                return true;
            }

            return false;
        }
    }
}
