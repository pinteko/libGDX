package com.mygdx.game.screens;

import com.badlogic.gdx.math.Vector2;

public class Bullet {
    Vector2 position;
    Vector2 velocity;
    boolean active;
    boolean direction;

    public Bullet() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public void setup(float x, float y, float vx, float vy, boolean direction) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        this.direction = direction;
    }

    public void destroy() {
        active = false;
    }

    public void update(float dt) {
        if (!this.direction) {position.x += position.x * dt;}
        else {position.x -= position.x * dt;}
        if (position.x < 30 || position.x > 500) {
            destroy();
        }
    }
}
