package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Anim {
   private Texture img;
   private Animation<TextureRegion> anm;
    private TextureRegion[][] regions0;

    private TextureAtlas atlas;
    private TextureRegion[] region1;
   private float time;

     public Anim(String picture, int col, int row, Animation.PlayMode playMode) {
         img = new Texture(picture);
         TextureRegion region0 = new TextureRegion(img);
         int xCnt = region0.getRegionWidth() / col;
         int yCnt = region0.getRegionHeight() / row;
         regions0 = region0.split(xCnt, yCnt);
         region1 = new TextureRegion[regions0.length * regions0[0].length];
         int cnt = 0;
         for (int i = 0; i < regions0.length; i++) {
             for (int j = 0; j < regions0[0].length; j++) {
                 region1[cnt++] = regions0[i][j];
             }
         }
         anm = new Animation<TextureRegion>(1/15f, region1);

         setMode(playMode);

         time += Gdx.graphics.getDeltaTime();
     }

     public Anim(String region, Animation.PlayMode playMode) {
         atlas = new TextureAtlas("atlas/fighter/fighter.atlas");
         anm = new Animation<TextureRegion>(1/15f, atlas.findRegions(region));
         setMode(playMode);

         time += Gdx.graphics.getDeltaTime();
     }

     public TextureRegion getFrame() {
         return anm.getKeyFrame(time);
     }
     public void setTime(float time) { this.time += time;}

    public boolean isAnimationOver() {
         return anm.isAnimationFinished(time);
    }

    public void zeroTime() {this.time = 0;}

    public void setMode(Animation.PlayMode playMode) {
         anm.setPlayMode(playMode);
    }

    public TextureRegion[] jump() {
         return region1;
    }

    public void dispose() {}
}
