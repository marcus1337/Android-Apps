package com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES;

import android.graphics.Point;
import android.util.Log;

import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Player;
import com.fullrune.areashiftertwo.MODEL.Pieces.Unit;
import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

import java.util.ArrayList;

public class EnemyPhysics1 extends Physics {

    public EnemyPhysics1(Unit unit) {
        super(unit);
    }


    private boolean forceOneWay;

    @Override
    public void moveNormal(int[][] map) {

        int rh = unit.getRadius() / 2;
        Point pleft = new Point(unit.getX() - rh, unit.getY());
        Point pright = new Point(unit.getX() + rh, unit.getY());
        Point pup = new Point(unit.getX(), unit.getY() - rh);
        Point pdown = new Point(unit.getX(), unit.getY() + rh);
        ArrayList<Point> leftSide = getSideOfSqr(new Point(pleft.x, pleft.y - rh), new Point(pleft.x, pleft.y + rh));
        ArrayList<Point> rightSide = getSideOfSqr(new Point(pright.x, pright.y - rh), new Point(pright.x, pright.y + rh));
        ArrayList<Point> upSide = getSideOfSqr(new Point(pup.x - rh, pup.y), new Point(pup.x + rh, pup.y));
        ArrayList<Point> downSide = getSideOfSqr(new Point(pdown.x - rh, pdown.y), new Point(pdown.x + rh, pdown.y));
        ArrayList<ArrayList<Point>> arrays = new ArrayList<ArrayList<Point>>();
        arrays.add(leftSide);
        arrays.add(rightSide);
        arrays.add(upSide);
        arrays.add(downSide);

        SquareInfo squareInfo = new SquareInfo();

      /*  ArrayList<Point> square = new ArrayList<Point>();
        square.addAll(leftSide);
        square.addAll(rightSide);
        square.addAll(upSide);
        square.addAll(downSide);*/

        int xSteps = unit.getxVel();
        int ySteps = unit.getyVel();
        int oneX = (xSteps == 0) ? 0 : (xSteps / Math.abs(xSteps));
        int oneY = (ySteps == 0) ? 0 : (ySteps / Math.abs(ySteps));

        if(forceOneWay){
            squareInfo.calcScores(leftSide,rightSide,upSide,downSide,map);
            if(!squareInfo.anyHit()){
               forceOneWay = false;
            }
            return;
        }

        while ((xSteps != 0 || ySteps != 0)) {

            squareInfo.calcScores(leftSide,rightSide,upSide,downSide,map);
            if(squareInfo.anyHit()){
                //Log.i("TESTABC", " HEY " + squareInfo.bestSide);
                int bestSide = squareInfo.bestSide;
                if(bestSide > 0){
                    if(bestSide == 1)//left
                        unit.setxVel(Math.abs(unit.getxVel()));
                    if(bestSide == 2)//right
                        unit.setxVel(-Math.abs(unit.getxVel()));
                    if(bestSide == 3)//up
                        unit.setyVel(Math.abs(unit.getyVel()));
                    if(bestSide == 4)//down
                        unit.setyVel(-Math.abs(unit.getyVel()));
                    return;
                }
                if(bestSide == 0){
                 //   int movX = (unit.getxVel() == 0) ? 0 : -(unit.getxVel()/Math.abs(unit.getxVel()));
                  //  int movY = (unit.getxVel() == 0) ? 0 : -(unit.getyVel()/Math.abs(unit.getyVel()));
                    unit.setxVel(-unit.getxVel());
                    unit.setyVel(-unit.getyVel());
                    forceOneWay = true;
                    return;
                }
            }

            moveArraysStep(arrays, oneX, oneY);
            xSteps = moveToZero(xSteps);
            ySteps = moveToZero(ySteps);
        }

      /*  boolean stopCheckY = false;
        boolean stopCheckX = false;

        HitInfo leftHits = new HitInfo();
        HitInfo rightHits = new HitInfo();
        HitInfo upHits = new HitInfo();
        HitInfo downHits = new HitInfo();

        while ((xSteps != 0 || ySteps != 0)) {

            if (!stopCheckY){
                boolean leftHit = isArrayBlocked(leftSide, map, leftHits);
                boolean rightHit = isArrayBlocked(rightSide, map, rightHits);
                if (leftHits.allHit || rightHits.allHit) {
                    //unit.setyVel(unit.getyVel() * (-1));
                    stopCheckY = true;
                }else if(leftHit){
                    if(leftHits.leftHit){
                        unit.setyVel(Math.abs(unit.getyVel()));
                    }else
                        unit.setyVel(-Math.abs(unit.getyVel()));
                    stopCheckY = true;
                }else if(rightHit){
                    if(rightHits.leftHit){
                        unit.setyVel(Math.abs(unit.getyVel()));
                    }else
                        unit.setyVel(-Math.abs(unit.getyVel()));
                    stopCheckY = true;
                }
            }
            if (!stopCheckX){
                boolean upHit = isArrayBlocked(upSide, map, upHits);
                boolean downHit = isArrayBlocked(downSide, map, downHits);
                if (upHit || downHit) {
                    unit.setxVel(unit.getxVel() * (-1));
                    stopCheckX = true;
                }

            }

            moveArrayValuesStep(arrays, oneX, oneY);
            xSteps = moveToZero(xSteps);
            ySteps = moveToZero(ySteps);

        }*/

    }


