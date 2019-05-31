package com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER;

import com.fullrune.areashiftertwo.MODEL.MapValue;
import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

import java.util.ArrayList;

public class PlayerHelp {

    public static Point detectPlayerEdgeHit(Point start, Point end, int[][] map, ArrayList<Point> track, MapValue mapValue, Player player){
        int x1 = start.x; int y1 = start.y; int x2 = end.x; int y2 = end.y;
        Input tmpDir = player.getDirection();
        player.setDirRiskEnd(player.getDirection());
       // Log.i("LANDEBY ",  "D: " + player.getDirRiskEnd());
        if(x1 == x2){ // no k value
            for(int i = y1; i != y2;){

                if(UnitHelp.isPointInside(map, x1, i)){
                    if(map[x1][i] == mapValue.getValue()){
                        player.setDirRiskEnd(tmpDir);
                        return new Point(x1, i);
                    }

                    for(Point p : getNeighbors(map,x1, i, tmpDir)){
                        if(map[p.x][p.y] == mapValue.getValue()){
                            player.setDirRiskEnd(p.direction);
                            return p;
                        }
                    }

                    if(track != null)
                        track.add(new Point(x1, i));
                }

                if(i < y2)
                    i++;
                else i--;
            }

        }else{
            float k = (float)(y2-y1)/(float)(x2-x1);
            float m = y1 - k*x1;

            for(int i = x1; i != x2 ;){
                int tmpy = Math.round(k*i+m);

                if(UnitHelp.isPointInside(map, i, tmpy)){
                    if(map[i][tmpy] == mapValue.getValue()){
                        player.setDirRiskEnd(tmpDir);
                        return new Point(i,tmpy);
                    }

                    for(Point p : getNeighbors(map,i, tmpy, tmpDir)){
                        if(map[p.x][p.y] == mapValue.getValue()){
                            player.setDirRiskEnd(p.direction);
                            return p;
                        }
                    }

                    if(track != null)
                        track.add(new Point(i, tmpy));
                }

                if(i < x2)
                    i++;
                else i--;
            }

        }
        return null;
    }

    private static ArrayList<Point> getNeighbors(int[][] map, int x, int y, Input direction){
        ArrayList<Point> result = new ArrayList<Point>();

        switch (direction) {
            case MOVE_UP:
             //   if(UnitHelp.isPointInside(map,x,y+1))
               //     result.add(new Point(x,y+1, Input.MOVE_DOWN));
                if(UnitHelp.isPointInside(map,x+1,y))
                    result.add(new Point(x+1,y, Input.MOVE_RIGHT));
                if(UnitHelp.isPointInside(map,x-1,y))
                    result.add(new Point(x-1,y, Input.MOVE_LEFT));

                break;
            case MOVE_DOWN:
             //   if(UnitHelp.isPointInside(map,x,y-1))
             //       result.add(new Point(x,y-1, Input.MOVE_UP));
                if(UnitHelp.isPointInside(map,x+1,y))
                    result.add(new Point(x+1,y, Input.MOVE_RIGHT));
                if(UnitHelp.isPointInside(map,x-1,y))
                    result.add(new Point(x-1,y, Input.MOVE_LEFT));
                break;
            case MOVE_LEFT:
                if(UnitHelp.isPointInside(map,x,y+1))
                    result.add(new Point(x,y+1, Input.MOVE_DOWN));
              //  if(UnitHelp.isPointInside(map,x+1,y))
             //       result.add(new Point(x+1,y, Input.MOVE_RIGHT));
                if(UnitHelp.isPointInside(map,x,y-1))
                    result.add(new Point(x,y-1, Input.MOVE_UP));
                break;
            case MOVE_RIGHT:
                if(UnitHelp.isPointInside(map,x,y+1))
                    result.add(new Point(x,y+1, Input.MOVE_DOWN));
                if(UnitHelp.isPointInside(map,x,y-1))
                    result.add(new Point(x,y-1, Input.MOVE_UP));
              //  if(UnitHelp.isPointInside(map,x-1,y))
              //      result.add(new Point(x-1,y, Input.MOVE_LEFT));
                break;
        }

        return result;
    }

    public static void adjustVelocityFromDirection(Player player) {
        Input direction = player.getDirection();
        if (direction == null) {
            player.setxVel(0);
            player.setyVel(0);
        } else
            switch (direction) {
                case MOVE_UP:
                    player.setxVel(0);
                    player.setyVel(-player.getBaseVelocity());
                    break;
                case MOVE_DOWN:
                    player.setxVel(0);
                    player.setyVel(player.getBaseVelocity());
                    break;
                case MOVE_LEFT:
                    player.setxVel(-player.getBaseVelocity());
                    player.setyVel(0);
                    break;
                case MOVE_RIGHT:
                    player.setxVel(player.getBaseVelocity());
                    player.setyVel(0);
                    break;
            }
    }

}
