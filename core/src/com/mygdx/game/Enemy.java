package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Enemy {

    private final Anim animEnemy;
    private boolean directionEnemy;
    private boolean startEnemy;
    private boolean bodyEnemyActive;
    private boolean contactEnemySensor;
    private Rectangle enemyRect;
    private Body bodyEnemy;

    public Enemy(String picture, int col, int row, Animation.PlayMode playMode, Body bodyEnemy, Rectangle enemyRect) {
        animEnemy = new Anim(picture, col, row, playMode);
        directionEnemy = true;
        bodyEnemyActive = true;
        this.enemyRect = enemyRect;
        this.bodyEnemy = bodyEnemy;
        this.contactEnemySensor = false;
        startEnemy = true;
    }

    public void render(SpriteBatch batch, float dt) {
        if (contactEnemySensor) {
                startEnemy = false;
                directionEnemy = !directionEnemy;
                contactEnemySensor = false;
        }
        if (!directionEnemy && !animEnemy.getFrame().isFlipX()) {
            animEnemy.getFrame().flip(true, false);
        }
        if (directionEnemy && animEnemy.getFrame().isFlipX()) {
            animEnemy.getFrame().flip(true, false);
        }
        if (startEnemy) {
            bodyEnemy.setLinearVelocity(30,0);
            bodyEnemy.applyForceToCenter(new Vector2(0, -1000), true);
        }
        enemyRect.x = bodyEnemy.getPosition().x - enemyRect.width/2;
        enemyRect.y = bodyEnemy.getPosition().y - enemyRect.height/2;
        animEnemy.setTime(dt);
//        System.out.println(bodyEnemy.getLinearVelocity());
        batch.draw(animEnemy.getFrame(), enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height);
    }

    public boolean isContactEnemySensor() {
        return contactEnemySensor;
    }

    public void setContactEnemySensor(boolean contactEnemySensor) {
        this.contactEnemySensor = contactEnemySensor;
    }

    public boolean isBodyEnemyActive() {
        return bodyEnemyActive;
    }

    public void setBodyEnemyActive(boolean bodyEnemyActive) {
        this.bodyEnemyActive = bodyEnemyActive;
    }

    public Anim getAnimEnemy() {
        return animEnemy;
    }

    public Rectangle getEnemyRect() {
        return this.enemyRect;
    }

    public Body getBodyEnemy() {
        return bodyEnemy;
    }

    public boolean isDirectionEnemy() {
        return directionEnemy;
    }

    public void dispose() {
        animEnemy.dispose();
    }
}
