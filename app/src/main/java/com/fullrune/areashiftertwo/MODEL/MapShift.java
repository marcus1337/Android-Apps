package com.fullrune.areashiftertwo.MODEL;

import android.util.Log;

import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import static com.fullrune.areashiftertwo.MODEL.MapValue.*;
import static com.fullrune.areashiftertwo.MODEL.MapValue.EDGE;
import static com.fullrune.areashiftertwo.MODEL.MapValue.EMPTY;
import static com.fullrune.areashiftertwo.MODEL.MapValue.LINE;
import static com.fullrune.areashiftertwo.MODEL.MapValue.WALL;
import static com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp.*;

public class MapShift implements Serializable {
    private int[][] fakeMap;
    private int[][] map;

    private int dynCounter;

    private int wallCounter;
    public int getWallCounter(){
        return  wallCounter;
    }
    public void setWallCounter(int wallCounter){
        this.wallCounter = wallCounter;
    }

    private int getDynChecked() {
        return dynCounter;
    }

    private int getDynFree() {
        return dynCounter + 1;
    }

    public MapShift(int[][] map, int rows, int cols) {
        this.map = map;
        fakeMap = new int[rows][cols];
        dynCounter = 1;
    }

    private boolean isBlueBox(int x, int y){
        if(!isPointInside(map, x, y)){
            return false;
        }
        if(!(map[x][y] == LINE.getValue()) && !(map[x][y] == EDGE.getValue())){
            return false;
        }
        if(fakeMap[x][y] == getDynChecked()){
            return false;
        }
        return true;
    }

    private boolean isBoxTrash(int x, int y){
        return !isPointInside(map, x, y) || isPointMapType(map,x,y,WALL);
    }

    private boolean twoOrThreeNo(int x, int y){
        if(isPointMapType(map,x+1,y,EDGE) && isPointMapType(map,x-1,y,EDGE)){
            if(isBoxTrash(x,y+1) && isBoxTrash(x,y-1)){
                return true;
            }
        }
        if(isPointMapType(map,x,y+1,EDGE) && isPointMapType(map,x,y-1,EDGE)){
            if(isBoxTrash(x-1,y) && isBoxTrash(x+1,y)){
                return true;
            }
        }
        int countTrash = 0;
        if(isBoxTrash(x,y+1))
            countTrash++;
        if(isBoxTrash(x,y-1))
            countTrash++;
        if(isBoxTrash(x+1,y))
            countTrash++;
        if(isBoxTrash(x-1,y))
            countTrash++;
        if(countTrash >= 3)
            return true;

        return false;
    }

    private boolean isBlueBox2(int x, int y){
        if(!isPointInside(map, x, y)){
            return false;
        }
        if(!(map[x][y] == EDGE.getValue())){
            return false;
        }
        if(fakeMap[x][y] == getDynChecked()){
            return false;
        }
        return true;
    }

    private void tryWallTrans(int x, int y){
        if(twoOrThreeNo(x,y)){
            map[x][y] = WALL.getValue();
            wallCounter++;
        }
        fakeMap[x][y] = getDynChecked();
    }

    private void removeLinesAndEdges2(int x, int y){
        LinkedList<Point> extra = new LinkedList<Point>();

        tryWallTrans(x, y);

        for(int i = x+1; isBlueBox2(i, y); i++){
            if(isBlueBox2(i,y+1))
                extra.add(new Point(i,y+1));
            if(isBlueBox2(i,y-1))
                extra.add(new Point(i,y-1));
            tryWallTrans(i, y);
        }
        for(int i = x-1; isBlueBox2(i, y); i--){
            if(isBlueBox2(i,y+1))
                extra.add(new Point(i,y+1));
            if(isBlueBox2(i,y-1))
                extra.add(new Point(i,y-1));
            tryWallTrans(i, y);
        }

        for(int i = y+1; isBlueBox2(x, i); i++){
            if(isBlueBox2(x+1,i))
                extra.add(new Point(x+1,i));
            if(isBlueBox2(x-1,i))
                extra.add(new Point(x-1,i));
            tryWallTrans(x, i);
        }

        for(int i = y-1; isBlueBox2(x, i); i--){
            if(isBlueBox2(x+1,i))
                extra.add(new Point(x+1,i));
            if(isBlueBox2(x-1,i))
                extra.add(new Point(x-1,i));
            tryWallTrans(x, i);
        }

        while(!extra.isEmpty()){
            Point p = extra.pop();
            removeLinesAndEdges2(p.x, p.y);
        }
    }

