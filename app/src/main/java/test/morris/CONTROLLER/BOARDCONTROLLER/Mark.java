package test.morris.CONTROLLER.BOARDCONTROLLER;

/**
 * Created by Marcus on 2016-11-22.
 */

public class Mark {

    private float x, y, dist;
    int number;

    public Mark(float x, float y, float dist, int number) {
        this.x = x;
        this.y = y;
        this.dist = dist;
        this.number = number;
    }

    public int getNumber(){
        return number;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDist() {
        return dist;
    }

}