    private void moveArrayValuesStep(ArrayList<Point> points, int xs, int ys) {
            for (Point p : points) {
                p.x += xs;
                p.y += ys;
            }
    }

    private void moveArraysStep(ArrayList<ArrayList<Point>> arrays, int xs, int ys) {
        for (ArrayList<Point> array : arrays) {
            for (Point p : array) {
                p.x += xs;
                p.y += ys;
            }
        }
    }

  /*  private boolean isArrayBlocked(ArrayList<Point> points, int[][] map, HitInfo hitInfo) {
        boolean result = false;
        int hitCounter = 0;
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (UnitHelp.isBlockedEnemy(map, p.x, p.y)) {
                if(i < points.size()/2){
                    hitInfo.leftHit = true;
                }
                result = true;
                hitCounter++;
            }
        }
        if(hitCounter == points.size()-1){
            hitInfo.allHit = true;
        }

        return result;
    }*/

    private ArrayList<Point> getSideOfSqr(Point sp, Point ep) {
        ArrayList<Point> result = new ArrayList<Point>();
        if (sp.x == ep.x && sp.y == ep.y) {
            result.add(new Point(sp.x, sp.y));
            return result;
        }
        while (sp.x != ep.x || sp.y != ep.y) {
            result.add(new Point(sp.x, sp.y));
            pointStepCloser(sp, ep);
          //  Log.i("ChangesP", "X: " + sp.x + " Y: " + sp.y + "  ("+ep.x +"," + ep.y +")");
        }
        result.add(new Point(sp.x, sp.y));
        return result;
    }

    private int moveToZero(int num) {
        if (num > 0)
            num--;
        if (num < 0)
            num++;
        return num;
    }

    private void pointStepCloser(Point sp, Point ep) {
        if (sp.x > ep.x)
            sp.x--;
        if (sp.x < ep.x)
            sp.x++;
        if (sp.y > ep.y)
            sp.y--;
        if (sp.y < ep.y)
            sp.y++;
    }

    private class SquareInfo{
        public int upScore, downScore, leftScore,rightScore, bestScore;
        public int bestSides, bestSide;
        private int totalHits;

        private void calcBest(){ //at most 2 best sides for simplicity
            if(!anyHit()){
                bestSides = 0;
                bestSide = 0;
                return;
            }
            if(upScore > downScore && upScore > leftScore && upScore > rightScore){
                bestSides = 1;
                bestSide = 3;
                return;
            }
            if(downScore > upScore && downScore > leftScore && downScore > rightScore){
                bestSides = 1;
                bestSide = 4;
                return;
            }
            if(leftScore > downScore && upScore < leftScore && leftScore > rightScore){
                bestSides = 1;
                bestSide = 1;
                return;
            }
            if(rightScore > upScore && rightScore > leftScore && downScore < rightScore){
                bestSides = 1;
                bestSide = 2;
                return;
            }

        }

       /* private void calcTwo(){
            if(bestSides == 1){
                bestScore = Math.max(Math.max(leftScore,rightScore), Math.max(upScore,downScore));
                int temp = 0;
                if(bestScore >= rightScore){
                    temp++;
                }
                if(bestScore >= leftScore){
                    temp++;
                }
                if(bestScore >= upScore){
                    temp++;
                }
                if(bestScore >= downScore){
                    temp++;
                }
                if(temp >= 2){
                    bestSides = 2;
                }
            }
        }*/

        public boolean anyHit(){
            return upScore > 0 || downScore > 0 || leftScore > 0 || rightScore > 0;
        }

        public void calcScores(ArrayList<Point> leftSide, ArrayList<Point> rightSide,
                               ArrayList<Point> upSide, ArrayList<Point> downSide, int[][] map){
            totalHits = 0;
            upScore = calcHitPoints(upSide, map);
            downScore = calcHitPoints(downSide, map);
            leftScore = calcHitPoints(leftSide, map);
            rightScore = calcHitPoints(rightSide, map);
            calcBest();
            //calcTwo();
        }

        private int calcHitPoints(ArrayList<Point> points, int[][] map){
            int result = 0;
            for(Point p : points){
                if(UnitHelp.isBlockedEnemy(map, p.x,p.y)){
                    result++;
                    totalHits++;
                }
            }
            return result;
        }

    }

/*
    private class HitInfo{
        public boolean leftHit;
        public boolean allHit;

        public HitInfo(){
        }

    }*/

}
