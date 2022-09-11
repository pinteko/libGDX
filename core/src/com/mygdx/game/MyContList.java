package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.GameScreen;

public class MyContList implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("hero") && tmpB.equals("qube")) {
                GameScreen.rockMove = true;
//                a.getBody().applyForceToCenter(new Vector2(100000, 0), true);
//                b.getBody().setActive(false);
            }
            if (tmpA.equals("qube") && tmpB.equals("hero")) {
                GameScreen.rockMove = true;
//                b.getBody().applyForceToCenter(new Vector2(100000, 0), true);
//                a.getBody().setActive(false);

            }
            if (tmpA.equals("hero") && (tmpB.equals("state") || tmpB.equals("qube"))) {
                GameScreen.musicHero.play();
            }
            if ((tmpA.equals("state") || tmpA.equals("qube")) && tmpB.equals("hero")) {
                GameScreen.musicHero.play();

            }
            if (tmpA.equals("qube") && tmpB.equals("ball")) {
                GameScreen.musicBall.play();
            }
            if (tmpA.equals("ball") && tmpB.equals("qube")) {
                GameScreen.musicBall.play();

            }
            if (tmpA.equals("hero") && tmpB.equals("ball")) {
                GameScreen.musicGameOver.play();
                GameScreen.enemiesToDelete.add(b.getBody());
                GameScreen.bodyBallActive = false;
            }
            if (tmpA.equals("ball") && tmpB.equals("hero")) {
                GameScreen.musicGameOver.play();
                GameScreen.enemiesToDelete.add(a.getBody());
                GameScreen.bodyBallActive = false;
            }
            if (tmpA.equals("hero") && tmpB.equals("enemy")) {
                GameScreen.musicGameOver.play();
                GameScreen.enemiesToDelete.add(b.getBody());
                GameScreen.bodyEnemyActive = false;
            }
            if (tmpA.equals("enemy") && tmpB.equals("hero")) {
                GameScreen.musicGameOver.play();
                GameScreen.enemiesToDelete.add(a.getBody());
                GameScreen.bodyEnemyActive = false;
            }
            if (tmpA.equals("hero") && tmpB.equals("present")) {
                GameScreen.musicPresent.play();
            }
            if (tmpA.equals("present") && tmpB.equals("hero")) {
                GameScreen.musicPresent.play();

            }
            if (tmpA.equals("hero") && tmpB.equals("liana")) {
                    a.getBody().setGravityScale(0f);
            }
            if (tmpA.equals("liana") && tmpB.equals("hero")) {
                a.getBody().setGravityScale(0f);
            }
            if (tmpA.equals("sensor") && (tmpB.equals("state") || tmpB.equals("qube"))) {
                Hero.setContactGround(true);
                Hero.setJumpInAir(false);
            }
            if ((tmpA.equals("state") || tmpA.equals("qube")) && tmpB.equals("sensor")) {
                Hero.setContactGround(true);
                Hero.setJumpInAir(false);
            }
            if (tmpA.equals("footEnemy") && (tmpB.equals("stop"))) {
                GameScreen.stopEnemy = true;
            }
            if ((tmpA.equals("stop") || tmpA.equals("footEnemy"))) {
                GameScreen.stopEnemy = true;
            }
        }


    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("sensor") && (tmpB.equals("state") || tmpB.equals("qube"))) {
                Hero.setContactGround(false);
            }
            if ((tmpA.equals("state") || tmpA.equals("qube")) && tmpB.equals("sensor")) {
                Hero.setContactGround(false);
            }
            if (tmpA.equals("hero") && tmpB.equals("liana")) {
                a.getBody().setGravityScale(3.5f);
                Hero.setJumpInAir(false);
            }
            if (tmpA.equals("liana") && tmpB.equals("hero")) {
                a.getBody().setGravityScale(3.5f);
                Hero.setJumpInAir(false);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
