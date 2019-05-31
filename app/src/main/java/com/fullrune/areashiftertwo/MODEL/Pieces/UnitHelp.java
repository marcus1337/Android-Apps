package com.fullrune.areashiftertwo.MODEL.Pieces;

import android.util.Log;

import java.util.ArrayList;

import com.fullrune.areashiftertwo.MODEL.MapValue;

import static com.fullrune.areashiftertwo.MODEL.MapValue.*;
import static com.fullrune.areashiftertwo.MODEL.MapValue.EDGE;

public class UnitHelp {

    public static boolean isUnitCollision(Unit u1, Unit u2) {
        if (Math.pow((u1.getX() - u2.getX()), 2) + Math.pow((u2.getY() - u1.getY()), 2) <= Math.pow((u2.getRadius() + u1.getRadius()), 2))
            return true;
        return false;
    }

    public static boolean isWallCollision(int[][] map, Unit unit) {
        for (Point p : getCircleref(unit, 0, 360)) {
            if (isPointInside(map, p.x, p.y)) {
                if (map[p.x][p.y] == WALL.getValue())
                    return true;
            }
        }
        return false;
    }

    public static boolean isLineCollision(int[][] map, Unit unit) {
        for (Point p : getCircleref(unit, 0, 360)) {
            if (isPointInside(map, p.x, p.y)) {
                if (map[p.x][p.y] == LINE.getValue())
                    return true;
            }
        }
        return false;
    }

    public static ArrayList<Point> getShortCircleref(Unit unit, int begin, int end) {
        ArrayList<Point> tmp = new ArrayList<Point>();
        int radius = unit.getRadius();
        int rh = radius / 2;
        int extraStep = 1;
        if (rh <= 5) {
            extraStep = 45;
        }

        for (int i = begin; i <= end; i += extraStep) {
            int tmpx = (int) Math.round(unit.getRadius() * Math.cos(Math.toRadians(i))) + unit.getX();
            int tmpy = (int) Math.round(unit.getRadius() * Math.sin(Math.toRadians(i))) + unit.getY();
            tmp.add(new Point(tmpx, tmpy));
        }
        return tmp;
    }

    public static ArrayList<Point> getCircleref(Unit unit, int begin, int end) {
        ArrayList<Point> tmp = new ArrayList<Point>();
        for (int i = begin; i < end; i++) {
            int tmpx = (int) Math.round(unit.getRadius() * Math.cos(Math.toRadians(i))) + unit.getX();
            int tmpy = (int) Math.round(unit.getRadius() * Math.sin(Math.toRadians(i))) + unit.getY();
            tmp.add(new Point(tmpx, tmpy));
        }
        return tmp;
    }

    public static boolean isPointMapType(int[][] map, int px, int py, MapValue mapValue) {
        return (isPointInside(map, px, py) && map[px][py] == mapValue.getValue());
    }

    public static boolean isPointInside(int[][] map, int px, int py) {
        if (px < 0 || py < 0)
            return false;
        if (px >= map.length || py >= map[0].length)
            return false;
        return true;
    }

    public static boolean isBlockedEnemy(int[][] map, int x, int y) {
        if (isPointInside(map, x, y)) {
            if ((map[x][y] == EDGE.getValue()) || (map[x][y] == WALL.getValue())) {
                return true;
            }
        }

        return false;
    }

    public static Point detectEnemyMapHit1(Point start, Point end, int[][] map) {
        int x1 = start.x;
        int y1 = start.y;
        int x2 = end.x;
        int y2 = end.y;
        if (x1 == x2) {
            for (int i = y1; i != y2; i = (i > y2) ? (i - 1) : (i + 1)) {
                if (isBlockedEnemy(map, x1, i)) {
                    return new Point(x1, i);
                }
            }
        } else if (y1 == y2) {
            for (int i = x1; i != x2; i = (i > x2) ? (i - 1) : (i + 1)) {
                if (isBlockedEnemy(map, i, y1)) {
                    return new Point(i, y1);
                }
            }
        } else {
            float k = (float) (y2 - y1) / (float) (x2 - x1);
            float m = y1 - k * x1;

            int diffX = Math.abs(x1 - x2);
            int diffY = Math.abs(y1 - y2);

            if (diffX > diffY) {
                for (int i = y1; i != y2; i = (i > y2) ? i - 1 : i + 1) {
                    int tmpx = Math.round((i - m) / k);

                    if (isBlockedEnemy(map, tmpx, i)) {
                        return new Point(tmpx, i);
                    }
                }
            } else {
                for (int i = x1; i != x2; ) {
                    int tmpy = Math.round(k * i + m);

                    if (isBlockedEnemy(map, i, tmpy)) {
                        return new Point(i, tmpy);
                    }

                    if (i < x2)
                        i++;
                    else i--;
                }
            }
        }
        if (isBlockedEnemy(map, end.x, end.y)) {
            return end;
        }

        return null;
    }

