package com.fullrune.areashifter.CONTROLLER;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.fullrune.areashifter.CONTROLLER.COMMANDS.Command;
import com.fullrune.areashifter.CONTROLLER.COMMANDS.CommandManager;
import com.fullrune.areashifter.MODEL.PLAYER.Input;

/**
 * Created by Marcus on 2017-04-25.
 */

public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;
    private CommandManager commandManager;

    public OnSwipeTouchListener(Context context, CommandManager commandManager) {
        this.commandManager = commandManager;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {
        commandManager.handleInput(Input.MOVE_LEFT);
    }

    public void onSwipeRight() {
        commandManager.handleInput(Input.MOVE_RIGHT);
    }

    public void onSwipeUp(){
        commandManager.handleInput(Input.MOVE_UP);
    }

    public void onSwipeDown(){
        commandManager.handleInput(Input.MOVE_DOWN);
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
            }else
            if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
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
