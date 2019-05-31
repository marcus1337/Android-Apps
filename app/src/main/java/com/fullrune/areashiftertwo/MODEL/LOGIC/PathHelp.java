package com.fullrune.areashiftertwo.MODEL.LOGIC;
import android.util.Log;

import com.fullrune.areashiftertwo.MODEL.MapValue;
import com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES.Enemy;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Player;
import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.Pieces.Unit;
import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

import java.util.ArrayList;
import static com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input.MOVE_DOWN;
import static com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input.MOVE_LEFT;
import static com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input.MOVE_RIGHT;
import static com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input.MOVE_UP;

public class PathHelp {

    public static int boxLeft, boxRight, boxTop, boxBottom;

    public static ArrayList<Point> getBadPoints(int[][] map, ArrayList<Enemy> enemies){
        ArrayList<Point> points = new ArrayList<Point>();
        for(Unit u : enemies){
            points.add(getUpperPoint(map, u));
        }
        return points;
    }

    private static Point getUpperPoint(int[][] map, Unit unit){
        Point result = new Point(unit.getX(), unit.getY());
        while(!isPathWalkable(map, result.x, result.y)){
            result.y = result.y - 1;
            if(result.y < -1){
                result.y = 0;
                result.x = 0;
                return result;
            }
        }
        result.y = result.y + 1;
        return result;
    }

    public static Point getGreedyFreePoint(Player player,int[][] map, ArrayList<Point> badSpots){
        Point start1 = new Point(0,0);
        Point start2 = new Point(0,0);
        getPathStarts(player, start1, start2);
        Path path1 = new Path(start1);
        Path path2 = new Path(start2);
        Point begin = new Point(player.getX(), player.getY());
        begin.direction = player.getDirRiskEnd();
        Path.findStrategy(begin, path1, path2);
        //Log.i("MEGATEST_D", " X: " + path1.point.x + " y: " + path1.point.y);
        boolean path1ok = checkPathOk(map, path1, badSpots);
        if(path1ok){
            return checkForEmpty(map,path1.point, path1.turnStrategy);
        }else{
            boolean path2ok = checkPathOk(map, path2, badSpots);
            if(path2ok){
                return checkForEmpty(map,path2.point, path2.turnStrategy);
            }
        }
        return null;
    }

    private static Point checkForEmpty(int[][] map ,Point start, Input strategy){

        if(strategy == MOVE_LEFT){
            Point ending = getBestMoveLeft(start.x, start.y, start.direction, map);
            while(!start.isEqual(ending)){
                Point p = checkLeftEmpty(ending.x, ending.y, ending.direction, map);
                if(p != null)
                    return p;
                ending = getBestMoveLeft(ending.x, ending.y, ending.direction, map);
            }
        }else{
            Point ending = getBestMoveRight(start.x, start.y, start.direction, map);
            while(!start.isEqual(ending)){
                Point p = checkRightEmpty(ending.x, ending.y, ending.direction, map);
                if(p != null)
                    return p;
                ending = getBestMoveRight(ending.x, ending.y, ending.direction, map);
            }
        }
        return null;
    }

    //**
    // @return, 0 = NoOK, 1 = Path1OK, 2 = Path2OK, 3 = BothOK
    // */
    public static int scanPaths(Player player,int[][] map, ArrayList<Point> badSpots){
        Point start1 = new Point(0,0);
        Point start2 = new Point(0,0);
        //Log.i("LANDA", "X: " + player.getX() + " Y: " + player.getY() + " D: " + player.getDirRiskEnd());
        getPathStarts(player, start1, start2);
        //  Log.i("LANDB", "PX: " + start1.x + " PY: " + start1.y + " D: " + start1.direction);
        //  Log.i("LANDC", "PX: " + start2.x + " PY: " + start2.y + " D: " + start2.direction);
        Path path1 = new Path(start1);
        Path path2 = new Path(start2);
        Point begin = new Point(player.getX(), player.getY());
        begin.direction = player.getDirRiskEnd();
        Path.findStrategy(begin, path1, path2);
        // Log.i("LANTEST_F", " A: " + path1.turnStrategy + " B: " + path2.turnStrategy);
        //  Log.i("LANDB", "PX: " + path1.point.x + " PY: " + path1.point.y + " D: " + path1.point.direction);
        //Log.i("LANDC", "PX: " + path2.point.x + " PY: " + path2.point.y + " D: " + path2.point.direction);
        boolean path1ok = checkPathOk(map, path1, badSpots);
        boolean path2ok = checkPathOk(map, path2, badSpots);

        if(path1ok && path2ok){
            return 3;
        }else if(path2ok){
            return 2;
        }else if(path1ok){
            return 1;
        }
        return 0;
    }

