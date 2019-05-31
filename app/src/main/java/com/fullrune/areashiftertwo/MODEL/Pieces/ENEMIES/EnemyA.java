package com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES;


import static com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES.EnemyType.ENEMY_A;

public class EnemyA extends Enemy {

    public EnemyA(int x, int y, int radius) {
        super(x, y, radius);
    }

    @Override
    public EnemyType getType() {
        return ENEMY_A;
    }
}
