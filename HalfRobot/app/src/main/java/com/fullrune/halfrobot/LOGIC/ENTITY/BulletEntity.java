package com.fullrune.halfrobot.LOGIC.ENTITY;

import com.fullrune.halfrobot.LOGIC.GRAPHICS.Graphics;
import com.fullrune.halfrobot.LOGIC.INPUT.playerinputset.EntityInput;
import com.fullrune.halfrobot.LOGIC.Model;
import com.fullrune.halfrobot.LOGIC.PHYSICS.Physics;

import java.util.ArrayList;

/**
 * Created by Marcus on 2017-09-26.
 */

public class BulletEntity extends Entity {

    public BulletEntity(Physics physics, Graphics graphics, EntityInput input){
        super(physics, graphics, input);
        setWidthHeight(15,12);
    }

    public void update(Model model){
        if (isDead())
            return;

        Entity e = model.getPlayerEntity();
        if (!e.isDamaged() && e.getID() != getID()) {
            if (checkCollision(e)) {
                e.setDamaged(true);
                e.setHP(e.getHP() - 40);
                setDead(true);
            }
        }

        ArrayList<Entity> ais = model.getAis();
        for (Entity bot : ais) {
            if (bot.getID() == getID() || bot.startedDying())
                continue;
            if (checkCollision(bot)) {
                bot.setDamaged(true);
                bot.setHP(bot.getHP() - 40);
                setDead(true);
            }
        }

        Entity boss = model.getBoss();
        if (boss.getID() != getID() && !boss.startedDying()) {
            if (checkCollision(boss)) {
                boss.setDamaged(true);
                boss.setHP(boss.getHP() - 40);
                setDead(true);
            }
        }

        _input.handleInput(this, model);
        _physics.update(model, this);

        if (ttl < 0)
            setDead(true);

        ttl--;
    }

    private int ttl = 300;
}