    private static boolean checkGoodPath(Point point, ArrayList<Point> badSpots, Input strategy){
        if(point.direction == strategy){
            Point tmp = new Point(0,0);
            tmp.x = point.x;
            tmp.y = point.y;
            tmp.y += 1;
            for(Point p : badSpots){
                if(p.isEqual(tmp)){
                    return false;
                }
            }
        }
        return true;
    }

    private static void adjustBoxValues(int x, int y){
        if(x > boxRight)
            boxRight = x;
        if(x < boxLeft)
            boxLeft = x;
        if(y > boxBottom)
            boxBottom = y;
        if(y < boxTop)
            boxTop = y;
    }

    public static boolean checkPathOk(int[][] map ,Path path, ArrayList<Point> badSpots){
        Point start = path.point;
        boxLeft = boxRight = start.x;
        boxTop = boxBottom = start.y;
        Input strategy = path.turnStrategy;
        if(!checkGoodPath(start, badSpots, strategy)){
            return false;
        }
        if(strategy == MOVE_LEFT){
            Point ending = getBestMoveLeft(start.x, start.y, start.direction, map);
            while(!start.isEqual(ending)){
                adjustBoxValues(ending.x, ending.y);
                if(!checkGoodPath(ending, badSpots, strategy)){
                    return false;
                }
                ending = getBestMoveLeft(ending.x, ending.y, ending.direction, map);
            }
        }else{
            Point ending = getBestMoveRight(start.x, start.y, start.direction, map);
            while(!start.isEqual(ending)){
                adjustBoxValues(ending.x, ending.y);
                if(!checkGoodPath(ending, badSpots, strategy)){
                    return false;
                }
                ending = getBestMoveRight(ending.x, ending.y, ending.direction, map);
            }
        }

        return true;
    }

    public static boolean isPointEmpty(int[][] map, int x, int y){
        if(UnitHelp.isPointInside(map, x, y)){
            if(map[x][y] == MapValue.EMPTY.getValue()){
                return true;
            }
        }
        return false;
    }

    public static Point checkLeftEmpty(int x, int y, Input move, int[][] map){
        switch (move){
            case MOVE_UP:
                if(isPointEmpty(map,x-1,y))
                    return new Point(x-1,y, MOVE_LEFT);
                break;
            case MOVE_DOWN:
                if(isPointEmpty(map,x+1,y))
                    return new Point(x+1,y, MOVE_RIGHT);
                break;
            case MOVE_LEFT:
                if(isPointEmpty(map,x,y+1))
                    return new Point(x,y+1, MOVE_DOWN);
                break;
            case MOVE_RIGHT:
                if(isPointEmpty(map,x,y-1))
                    return new Point(x,y-1, MOVE_UP);
                break;
        }
        return null;
    }
    public static Point checkRightEmpty(int x, int y, Input move, int[][] map){
        switch (move){
            case MOVE_UP:
                if(isPointEmpty(map,x+1,y))
                    return new Point(x+1,y, MOVE_RIGHT);
                break;
            case MOVE_DOWN:
                if(isPointEmpty(map,x-1,y))
                    return new Point(x-1,y, MOVE_LEFT);
                break;
            case MOVE_LEFT:
                if(isPointEmpty(map,x,y-1))
                    return new Point(x,y-1, MOVE_UP);
                break;
            case MOVE_RIGHT:
                if(isPointEmpty(map,x,y+1))
                    return new Point(x,y+1, MOVE_DOWN);
                break;
        }
        return null;
    }

    public static boolean isPathWalkable(int[][] map, int x, int y){
        if(UnitHelp.isPointInside(map, x, y)){
            if(map[x][y] == MapValue.EDGE.getValue() || map[x][y] == MapValue.LINE.getValue()){
                return true;
            }
        }
        return false;
    }