    private void removeLinesAndEdges(int x, int y){

        if(!isBlueBox(x,y)){
            return;
        }
        if(map[x][y] == LINE.getValue()){
            map[x][y] = EDGE.getValue();
        }
        if(twoOrThreeNo(x,y)){
            map[x][y] = WALL.getValue();
            wallCounter++;
        }
        fakeMap[x][y] = getDynChecked();

        removeLinesAndEdges(x+1,y);
        removeLinesAndEdges(x-1,y);
        removeLinesAndEdges(x,y+1);
        removeLinesAndEdges(x,y-1);
    }

    public void removeLinesToEmpty(ArrayList<Point> lines){
        if(lines == null || lines.isEmpty())
            return;
        for(Point p : lines){
            map[p.x][p.y] = MapValue.EMPTY.getValue();
        }
    }

    public void removeLinesAndEdges(ArrayList<Point> lines){
        if(lines == null || lines.isEmpty())
            return;
        Point p = lines.get(0);
        for(Point point : lines){
            map[point.x][point.y] = EDGE.getValue();
        }

        removeLinesAndEdges2(p.x, p.y);
        dynCounter+=2;
    }

    public void shiftWalls(Point start) {
        if(start == null)
            return;
        LinkedList<Point> list = new LinkedList<Point>();
        list.add(new Point(start.x, start.y));
        shiftDown(list);
    }

    private void shiftUpDown(Point p, LinkedList<Point> listUP, LinkedList<Point> listDOWN) {
        if(isEmptyBox(p.x, p.y)) {
            wallCounter++;
            map[p.x][p.y] = WALL.getValue();
        }

        for (int i = p.x + 1; isEmptyBox(i, p.y); i++) {
            map[i][p.y] = WALL.getValue();
            wallCounter++;
            if (isEmptyBox(i, p.y + 1))
                listDOWN.add(new Point(i, p.y + 1));
            if (isEmptyBox(i, p.y - 1))
                listUP.add(new Point(i, p.y - 1));
        }
        for (int i = p.x - 1; isEmptyBox(i, p.y); i--) {
            map[i][p.y] = WALL.getValue();
            wallCounter++;
            if (isEmptyBox(i, p.y + 1))
                listDOWN.add(new Point(i, p.y + 1));
            if (isEmptyBox(i, p.y - 1))
                listUP.add(new Point(i, p.y - 1));
        }
    }

    private void shiftDown(LinkedList<Point> listDOWN) {
        LinkedList<Point> listUP = new LinkedList<Point>();
        int startX = listDOWN.peek().x;
        int startY = listDOWN.peek().y;
        if (isEmptyBox(startX, startY + 1))
            listDOWN.add(new Point(startX, startY + 1));

        while (!listDOWN.isEmpty()) {
            Point p = listDOWN.pop();
            shiftUpDown(p, listUP, listDOWN);
        }

        if (!listUP.isEmpty()) {
            shiftUp(listUP);
        }

    }

    private void shiftUp(LinkedList<Point> listUP) {
        LinkedList<Point> listDOWN = new LinkedList<Point>();
        int startX = listUP.peek().x;
        int startY = listUP.peek().y;
        if (isEmptyBox(startX, startY - 1))
            listUP.add(new Point(startX, startY - 1));

        while (!listUP.isEmpty()) {
            Point p = listUP.pop();
            shiftUpDown(p, listUP, listDOWN);
        }

        if (!listDOWN.isEmpty()) {
            shiftDown(listDOWN);
        }
    }

    private boolean isEmptyBox(int x, int y) {
        if (!isPointInside(map, x, y) || !(map[x][y] == EMPTY.getValue()))
            return false;
        return true;
    }

}
