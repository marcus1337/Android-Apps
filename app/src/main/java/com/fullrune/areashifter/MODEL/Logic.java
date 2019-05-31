package com.fullrune.areashifter.MODEL;

import com.fullrune.areashifter.MODEL.PLAYER.Input;
import com.fullrune.areashifter.MODEL.PLAYER.Player;
import com.fullrune.areashifter.MODEL.Pieces.Enemy;
import com.fullrune.areashifter.MODEL.Pieces.Line;
import com.fullrune.areashifter.MODEL.Pieces.Point;
import com.fullrune.areashifter.MODEL.Pieces.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Marcus on 2017-05-03.
 */

public class Logic implements Serializable {

    private Player player;
    private int w, h;
    private ArrayList<Line> safeLines;
    private ArrayList<Line> anyLines;
    private ArrayList<Point> filledPoints;
    private ArrayList<Enemy> enemies;
    private int[][] gameB;
    private Model model;
    private int standardRadius;
    private static final long serialVersionUID = -29238982928391L;

    public void setMap(int[][] tmpMap){
        gameB = tmpMap;
    }

    public Logic(int[][] gameB, int w, int h, Player player, ArrayList<Line> safeLines,
                 ArrayList<Line> anyLines, ArrayList<Point> filledPoints,
                 ArrayList<Enemy> enemies, Model model, int standardRadius) {
        this.w = w;
        this.h = h;
        this.player = player;
        this.safeLines = safeLines;
        this.anyLines = anyLines;
        this.filledPoints = filledPoints;
        this.enemies = enemies;
        this.gameB = gameB;
        this.model = model;
        this.standardRadius = standardRadius;
    }

    private static boolean circleCollision(Unit u1, Unit u2){
        return Math.pow((u2.getX()-u1.getX()),2) + Math.pow((u1.getY()-u2.getY()),2) <= Math.pow((u1.getRadius()+u2.getRadius()),2);
    }

    private static boolean circleLineCollision(Unit u, Line line){
        int left = u.getX()-u.getRadius();
        int right = u.getX()+u.getRadius();
        int up = u.getY()-u.getRadius();
        int down = u.getY()+u.getRadius();

        if(Line.isVertical(line)){
            if(left < line.getX() && right > line.getX() && up < line.maxYP() && down > line.minYP())
                return true;
        }else{
            if(up < line.getY() && down > line.getY() && left < line.maxXP() && right > line.minXP())
                return true;
        }
        return false;
    }

