package com.fullrune.halfrobot.LOGIC.PHYSICS;

import com.fullrune.halfrobot.LOGIC.ENTITY.Entity;
import com.fullrune.halfrobot.LOGIC.Model;
import com.fullrune.halfrobot.MISC.Worldmap;

/**
 * Created by Marcus on 2017-09-26.
 */

public class PhysicsPlayer extends Physics {

    PhysicsPlayer() {
        xPathBlocked = false;
        yPathBlocked = false;
    }

    @Override
    public void update(Model model, Entity entity){
        Worldmap worldmap = model.getPlayerMap();
        bw = worldmap.getWidth();
	    char[] karta = worldmap.getMapArr();

        if (entity.isDead())
            return;

        int xVel = entity.getVelocityX();
        int yVel = entity.getVelocityY();


        // if(entity.getX() < 800)
        // cout << "TEST: " << entity.getY2() << endl;
        if (hitSpike(karta,entity)) {
            if (!entity.isDamaged()) {
                //	 if (entity.getY2() < 800)

                entity.setHP(0);
                entity.setDead(true);
            }
        }

        int x1 = entity.getX() / 20;
        int x2 = entity.getX2() / 20;
        int x3 = (x2 + x1) / 2;


        int xtmp = (entity.getX2() + xVel) / 20;

        int y1 = (entity.getY2()) / 20;
        int y2 = (entity.getY2() + 1) / 20;
        int y3 = (entity.getY()) / 20;

        int xSpec1 = (entity.getX2() - 10) / 20;
        int xSpec2 = (entity.getX() + 10) / 20;

        boolean tightSpace = false;

        checkCanLadder(karta, xSpec1, xSpec2, y2, y3);

        checkExitLadder(karta, xSpec1, xSpec2, y1, y2, y3, yVel);
        int realYVel = checkYCollision(karta, x1, x3, x2, entity.getY(), entity.getY2(), yVel);

        if (onLadder) {

            realYVel = entity.getVelocityY();

            if (karta[xSpec1 + y2 * bw] == 2 || karta[xSpec1 + y3 * bw] == 2) {
                entity.setX(xSpec1 * 20 - 8);
            }
            else if (karta[xSpec2 + y2 * bw] == 2 || karta[xSpec2 + y3 * bw] == 2) {
                entity.setX(xSpec2 * 20 - 8);
            }
            x1 = entity.getX() / 20;
            x2 = entity.getX2() / 20;
            x3 = (x2 + x1) / 2;
            realYVel = checkYCollision(karta, x3, x3, x3, entity.getY(), entity.getY2(), yVel);

        }
        else if (!onLadder) {

            if ((karta[x2 + y2 * bw] == 0 || (karta[x2 + y2 * bw] == 2)) &&
                    (karta[x1 + y2 * bw] == 0 || (karta[x1 + y2 * bw] == 2)) &&
                    (karta[x3 + y2 * bw] == 0 || (karta[x3 + y2 * bw] == 2))) {

                boolean inLadder = isInLadder(karta, x1, x3, x2, entity.getY2());

                inAir = true;

                if (!inLadder && (karta[x2 + y2 * bw] == 2 || karta[x1 + y2 * bw] == 2
                        || karta[x3 + y2 * bw] == 2)) {
                    inAir = false;
                }

                if (realYVel > 0)
                    if (!inLadder) {
                        if (karta[x2 + y2 * bw] == 2 || karta[x1 + y2 * bw] == 2 || karta[x3 + y2 * bw] == 2) {
                            realYVel = 0;
                            inAir = false;
                        }

                        int ySpec = (entity.getY2() + realYVel) / 20;
                        if (karta[x2 + ySpec * bw] == 2 || karta[x1 + ySpec * bw] == 2 || karta[x3 + ySpec * bw] == 2) {
                            realYVel = 0;
                            inAir = false;
                            while ((karta[x2 + y2 * bw] != 2 && karta[x1 + y2 * bw] != 2 && karta[x3 + y2 * bw] != 2)) {

                                entity.addXY(0, 1);

                                y2 = (entity.getY2() + 1) / 20;
                            }

                        }

                    }

            }
            else {
                inAir = false;
            }

            if (karta[x3 + y1 * bw] == 2 && karta[x1 + y3 * bw] == 1 && karta[x2 + y3 * bw] == 1) {
                realYVel = entity.getVelocityY();
                inAir = true;
                tightSpace = true;
            }
            if (karta[x3 + y1 * bw] == 2 && karta[x1 + y1 * bw] == 1 && karta[x2 + y1 * bw] == 1) {
                realYVel = entity.getVelocityY();
                inAir = true;
                tightSpace = true;
            }
        }


        entity.addXY(0, realYVel);

        if (entity.getVelocityY() > 0 && entity.getVelocityY() > realYVel)
            yPathBlocked = true;
        else if (entity.getVelocityY() < 0 && entity.getVelocityY() < realYVel)
            yPathBlocked = true;
        else
            yPathBlocked = false;



        y1 = (entity.getY2()) / 20;
        y3 = (entity.getY()) / 20;

        if (xVel > 0) {
            xPathBlocked = true;
            int x5 = (entity.getX2() + xVel) / 20;
            if (karta[x5 + y1 * bw] != 1 && karta[x5 + y3 * bw] != 1) {

                int yp = 20 - (entity.getY2() - (((entity.getY2()) / 20) * 20));
                if (!(yp > 0 && yp < 10 && karta[x5 + y1 * bw] == 6)
                        && !(yVel < 0 && karta[x5 + y1 * bw] == 6)) {
                    entity.addXY(entity.getVelocityX(), 0);
                    xPathBlocked = false;

                }

            }
        }
        else if (xVel < 0) {
            xPathBlocked = true;
            int x5 = (entity.getX() + xVel) / 20;
            if (karta[x5 + y1 * bw] != 1 && karta[x5 + y3 * bw] != 1) {

                int yp = 20 - (entity.getY2() - (((entity.getY2()) / 20) * 20));
                if (!(yp > 0 && yp < 10 && karta[x5 + y1 * bw] == 6)
                        && !(yVel < 0 && karta[x5 + y1 * bw] == 6)) {

                    entity.addXY(entity.getVelocityX(), 0);
                    xPathBlocked = false;

                }

            }
        }

        if (!onLadder && !tightSpace)
            while ((karta[x2 + y1 * bw] == 1 || karta[x2 + y3 * bw] == 1) && karta[x1 + y1 * bw] != 1) {
                entity.setX(entity.getX() - 1);
                x2 = entity.getX2() / 20;

            }
        if (!onLadder && !tightSpace)
            while ((karta[x1 + y1 * bw] == 1 || karta[x1 + y3 * bw] == 1) && karta[x2 + y1 * bw] != 1) {
                entity.setX(entity.getX() + 1);
                x1 = entity.getX() / 20;
            }
    }