    public static Point detectMapHit(Point start, Point end, int[][] map, ArrayList<Point> track, MapValue mapValue) {
        int x1 = start.x;
        int y1 = start.y;
        int x2 = end.x;
        int y2 = end.y;
        if (x1 == x2) { // no k value
            for (int i = y1; i != y2; ) {

                if (isPointInside(map, x1, i)) {
                    if (map[x1][i] == mapValue.getValue()) {
                        return new Point(x1, i);
                    }

                    if (track != null)
                        track.add(new Point(x1, i));
                }

                if (i < y2)
                    i++;
                else i--;
            }

        } else {
            float k = (float) (y2 - y1) / (float) (x2 - x1);
            float m = y1 - k * x1;

            //Log.i("TEST: ", " k " +k + " m " + m + "  y1: " + y1 + " y2 " + y2 + " x1 " + x1 + " x2 " + x2);

            for (int i = x1; i != x2; ) {
                int tmpy = Math.round(k * i + m);

                if (isPointInside(map, i, tmpy)) {
                    if (map[i][tmpy] == mapValue.getValue()) {
                        return new Point(i, tmpy);
                    }

                    if (track != null)
                        track.add(new Point(i, tmpy));
                }

                if (i < x2)
                    i++;
                else i--;
            }

        }

        if (isPointInside(map, end.x, end.y)) {
            if (map[end.x][end.y] == mapValue.getValue()) {
                return end;
            }
        }

        return null;
    }

    private static int countCloseEdges(int[][] map, int x, int y) {
        int counter = 0;
        if (isPointInside(map, x + 1, y) && map[x + 1][y] == EDGE.getValue()) {
            counter++;
        }
        if (isPointInside(map, x - 1, y) && map[x - 1][y] == EDGE.getValue()) {
            counter++;
        }
        if (isPointInside(map, x, y + 1) && map[x][y + 1] == EDGE.getValue()) {
            counter++;
        }
        if (isPointInside(map, x, y - 1) && map[x][y - 1] == EDGE.getValue()) {
            counter++;
        }
        return counter;
    }

    public static Point detectManyEdges(Point start, Point end, int[][] map) { //* Can only move straight up, down, left or right *//
        int tmpx = start.x;
        int tmpy = start.y;

        while (tmpx != end.x || tmpy != end.y) {

            int counter = countCloseEdges(map, tmpx, tmpy);

            if (counter > 2) {
                return new Point(tmpx, tmpy);
            }

            if (tmpx > end.x)
                tmpx--;
            if (tmpx < end.x)
                tmpx++;
            if (tmpy > end.y)
                tmpy--;
            if (tmpy < end.y)
                tmpy++;
        }
        if (countCloseEdges(map, tmpx, tmpy) > 2)
            return new Point(tmpx, tmpy);

        return null;
    }

    public static boolean isMovePossible(int[][] map, Unit unit) {
        int xDir = unit.getxVel();
        int yDir = unit.getyVel();
        if (xDir != 0)
            xDir = xDir / Math.abs(xDir);
        if (yDir != 0)
            yDir = yDir / Math.abs(yDir);

        int tmpx = unit.getX() + xDir;
        int tmpy = unit.getY() + yDir;
        if (!isPointInside(map, tmpx, tmpy)
                || map[tmpx][tmpy] == WALL.getValue()) {
            return false;
        }
        return true;
    }

    public static boolean isMoveRisky(int[][] map, Unit unit) {
        int xDir = unit.getxVel();
        int yDir = unit.getyVel();
        if (xDir != 0)
            xDir = xDir / Math.abs(xDir);
        if (yDir != 0)
            yDir = yDir / Math.abs(yDir);

        int tmpx = unit.getX() + xDir;
        int tmpy = unit.getY() + yDir;
        if (map[tmpx][tmpy] == EMPTY.getValue()) {
            return true;
        }
        return false;
    }

    public static Point walkDetectCrossing(int[][] map, Unit unit) {
        int x1 = unit.getX();
        int y1 = unit.getY();
        int x2 = unit.getX() + unit.getxVel();
        int y2 = unit.getY() + unit.getyVel();

        Point manyEdgesPoint = detectManyEdges(new Point(x1, y1), new Point(x2, y2), map);
        if (manyEdgesPoint != null) {
            return manyEdgesPoint;
        }
        return manyEdgesPoint;
    }

    public static Point walkAlongLine(int[][] map, Unit unit) {
        int x1 = unit.getX();
        int y1 = unit.getY();
        int x2 = unit.getX() + unit.getxVel();
        int y2 = unit.getY() + unit.getyVel();

        ArrayList<Point> track = new ArrayList<Point>();
        Point wallpoint = detectMapHit(new Point(x1, y1), new Point(x2, y2), map, track, WALL);

        if (wallpoint != null) {
            return track.get(track.size() - 1);
        }
        track.clear();
        track.add(new Point(unit.getX(), unit.getY()));
        Point emptyPoint = detectMapHit(new Point(x1, y1), new Point(x2, y2), map, track, EMPTY);
        // Log.i("TEST_Z", "X: " + x1 + " Y: " + y1 + " MAP: " +map[x1][y1]);
        if (emptyPoint != null) {
            return track.get(track.size() - 1);
        }

        if (!isPointInside(map, x2, y2)) {
            if (!track.isEmpty())
                return track.get(track.size() - 1); //should only be possible while on edge of map
            return new Point(x1, y1);
        }

        return null;
    }

}
