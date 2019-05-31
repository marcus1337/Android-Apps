package com.fullrune.halfrobot.LOGIC.ENTITY;

import com.fullrune.halfrobot.LOGIC.GRAPHICS.Graphics;
import com.fullrune.halfrobot.LOGIC.INPUT.playerinputset.EntityInput;
import com.fullrune.halfrobot.LOGIC.Model;
import com.fullrune.halfrobot.LOGIC.PHYSICS.Physics;

/**
 * Created by Marcus on 2017-09-26.
 */

public class BotEntity extends Entity {

    BotEntity(Physics physics, Graphics graphics, EntityInput input) {
        super(physics, graphics, input);
        setID(777);
    }

    public void update(Model model) {
        if (isDead() || startedDying())
            return;

        _input.handleInput(this, model);
        _physics.update(model, this);
    }

}
