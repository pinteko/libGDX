package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	int clk;
	Anim animation;
	boolean direction;
	float x;
	float y;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		animation = new Anim("man.png", 6, 4, Animation.PlayMode.LOOP);
		x = Gdx.input.getX();
		y = Gdx.graphics.getHeight() - Gdx.input.getY();
		direction = true;
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		animation.setTime(Gdx.graphics.getDeltaTime());

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) clk++;
		Gdx.graphics.setTitle("Clicked "+clk+" times!");

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) direction = true;
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) direction = false;
		if (!animation.getFrame().isFlipX() && direction) animation.getFrame().flip(true, false);
		if (animation.getFrame().isFlipX() && !direction) animation.getFrame().flip(true, false);

		batch.begin();
		batch.draw(animation.getFrame(), 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		animation.dispose();
	}
}
