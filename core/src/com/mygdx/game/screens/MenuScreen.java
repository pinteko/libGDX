package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

public class MenuScreen implements Screen {

    Main game;

    private SpriteBatch batch;

    private Texture img;
    private Texture button_start;
    private Rectangle startRect;
    private Music music;
    private Sound sound;

    private ShapeRenderer shapeRenderer;

    public MenuScreen(Main main) {
        game = main;
        batch = new SpriteBatch();
        img = new Texture("city.jpg");
        button_start = new Texture("button_start.png");
        startRect = new Rectangle(Gdx.graphics.getWidth()/2f - button_start.getWidth()/2f,
                Gdx.graphics.getHeight()/2f - button_start.getHeight()/2f,
                button_start.getWidth(), button_start.getHeight());
        shapeRenderer = new ShapeRenderer();
        music = Gdx.audio.newMusic(Gdx.files.internal("menu.wav")); //написать имя музыки из папки assets
        music.setLooping(true); //повторяемость
        music.setVolume(0.25f);
        music.play();
//        sound = Gdx.audio.newSound((Gdx.files.internal("menu.wav")));  //написать имя аудио из папки assets
//        sound.setLooping(1L, true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.BLUE);

        batch.begin();
        batch.draw(img, 0, 0);
        batch.draw(button_start, Gdx.graphics.getWidth()/2f - button_start.getWidth()/2f,
                Gdx.graphics.getHeight()/2f - button_start.getHeight()/2f);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(startRect.x, startRect.y, startRect.width, startRect.height);
        shapeRenderer.end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (startRect.contains(x, y)) {
            dispose();
            game.setScreen(new GameScreen(game)); }
        }
//        else {
////            sound.play(0.5f, 1, 0);  //pitch замедление (0.5f) к примеру
////            sound.loop(); //включение с повтором
////            sound.stop(); // остановка
//        }
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
//        sound.dispose();
    }
}
