package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;

public class Enemy {

    private final Anim animEnemy;

    public Enemy() {
        animEnemy = new Anim("enemy.png", 6, 5, Animation.PlayMode.LOOP);
    }
    public Anim getAnimEnemy() {
        return animEnemy;
    }
}
