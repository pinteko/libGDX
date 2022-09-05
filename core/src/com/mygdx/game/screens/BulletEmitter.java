package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BulletEmitter {
    private static final BulletEmitter ourInstance = new BulletEmitter();

    public static BulletEmitter getInstance() {
        return ourInstance;
    }

    Texture texture;
    Bullet[] bullets;

    private BulletEmitter() {
        texture = new Texture("bullet.png");
        bullets = new Bullet[200];
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet();
        }
    }

    public void update(float dt) {
        for (Bullet o : bullets) {
            if (o.active) {
                o.update(dt);
            }
        }
    }
    public void render(SpriteBatch batch, boolean direction) {
        for (Bullet o : bullets) {
            if (o.active) {
                batch.draw(texture, o.position.x, o.position.y);
            }
        }
    }
}