    @Override
    public boolean isInAir(){
        return onLadder;
    }

    @Override
    public boolean isOnLadder(){
        return inAir;
    }

    @Override
    public boolean canUseLadderUP() {
        return canLadderUP;
    }

    @Override
    public boolean canUseLadderDOWN() {
        return canLadderDOWN;
    }

    @Override
    public void enterLadder() {
        onLadder = true;
    }

    @Override
    public void exitLadder() {
        onLadder = false;
    }

    @Override
    public boolean isXPathBlocked() {
        return xPathBlocked;
    }

    @Override
    public boolean isYPathBlocked() {
        return yPathBlocked;
    }

    private boolean isInLadder(char[] karta, int xLeft, int xMid, int xRight, int yDown){
        int y1 = (yDown - 1) / 20;
        if (karta[xRight + y1 * bw] == 2 || karta[xLeft + y1 * bw] == 2 || karta[xMid + y1 * bw] == 2) {
            return true;
        }
        return false;
    }

    private int checkYCollision(char[] karta, int xLeft, int xMid, int xRight, int yUp, int yDown, int yVel){
        int yNewUp = (yUp + yVel) / 20;
        int yNewDown = (yDown + yVel) / 20;
        int tmpVel = yVel;
        if (yVel < 0) {

            for (int i = yVel; i <= 0 && tmpVel != 0; i++) {

                if (karta[xRight + yNewUp * bw] != 1 && karta[xLeft + yNewUp * bw] != 1 && karta[xMid + yNewUp * bw] != 1)
                {
                    return tmpVel;

                }

                tmpVel++;
                yNewUp = (yUp + tmpVel) / 20;
            }
        }
        else if (yVel > 0) {
            for (int i = yVel; i >= 0 && tmpVel != 0; i--) {
                if (karta[xRight + yNewDown * bw] != 1 && karta[xLeft + yNewDown * bw] != 1 && karta[xMid + yNewDown * bw] != 1)
                {
                    return tmpVel;
                }

                tmpVel--;
                yNewDown = (yDown + tmpVel) / 20;
            }
        }

        return tmpVel;
    }

    private void checkExitLadder(char[] karta, int xLeft, int xRight, int yDown, int yDownP, int yUp, int yVel){
        if (!onLadder)
            return;

        if (yVel < 0) {
            if (karta[xLeft + yDownP * bw] == 0 && karta[xRight + yDownP * bw] == 0) {
                onLadder = false;
                //cout << "TEST: " << yDownP << endl;

            }
        }
        else if (yVel > 0) {
            if ((karta[xLeft + yUp * bw] != 2 && karta[xRight + yUp * bw] != 2 &&
                    (karta[xLeft + yDownP * bw] != 2 && karta[xRight + yDownP * bw] != 2))
                    || (karta[xLeft + yDownP * bw] == 1 || karta[xRight + yDownP * bw] == 1)
                    ) {
                onLadder = false;
            }
        }
    }

    private void checkCanLadder(char[] karta, int xLeft, int xRight, int yDown, int yUp){
        if (karta[xLeft + yDown * bw] == 2 || karta[xRight + yDown * bw] == 2) {
            canLadderDOWN = true;
        }
        else
            canLadderDOWN = false;

        if (karta[xLeft + yUp * bw] == 2 || karta[xRight + yUp * bw] == 2) {
            canLadderUP = true;
        }
        else
            canLadderUP = false;
    }

    private boolean inAir = false;
    private boolean onLadder = false;
    private boolean wantToExitLadder = false;
    private boolean canLadderUP = false;
    private boolean canLadderDOWN = false;

    private boolean xPathBlocked;
    private boolean yPathBlocked;

}
