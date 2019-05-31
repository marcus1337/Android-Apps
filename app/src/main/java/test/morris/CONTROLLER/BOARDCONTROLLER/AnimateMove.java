package test.morris.CONTROLLER.BOARDCONTROLLER;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

/**
 * Created by Marcus on 2016-11-22.
 */

//animerar pjäser som flyttar sig genom att anropa CustomBoard
public class AnimateMove {

    private CustomBoard customBoard;
    private float startX, startY, endX, endY;
    private ValueAnimator valueAnimator;
    private boolean isDone;
    private float xSpeed, ySpeed;

    public AnimateMove(CustomBoard customBoard) {
        this.customBoard = customBoard;
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(800);
        isDone = true;
        valueAnimator.addUpdateListener(new AniListener());
        valueAnimator.addListener(new AniFinishListener());
        startX = startY = endY = endX = 0;
    }

    public void start() {
        isDone = false;
        valueAnimator.start();
    }

    public boolean isFinished(){
        return isDone;
    }

    public void init(float startX, float startY, float endX, float endY, int turn) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        xSpeed = (endX - startX) / 40;
        ySpeed = (endY - startY) / 40;
    }


    private class AniListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            startX += xSpeed;
            startY += ySpeed;
            customBoard.setAniXY(startX, startY);
            customBoard.invalidate();

            if(Math.abs(endX - startX) < 10 && Math.abs(endY - startY) < 10)
                valueAnimator.cancel();
        }
    }

    private class AniFinishListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationEnd(Animator animation)
        {
            customBoard.setLatestMove(-1);
            customBoard.setAniXY(-1000,-1000);
            isDone = true;
        }

    }

}
