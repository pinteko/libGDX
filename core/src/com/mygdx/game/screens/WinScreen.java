package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Main;

public class WinScreen implements Screen {
    private Main game;

    private final SpriteBatch batch;

    private final Texture img;
    private final Texture button_goNext;
    private final Texture button_exit;
    private final Rectangle goNextRect;
    private final Rectangle exitRect;

    private final Music music;

    private ShapeRenderer shapeRenderer;

    public WinScreen(Main main) {
        this.game = main;
        batch = new SpriteBatch();
        img = new Texture("win.jpg");
        button_goNext = new Texture("next.png");
        button_exit = new Texture("exit.jpg");
        goNextRect = new Rectangle(0, 0, button_goNext.getWidth(), button_goNext.getHeight());
        exitRect = new Rectangle(Gdx.graphics.getWidth() - button_exit.getWidth(),
                0, button_exit.getWidth(), button_exit.getHeight());
        shapeRenderer = new ShapeRenderer();
        music = Gdx.audio.newMusic(Gdx.files.internal("winMusic.wav")); //написать имя музыки из папки assets
        music.setLooping(false); //повторяемость
        music.setVolume(0.25f);
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(button_goNext, 0, 0, button_goNext.getWidth(), button_goNext.getHeight());
        batch.draw(button_exit, Gdx.graphics.getWidth() - button_exit.getWidth(),
                0, button_exit.getWidth(), button_exit.getHeight());
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(goNextRect.x, goNextRect.y, goNextRect.width, goNextRect.height);
        shapeRenderer.rect(exitRect.x, exitRect.y, exitRect.width, exitRect.height);
        shapeRenderer.end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (goNextRect.contains(x, y)) {
                dispose();
                game.setScreen(new GameScreenNextLevel(game, "map/map3.tmx"));
            }
            if (exitRect.contains(x, y)) {
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        music.dispose();
        shapeRenderer.dispose();
        button_exit.dispose();
        button_goNext.dispose();
    }
}
