package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
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
                GameScreen.bodies.add(b.getBody());
                b.getBody().setGravityScale(3.0f);
//                a.getBody().applyForceToCenter(new Vector2(100000, 0), true);
//                b.getBody().setActive(false);
            }
            if (tmpA.equals("qube") && tmpB.equals("hero")) {
                a.getBody().setGravityScale(3.0f);
                GameScreen.bodies.add(a.getBody());
//                b.getBody().applyForceToCenter(new Vector2(100000, 0), true);
//                a.getBody().setActive(false);

            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
