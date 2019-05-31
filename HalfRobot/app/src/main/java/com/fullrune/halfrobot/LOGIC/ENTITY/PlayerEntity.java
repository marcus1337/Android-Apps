package com.fullrune.halfrobot.LOGIC.ENTITY;

import com.fullrune.halfrobot.LOGIC.GRAPHICS.Graphics;
import com.fullrune.halfrobot.LOGIC.INPUT.playerinputset.EntityInput;
import com.fullrune.halfrobot.LOGIC.PHYSICS.Physics;

import java.util.ArrayList;

/**
 * Created by Marcus on 2017-09-26.
 */

public class PlayerEntity extends Entity {

    public PlayerEntity(Physics physics, Graphics graphics, EntityInput input)
     {
         super(physics, graphics, input);
        _lives = 1;
    }


    public void checkCollisions(ArrayList<Entity> ais) {

        for (Entity bot : ais) {
            if (!isDamaged() && !bot.startedDying() && !bot.isDead() && checkCollision(bot)) {
                setHP(getHP() - 30);
                setDamaged(true);
            }
        }

    }

    public void checkACollision(Entity entity) {
        if (entity.isDead() || entity.startedDying())
            return;

        if (!isDamaged() && checkCollision(entity)) {
            setHP(getHP() - 20);
            setDamaged(true);
        }
    }

    public int getLives() {
        return _lives;
    }

    public void setLives(int lives) {
        _lives = lives;
    }


    private int _lives;

}

