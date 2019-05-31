package test.gameapp.MODEL.SERVER.COLLISION;

import test.gameapp.MODEL.SERVER.ITEM.Item;
import test.gameapp.MODEL.SERVER.PLAYER.Player;

/**
 * This class includes different functions to determine collisions:
 *
 * collisionBetweenPlayers() -> Determines if a collision between two players has occured.
 * collisionBetweenPlayerAndItems() -> Determines if a collision between a player and an item has occured.
 * collisionBetweenPlayerAndItem() -> Determines if a collision between a player and a specific item has occured.
 * attackPlayer() -> Determines if the attack cast by one of the players hits the other player.
 * attackItem() -> Determines if the attack cast by one of the players hits an item.
 * useItem() -> Determines if the player is close enough to an object to use it for something.
 * keepPlayerInsideArea() -> Makes sure that the player stays inside a given area.
 *
 * Created by Andreas on 2017-01-04.
 */

public class CollisionDetection {

    /**
     *  This function checks if there is a collision between two Player objects.
     *  If a collision is detected the function will return true.
     *  If no collision is detected the function will return false.
     *
     * @param a - (Player) 1:st player
     * @param b - (Player) 2:nd player
     * @param playerRadius - (int) The radius of the players.
     * @return - (boolean) true (if there is a collision betweem the players), false (if there is no collision between the players)
     */
    public static boolean collisionBetweenPlayers(Player a, Player b,int playerRadius){
        if(a==null || b==null || a.getDead() || b.getDead())
            return false;

        float aX = a.getX();
        float aY = a.getY();
        float bX = b.getX();
        float bY = b.getY();
        if((playerRadius > (aX - bX) && (aX - bX) > -playerRadius) && (playerRadius > (aY - bY) && (aY - bY) > -playerRadius))
            return true;
        return false;
    }

    /**
     * This functions checks if there is a collision between a player and an item from the array.
     * If a collision is detected the function will return the item that the player collided with.
     * If no collision is detected the function will return null.
     *
     * @param a - (Player) the player
     * @param items (Item[]) array of all the items
     * @param playerRadius (int) The radius of the player
     * @return Item or null
     */
    public static Item collisionBetweenPlayerAndItems(Player a,Item[] items,int playerRadius){
        if(a==null || items == null || items.length==0)
            return null;

        float aX = a.getX();
        float aY = a.getY();
        for(int i=0;i<items.length;i++){
            if(items[i]!=null && items[i].getCollision()){
                if((aX+playerRadius)> (items[i].getX()-items[i].getWidth()/2) && (aX-playerRadius)<(items[i].getX()+items[i].getWidth()/2)){
                    if((aY+playerRadius)> (items[i].getY()-items[i].getHeight()/2) && (aY-playerRadius)<(items[i].getY()+items[i].getHeight()/2)){
                        return items[i];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Checks if there is a collision between a certain player and object.
     * If there is true will be returner. Else false will be returned.
     */
    public static boolean collisionBetweenPlayerAndItem(Player a,Item item,int playerRadius){
        if(a==null || item == null)
            return false;

        float aX = a.getX();
        float aY = a.getY();
        if(item.getCollision()){
            if((aX+playerRadius)> (item.getX()-item.getWidth()/2) && (aX-playerRadius)<(item.getX()+item.getWidth()/2)){
                if((aY+playerRadius)> (item.getY()-item.getHeight()/2) && (aY-playerRadius)<(item.getY()+item.getHeight()/2)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * playerAttacking attacks the otherPlayer. If there is a hit True will be returned.
     * If there is no hit false will be returned.
     *
     * @param playerAttacking the player attacking
     * @param otherPlayer the player getting attacked
     * @param weaponrange (int) Range of the weapon
     * @return true or false
     */
    public static boolean attackPlayer(Player playerAttacking, Player otherPlayer,int weaponrange){
        boolean collision = false;

        float playerX = playerAttacking.getX();
        float playerY = playerAttacking.getY();
        float playerAngle = playerAttacking.getAngle();

        if(playerAngle == 0f)
            playerAttacking.setY(playerY-weaponrange);
        else if(playerAngle == 180f)
            playerAttacking.setY(playerY+weaponrange);
        else if(playerAngle == 90f)
            playerAttacking.setX(playerX-weaponrange);
        else if(playerAngle == 270f)
            playerAttacking.setX(playerX+weaponrange);

        if(collisionBetweenPlayers(playerAttacking,otherPlayer,40))
            collision = true;

        playerAttacking.setX(playerX);
        playerAttacking.setY(playerY);

        return collision;
    }

    /**
     * playerAttacking attacks items.
     * If an item is attacked that Item will be returned.
     *
     * @param playerAttacking the player attacking
     * @param items list of items
     * @param weaponrange range of the weapon
     * @return the attacked item.
     */
    public static Item attackItem(Player playerAttacking, Item[] items,int weaponrange){
        float playerX = playerAttacking.getX();
        float playerY = playerAttacking.getY();
        float playerAngle = playerAttacking.getAngle();

        if(playerAngle == 0f)
            playerAttacking.setY(playerY-weaponrange);
        else if(playerAngle == 180f)
            playerAttacking.setY(playerY+weaponrange);
        else if(playerAngle == 90f)
            playerAttacking.setX(playerX-weaponrange);
        else if(playerAngle == 270f)
            playerAttacking.setX(playerX+weaponrange);

        Item item = collisionBetweenPlayerAndItems(playerAttacking,items,0);
        playerAttacking.setX(playerX);
        playerAttacking.setY(playerY);

        return item;
    }

    /**
     * Player a tries to use an item.
     * If an item is within armlength that item will be returned.
     *
     * @param a - the player that wants to use an item
     * @param items - list of items
     * @param armlength - length of the players arm
     * @return - item within armslength from the player
     */
    public static Item useItem(Player a,Item[] items,int armlength){
        float playerX = a.getX();
        float playerY = a.getY();
        float playerAngle = a.getAngle();

        if(playerAngle == 0f)
            a.setY(playerY-armlength);
        else if(playerAngle == 180f)
            a.setY(playerY+armlength);
        else if(playerAngle == 90f)
            a.setX(playerX-armlength);
        else if(playerAngle == 270f)
            a.setX(playerX+armlength);

        Item item = collisionBetweenPlayerAndItems(a,items,0);
        a.setX(playerX);
        a.setY(playerY);

        return item;
    }

    /**
     * Makes sure that the player stays inside a specific area.
     * @param player - The player
     * @param xmin - Minimum allowed X for the player
     * @param xmax - Maximum allowed X for the player
     * @param ymin - Minimum allowed Y for the player
     * @param ymax - Maximum allowed Y for the player
     */
    public static void keepPlayerInsideArea(Player player,int xmin,int xmax,int ymin,int ymax){
        if(player.getX()<xmin)
            player.setX(xmin);
        if(player.getX()>xmax)
            player.setX(xmax);
        if(player.getY()<ymin)
            player.setY(ymin);
        if(player.getY()>ymax)
            player.setY(ymax);
    }
}
