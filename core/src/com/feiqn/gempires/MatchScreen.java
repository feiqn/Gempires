package com.feiqn.gempires;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MatchScreen extends ScreenAdapter {

    GempiresGame game;

    private Stage stage;
    private int rows, columns;
    private Gem gems;


    public MatchScreen(GempiresGame game) { this.game = game; }

    @Override
    public void show() {
        stage = new Stage();
        stage.addActor(gems.greenGem);
        gems.greenGem.setPosition(Gdx.graphics.getWidth() * .5f, Gdx.graphics.getHeight() * .5f);

        game.camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);


        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
