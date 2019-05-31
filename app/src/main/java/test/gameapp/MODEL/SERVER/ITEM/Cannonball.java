package test.gameapp.MODEL.SERVER.ITEM;

/**
 * Created by Andreas on 2017-01-05.
 */

public class Cannonball extends Item {

    private int damage;

    public Cannonball(float x, float y, float width, float height, float angle, int hp, int type, int id,int damage) {
        super(x, y, width, height, angle, hp, type, id);

        this.damage = damage;
    }

    public int getDamage(){
        return damage;
    }
}
