package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

public class Hero {

    private Anim animHero;
    private final Anim animHeroStart;
    private final Anim animHeroJump;
    private final Anim animHeroPunch;
    private static boolean contactGround;
    private static boolean directionHero;
    private static boolean jumpInAir;


    public Hero() {
        animHero = new Anim("counter", Animation.PlayMode.LOOP);
        animHeroStart = new Anim("start", Animation.PlayMode.LOOP);
        animHeroJump = new Anim("jump", Animation.PlayMode.LOOP);
        animHeroPunch = new Anim("punch", Animation.PlayMode.LOOP);
        directionHero = true;
        contactGround = false;
        jumpInAir = false;
    }

    public void render(SpriteBatch batch, Body bodyHero, Rectangle heroRect, float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A) && !jumpInAir) {
            bodyHero.applyForceToCenter(new Vector2(-1000, 0), true);
            animHero = animHeroStart;
            directionHero = true;
            if (!contactGround) {
                jumpInAir = true;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) && !jumpInAir) {
            bodyHero.applyForceToCenter(new Vector2(1000, 0), true);
            animHero = animHeroStart;
            directionHero = false;
            if (!contactGround) {
                jumpInAir = true;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && contactGround) {
            bodyHero.applyForceToCenter(new Vector2(0, 3000), true);
            animHero = animHeroJump;}
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            bodyHero.applyForceToCenter(new Vector2(0, -3000), true);
            animHero = animHeroJump;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            animHero = animHeroPunch;
        }


        if (directionHero && animHero.getFrame().isFlipX()) {
            animHero.getFrame().flip(true, false);
        }
        if (!directionHero && !animHero.getFrame().isFlipX()) {
            animHero.getFrame().flip(true, false);}

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            fire(directionHero, heroRect);
        }

        animHero.setTime(dt);

        batch.draw(animHero.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);

    }

    public void fire(boolean direction, Rectangle heroRect) {
        for (Bullet o : BulletEmitter.getInstance().bullets) {
            if (!o.active) {
                o.setup(heroRect.x, heroRect.y + heroRect.height/2, heroRect.x + 400, heroRect.y, direction);
                break;
            }
        }
    }

    public void update(float dt, Array<RectangleMapObject> staticObjects, ArrayList<Enemy> enemies, PhysX physX) {
        BulletEmitter.getInstance().update(dt * 4);
        for (Bullet b : BulletEmitter.getInstance().bullets) {
            if (b.active) {
                for (RectangleMapObject o: staticObjects) {
                    if (o.getRectangle().contains(b.position.x, b.position.y)) {
                        b.destroy();
                    }
                }
                for (Enemy e: enemies) {
                        if (e.getEnemyRect().contains(b.position.x, b.position.y)) {
                            b.destroy();
                            physX.deleteBody(e.getBodyEnemy());
                            e.setBodyEnemyActive(false);
                        }
                }
            }
        }
    }

    public Anim getAnimHero() {
        return animHero;
    }

    public Anim getAnimHeroStart() {
        return animHeroStart;
    }

    public Anim getAnimHeroJump() {
        return animHeroJump;
    }

    public Anim getAnimHeroPunch() {
        return animHeroPunch;
    }

    public static boolean isContactGround() {
        return contactGround;
    }

    public static boolean isDirectionHero() {
        return directionHero;
    }

    public static boolean isJumpInAir() {
        return jumpInAir;
    }

    public static void setContactGround(boolean contactGround) {
        Hero.contactGround = contactGround;
    }

    public static void setDirectionHero(boolean directionHero) {
        Hero.directionHero = directionHero;
    }

    public static void setJumpInAir(boolean jumpInAir) {
        Hero.jumpInAir = jumpInAir;
    }

    public void dispose() {
        this.animHero.dispose();
        this.animHeroStart.dispose();
        this.animHeroJump.dispose();
        this.animHeroPunch.dispose();
    }
}