    public boolean collision(Enemy e){
        if(circleCollision(e, player)){
            return true;
        }else{
            ArrayList<Line> riskyLines = player.getRiskLines();
            for(Line l: riskyLines){
                if(circleLineCollision(e,l)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPlayerOnline() {
        int x = player.getX();
        int y = player.getY();
        if (gameB[x][y] == 2)
            return true;
        //Log.i("huh333", "x: "+ x + " y: " + y + " MAP: " + gameB[x][y] );
        return false;
    }

    public boolean isPointOnLine(int x, int y) {
        if (!inBounds(x, y))
            return false;
        return gameB[x][y] == 2;
    }

    public boolean inBounds(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h)
            return false;
        return true;
    }

    private boolean isAreaFree(int x, int y) {
        for (Unit u : enemies) {
            Point p = findFirstUpWall(u);
            if (p != null)
                if (findTarget(p.getX(), p.getY(), x, y))
                    return false;
        }
        return true;
    }

    private Point findFirstUpWall(Unit u) {
        int x = u.getX();
        int y = u.getY();

        if(!inBounds(x,y) || gameB[x][y] == 3)
            return null;

        while (gameB[x][y] != 2) {
            y--;
        }

        return new Point(x, y + 1);
    }

    //om blockad i början blir det buggat
    private boolean findTarget(int startX, int startY, int goalX, int goalY) {
        int x = startX, y = startY;
        boolean checked = false;
        Input dir = Input.MOVE_LEFT;

        while (true) {
            // Log.i("TESTA44", "x: " + x + " y: " +y + " startX:" + startX + " startY: " + startY);

            if (x == startX && y == startY && checked) {
                return false;
            } else if (x == startX && y == startY) {
                checked = true;
            }
            if (x == goalX && y == goalY)
                return true;

            switch (dir) {
                case MOVE_UP:
                    if (gameB[x][y - 1] == 2) {
                        dir = Input.MOVE_LEFT;
                    } else if (gameB[x + 1][y] != 2) {
                        dir = Input.MOVE_RIGHT;
                        x++;
                    } else {
                        y--;
                    }

                    break;
                case MOVE_DOWN:
                    if (gameB[x][y + 1] == 2) {
                        dir = Input.MOVE_RIGHT;
                    } else if (gameB[x - 1][y] != 2) {
                        dir = Input.MOVE_LEFT;
                        x--;
                    } else {
                        y++;
                    }

                    break;
                case MOVE_LEFT:
                    if (gameB[x - 1][y] == 2) {
                        dir = Input.MOVE_DOWN;
                    } else if (gameB[x][y - 1] != 2) {
                        dir = Input.MOVE_UP;
                        y--;
                    } else {
                        x--;
                    }

                    break;
                case MOVE_RIGHT:
                    if (gameB[x + 1][y] == 2) {
                        dir = Input.MOVE_UP;
                    } else if (gameB[x][y + 1] != 2) {
                        dir = Input.MOVE_DOWN;
                        y++;
                    } else {
                        x++;
                    }
                    break;
            }

        }


    }

    public boolean playerHitWall() {
        int x2 = player.getX();
        int y2 = player.getY();
        int x1 = x2 + player.getxVel();
        int y1 = y2 + player.getyVel();
        Input dir = player.getDirection();
        Line playerLine = new Line(x1, y1, x2, y2);

        int minX = playerLine.minXP();
        int minY = playerLine.minYP();

        int maxX = playerLine.maxXP();
        int maxY = playerLine.maxYP();

        if (Line.isVertical(playerLine)) {
            for (int i = 0; i < maxY - minY; i++) {
                //Log.i("TEST555", "a: " + minX + " b: " + (minY+i));
                if (inBounds(minX, minY + i) && gameB[minX][minY + i] == 2) {
                    player.setX(minX);
                    player.setY(minY + i);
                    player.getRiskLines().get(0).setX(player.getX());
                    player.getRiskLines().get(0).setY(player.getY());
                    fillArea(true);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < maxX - minX; i++) {
                if (inBounds(minX + i, minY) && gameB[minX + i][minY] == 2) {
                    player.setX(minX + i);
                    player.setY(minY);
                    player.getRiskLines().get(0).setX(player.getX());
                    player.getRiskLines().get(0).setY(player.getY());
                    fillArea(false);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean testPoint(Point p) {
        if (p.getX() == 0) {
            if (gameB[1][p.getY()] == 3) {
                gameB[0][p.getY()] = 3;
                filledPoints.add(p);
                return true;
            }
        } else if (p.getX() == w - 1) {
            if (gameB[w - 2][p.getY()] == 3) {
                gameB[w - 1][p.getY()] = 3;
                filledPoints.add(p);
                return true;
            }
        } else if (p.getY() == 0) {
            if (gameB[p.getX()][1] == 3) {
                gameB[p.getX()][0] = 3;
                filledPoints.add(p);
                return true;
            }
        } else if (p.getY() == h - 1) {
            if (gameB[p.getX()][h - 2] == 3) {
                gameB[p.getX()][h - 1] = 3;
                filledPoints.add(p);
                return true;
            }
        } else if ((gameB[p.getX() - 1][p.getY()] == 3 && gameB[p.getX() + 1][p.getY()] == 3) ||
                (gameB[p.getX()][p.getY() - 1] == 3 && gameB[p.getX()][p.getY() + 1] == 3)) {
            gameB[p.getX()][p.getY()] = 3;
            filledPoints.add(p);
            return true;

        }

        return false;
    }

    private ArrayList<Point> lineToPoints(Line line) {
        ArrayList<Point> points = new ArrayList<Point>();

        int minX = line.minXP();
        int minY = line.minYP();
        int maxX = line.maxXP();
        int maxY = line.maxYP();
        if (Line.isVertical(line)) {
            for (int i = minY; i <= maxY; i++) {
                if (gameB[minX][i] == 2)
                    points.add(new Point(minX, i));
            }
        } else {
            for (int i = minX; i <= maxX; i++) {
                if (gameB[i][minY] == 2)
                    points.add(new Point(i, minY));
            }
        }

        return points;
    }

    private void removeBlues() {
        ArrayList<Point> extraP = new ArrayList<Point>();
        ArrayList<Line> toBeRemoved = new ArrayList<Line>();
        for (Line l : anyLines) {
            ArrayList<Point> points = new ArrayList<Point>();
            points = lineToPoints(l);
            if (points.size() == 0) {
                toBeRemoved.add(l);
            }

            for (Point p : points) {
                if (!testPoint(p)) {
                    extraP.add(p);
                }
            }
        }

        anyLines.removeAll(toBeRemoved);
        for (Point extraPoint : extraP) {
            testPoint(extraPoint);
        }

    }

    private void fillArea(boolean verticalDir) {
        Input dir = player.getDirection();
        player.registerLines();


        if (verticalDir) {
            if (dir == Input.MOVE_UP) {
                // Log.i("what775", "free: " + isAreaFree(player.getX() - 1, player.getY() + 1));
                if (isAreaFree(player.getX() + 1, player.getY() + 1)) {
                    fill(player.getX() + 1, player.getY() + 1);
                } else if (isAreaFree(player.getX() - 1, player.getY() + 1)) {
                    fill(player.getX() - 1, player.getY() + 1);
                }
            }
            if (dir == Input.MOVE_DOWN) {
                if (isAreaFree(player.getX() + 1, player.getY() - 1)) {
                    fill(player.getX() + 1, player.getY() - 1);
                } else if (isAreaFree(player.getX() - 1, player.getY() - 1)) {
                    fill(player.getX() - 1, player.getY() - 1);
                }
            }

        } else {

            if (dir == Input.MOVE_LEFT) {
                // Log.i("what775", "free: " + isAreaFree(player.getX() - 1, player.getY() + 1));
                if (isAreaFree(player.getX() + 1, player.getY() + 1)) {
                    fill(player.getX() + 1, player.getY() + 1);
                } else if (isAreaFree(player.getX() + 1, player.getY() - 1)) {
                    fill(player.getX() + 1, player.getY() - 1);
                }
            }
            if (dir == Input.MOVE_RIGHT) {
                if (isAreaFree(player.getX() - 1, player.getY() - 1)) {
                    fill(player.getX() - 1, player.getY() - 1);
                } else if (isAreaFree(player.getX() - 1, player.getY() + 1)) {
                    fill(player.getX() - 1, player.getY() + 1);
                }
            }
        }

        removeBlues();
    }

    private void fill(int x, int y) {

        boolean up1 = false;
        boolean down1 = false;

        boolean up2 = false;
        boolean down2 = false;

        for (int i = y; gameB[x][i] == 0; i++) {
            for (int j = x; gameB[j][i] == 0; j++) {
                // if (gameB[j][i] != 3) {
                gameB[j][i] = 3;
                filledPoints.add(new Point(j, i));
                if (gameB[j][i - 1] != 2 && up1) {
                    up1 = false;
                    fill(j, i - 1);
                } else if (gameB[j][i - 1] == 2) {
                    up1 = true;
                }
                if (gameB[j][i + 1] != 2 && down1) {
                    down1 = false;
                    fill(j, i + 1);
                } else if (gameB[j][i + 1] == 2) {
                    down1 = true;
                }

                // }
            }
            for (int j = x - 1; gameB[j][i] == 0; j--) {
                if (gameB[j][i] != 3) {
                    gameB[j][i] = 3;
                    filledPoints.add(new Point(j, i));

                    if (gameB[j][i - 1] != 2 && up2) {
                        up2 = false;
                        fill(j, i - 1);
                    } else if (gameB[j][i - 1] == 2) {
                        up2 = true;
                    }
                    if (gameB[j][i + 1] != 2 && down2) {
                        down2 = false;
                        fill(j, i + 1);
                    } else if (gameB[j][i + 1] == 2) {
                        down2 = true;
                    }

                }
            }
        }
        up1 = up2 = down1 = down2 = false;

        for (int i = y - 1; gameB[x][i] == 0; i--) {
            for (int j = x; gameB[j][i] == 0; j++) {
                // if (gameB[j][i] != 3) {
                gameB[j][i] = 3;
                filledPoints.add(new Point(j, i));
                if (gameB[j][i - 1] != 2 && up1) {
                    up1 = false;
                    fill(j, i - 1);
                } else if (gameB[j][i - 1] == 2) {
                    up1 = true;
                }
                if (gameB[j][i + 1] != 2 && down1) {
                    down1 = false;
                    fill(j, i + 1);
                } else if (gameB[j][i + 1] == 2) {
                    down1 = true;
                }
                // }
            }
            for (int j = x - 1; gameB[j][i] == 0; j--) {
                //  if (gameB[j][i] != 3) {
                gameB[j][i] = 3;
                filledPoints.add(new Point(j, i));
                if (gameB[j][i - 1] != 2 && up2) {
                    up2 = false;
                    fill(j, i - 1);
                } else if (gameB[j][i - 1] == 2) {
                    up2 = true;
                }
                if (gameB[j][i + 1] != 2 && down2) {
                    down2 = false;
                    fill(j, i + 1);
                } else if (gameB[j][i + 1] == 2) {
                    down2 = true;
                }

                //  }
            }
        }

    }

    public void lineToGameB(Line line) {
        int miY = line.minYP();
        int maY = line.maxYP();
        int miX = line.minXP();
        int maX = line.maxXP();
        if (Line.isVertical(line)) {
            for (int i = 0; i <= maY - miY; i++) {
                gameB[miX][miY + i] = 2;
            }
        } else {
            for (int i = 0; i <= maX - miX; i++) {
                gameB[miX + i][miY] = 2;
            }
        }

    }

    private int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public void generateEnemy() {
        int offset = standardRadius * 5;
        int randX = randInt(offset, w - offset);
        int randY = randInt(offset, h - offset);
        int speedX = 0;
        int speedY = 0;
        while (speedX == 0 || speedY == 0) {
            speedX = randInt(-1, 1);
            speedY = randInt(-1, 1);
        }

        Enemy en = new Enemy(randX, randY, standardRadius, model);
        en.setxVel(speedX);
        en.setyVel(speedY);

        enemies.add(en);
    }

    public boolean isMovePossible() {
        switch (player.getDirection()) {
            case MOVE_DOWN:
                // Log.i("UHMM123",  " a: " + (player.getY()));
                if (player.getY() + 1 >= h)
                    return false;
                if (gameB[player.getX()][player.getY() + 1] == 3)
                    return false;

                break;
            case MOVE_UP:
                //Log.i("UHMM123",  " a: " + (player.getY() - 1));
                if (player.getY() - 1 < 0)
                    return false;
                if (gameB[player.getX()][player.getY() - 1] == 3)
                    return false;

                break;
            case MOVE_LEFT:
                if (player.getX() - 1 < 0)
                    return false;
                if (gameB[player.getX() - 1][player.getY()] == 3)
                    return false;

                break;
            case MOVE_RIGHT:
                if (player.getX() + 1 >= w)
                    return false;
                if (gameB[player.getX() + 1][player.getY()] == 3)
                    return false;

                break;
        }

        return true;
    }

    public boolean isMoveRisky() {
        int x2 = player.getX();
        int y2 = player.getY();
        int x1 = x2 + player.getxVel();
        int y1 = y2 + player.getyVel();
        //Log.i("DIR1122", " X: " + player.getxVel() + " Y: " + player.getyVel());
        // Log.i("GHHEHEHEHE", "x2: " + x2 + " y2: " + y2 + " G: " + gameB[x2][y2] + "  x1: " + x1 + " y1: " + y1 + " G2: " + gameB[x1][y1]);
        if (gameB[x1][y1] == 0) {
            return true;
        }
        return false;
    }

}