    public static Point getBestMoveLeft(int x, int y, Input move, int[][] map){
        switch (move){
            case MOVE_UP:
                if(isPathWalkable(map,x-1,y))
                    return new Point(x-1,y, MOVE_LEFT);
                if(isPathWalkable(map,x,y-1))
                    return new Point(x,y-1, MOVE_UP);
                return new Point(x+1,y, MOVE_RIGHT);
            case MOVE_DOWN:
                if(isPathWalkable(map,x+1,y))
                    return new Point(x+1,y, MOVE_RIGHT);
                if(isPathWalkable(map,x,y+1))
                    return new Point(x,y+1, MOVE_DOWN);
                return new Point(x-1,y, MOVE_LEFT);
            case MOVE_LEFT:
                if(isPathWalkable(map,x,y+1))
                    return new Point(x,y+1, MOVE_DOWN);
                if(isPathWalkable(map,x-1,y))
                    return new Point(x-1,y, MOVE_LEFT);
                return new Point(x,y-1, MOVE_UP);
            case MOVE_RIGHT:
                if(isPathWalkable(map,x,y-1))
                    return new Point(x,y-1, MOVE_UP);
                if(isPathWalkable(map,x+1,y))
                    return new Point(x+1,y, MOVE_RIGHT);
                return new Point(x,y+1, MOVE_DOWN);
        }
        return null;
    }

    public static Point getBestMoveRight(int x, int y, Input move, int[][] map){
        switch (move){
            case MOVE_UP:
                if(isPathWalkable(map,x+1,y))
                    return new Point(x+1,y, MOVE_RIGHT);
                if(isPathWalkable(map,x,y-1))
                    return new Point(x,y-1, MOVE_UP);
                return new Point(x-1,y, MOVE_LEFT);
            case MOVE_DOWN:
                if(isPathWalkable(map,x-1,y))
                    return new Point(x-1,y, MOVE_LEFT);
                if(isPathWalkable(map,x,y+1))
                    return new Point(x,y+1, MOVE_DOWN);
                return new Point(x+1,y, MOVE_RIGHT);
            case MOVE_LEFT:
                if(isPathWalkable(map,x,y-1))
                    return new Point(x,y-1, MOVE_UP);
                if(isPathWalkable(map,x-1,y))
                    return new Point(x-1,y, MOVE_LEFT);
                return new Point(x,y+1, MOVE_DOWN);
            case MOVE_RIGHT:
                if(isPathWalkable(map,x,y+1))
                    return new Point(x,y+1, MOVE_DOWN);
                if(isPathWalkable(map,x+1,y))
                    return new Point(x+1,y, MOVE_RIGHT);
                return new Point(x,y-1, MOVE_UP);
        }
        return null;
    }

    public static void getPathStarts(Player player, Point path1, Point path2){
        Input curDir = player.getDirRiskEnd();
        //  Log.i("LANTEST", "B: "  + curDir);
        Input pathDir1 = Input.getStartWheel(curDir);
        //Log.i("LANTEST", "AAa " + pathDir1 + "  B: "  + curDir);
        int[][] map = player.getModel().getMap();
      //  Log.i("MEGATEST_PLAY", " X: " + player.getX() + " Y: " + player.getY());
        Point p1 = Input.getMovePoint(player.getX(), player.getY(), pathDir1);
       // Log.i("MEGATEST_PLAY2", " X: " + p1.x + " Y: " + p1.y);
        Point p2 = null;
        if(UnitHelp.isPointInside(map, p1.x, p1.y) && map[p1.x][p1.y] == MapValue.EDGE.getValue()){

            Input pathDir2 = Input.getIncrRight(p1.direction, 1);
            p2 = Input.getMovePoint(player.getX(), player.getY(), pathDir2);
            if(UnitHelp.isPointInside(map, p2.x, p2.y) && map[p2.x][p2.y] == MapValue.EDGE.getValue()){
            }else{
                pathDir2 = Input.getIncrRight(pathDir2, 1);
                p2 = Input.getMovePoint(player.getX(), player.getY(), pathDir2);
            }

            //   Log.i("LANTEST_A1", "X: " + p1.x + " Y: " + p1.y + " DIR: " + p1.direction);
            //   Log.i("LANTEST_A2", "X: " + p2.x + " Y: " + p2.y + " DIR: " + p2.direction);

        }else{
            pathDir1 = Input.getIncrRight(pathDir1, 1);
            p1 = Input.getMovePoint(player.getX(), player.getY(), pathDir1);
            Input pathDir2 = Input.getIncrRight(pathDir1, 1);
            p2 = Input.getMovePoint(player.getX(), player.getY(), pathDir2);

            //   Log.i("LANTEST_B", "X: " + p2.x + " Y: " + p2.y + " DIR: " + p2.direction);
        }
        path1.x = p1.x; path1.y = p1.y; path1.direction = p1.direction;
        path2.x = p2.x; path2.y = p2.y; path2.direction = p2.direction;
    }
}
