package com.fullrune.areashiftertwo.MODEL.LOGIC;

import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input;
import com.fullrune.areashiftertwo.MODEL.Pieces.Point;

import static com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input.MOVE_DOWN;
import static com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input.MOVE_LEFT;
import static com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input.MOVE_RIGHT;
import static com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input.MOVE_UP;

public class Path {
    public Point point;
    Input turnStrategy;

    public Path(){
    }
    public Path(Point point){
        this.point = point;
    }

    public static void findStrategy(Point begin, Path p1, Path p2){
        if(findSimpleStrategy(begin, p1)){
            p2.turnStrategy = getOppositeStrategy(p1.turnStrategy);
        }else{
            findSimpleStrategy(begin, p2);
            p1.turnStrategy = getOppositeStrategy(p2.turnStrategy);
        }

    }

    public static boolean findSimpleStrategy(Point begin, Path p){
        Input pDir = p.point.direction;
        switch (begin.direction){
            case MOVE_UP:
                if(pDir == MOVE_LEFT){
                    p.turnStrategy = MOVE_LEFT;
                    return true;
                }
                if(pDir == MOVE_RIGHT){
                    p.turnStrategy = MOVE_RIGHT;
                    return true;
                }
                break;
            case MOVE_DOWN:
                if(pDir == MOVE_LEFT){
                    p.turnStrategy = MOVE_RIGHT;
                    return true;
                }
                if(pDir == MOVE_RIGHT){
                    p.turnStrategy = MOVE_LEFT;
                    return true;
                }
                break;
            case MOVE_LEFT:
                if(pDir == MOVE_UP){
                    p.turnStrategy = MOVE_RIGHT;
                    return true;
                }
                if(pDir == MOVE_DOWN){
                    p.turnStrategy = MOVE_LEFT;
                    return true;
                }

                break;
            case MOVE_RIGHT:
                if(pDir == MOVE_UP){
                    p.turnStrategy = MOVE_LEFT;
                    return true;
                }
                if(pDir == MOVE_DOWN){
                    p.turnStrategy = MOVE_RIGHT;
                    return true;
                }
                break;
        }

        return false;
    }

    public static Input getOppositeStrategy(Input input){
        if(input == Input.MOVE_LEFT)
            return Input.MOVE_RIGHT;
        else
            return Input.MOVE_LEFT;
    }

}
