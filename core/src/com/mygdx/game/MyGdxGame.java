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
		y = 0;
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

		if (animation.getFrame() == animation.jump()[7]) {y += animation.getFrame().getRegionHeight()/2f;}
		if (animation.getFrame() == animation.jump()[8]) {y += animation.getFrame().getRegionHeight()/2f;}
		if (animation.getFrame() == animation.jump()[9]) {y -= animation.getFrame().getRegionHeight()/2f;}
		if (animation.getFrame() == animation.jump()[10]) {y -= animation.getFrame().getRegionHeight()/2f;}
		/* тут спрайт на картинке прыгает, и я его на двух кадрах подкидываю вверх, на двух опускаю вниз
		в соответствии с картинкой */

		batch.begin();
		batch.draw(animation.getFrame(), xDirection(), y);
		batch.end();
	}

	/* В этом методе я проверяю, уперся ли спрайт в конец дисплея в своем направлении,
	* и если да, то разворачиваю его и направляю в другую сторону, без использования клавиш.
	* Так же можно в любой точке заставить его развернуться  в нужную сторону с помощью клавиш
	* (это действие описано в методе render()).*/
	public float xDirection() {
//		float x = Gdx.input.getX() - animation.getFrame().getRegionWidth()/2;
		if (animation.getFrame().isFlipX()) {
			if (x >= Gdx.graphics.getWidth() - animation.getFrame().getRegionWidth()) {
				animation.getFrame().flip(true, false);
				direction = false;
				return x -= animation.getFrame().getRegionWidth()/100f;
			}
			return x += animation.getFrame().getRegionWidth()/100f;
		}
		else {
			if (x <= 0) {
				animation.getFrame().flip(true, false);
				direction = true;
				return x += animation.getFrame().getRegionWidth()/100f;
			}
			return x -= animation.getFrame().getRegionWidth()/100f;
		}
	}

	public float yDirection() {
//		float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animation.getFrame().getRegionHeight()/2;
		if (y < Gdx.graphics.getHeight()) {
			y += animation.getFrame().getRegionHeight()/2;
		}
		return y;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		animation.dispose();
	}
}
