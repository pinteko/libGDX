package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class Enemy {

    private final Anim animEnemy;
    private boolean directionEnemy;
    private boolean bodyEnemyActive;
    private Rectangle enemyRect;
    private Body bodyEnemy;

    public Enemy(String picture, int col, int row, Animation.PlayMode playMode, Body bodyEnemy, Rectangle enemyRect) {
        animEnemy = new Anim(picture, col, row, playMode);
        directionEnemy = true;
        bodyEnemyActive = true;
        this.enemyRect = enemyRect;
        this.bodyEnemy = bodyEnemy;
    }

    public void render(SpriteBatch batch, float dt) {
        if (enemyRect.x >= 380) {
            animEnemy.getFrame().flip(true, false);
            bodyEnemy.setLinearVelocity(0,0);
            directionEnemy = false;
        }
        if (enemyRect.x < 275) {
            animEnemy.getFrame().flip(true, false);
            bodyEnemy.setLinearVelocity(0,0);
            directionEnemy = true;
        }
        if (directionEnemy) {
            bodyEnemy.setLinearVelocity(30,0);
        } else {
            bodyEnemy.setLinearVelocity(-30,0);
        }

        animEnemy.setTime(dt);

        batch.draw(animEnemy.getFrame(), enemyRect.x, enemyRect.y, enemyRect.width, enemyRect.height);
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
