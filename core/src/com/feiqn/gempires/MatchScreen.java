package com.feiqn.gempires;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Random;

public class MatchScreen extends ScreenAdapter {

    GempiresGame game;

    private Stage stage;
    public int rows, columns;

    private TextureRegion[] gemTextures;

    // TODO: switch to Pooling for gems
    public ArrayList<Gem> gems;
    public ArrayList<Vector2> slots;

    public MatchScreen(GempiresGame game) { this.game = game; }

    public void createSlots(int countRows, int countColumns) {
        rows = countRows;
        columns = countColumns;

        Gdx.app.log("rowsLength", "rows set to: " + rows);
        Gdx.app.log("colLength", "columns set to: " + columns);

        Vector2 firstPosition = new Vector2(.5f, .5f);
        Vector2 lastPosition = firstPosition;
        slots.add(firstPosition);
        for(int i = 0; i < countRows; i++) {
            for(int x = 0; x < countColumns; x++) {
                Vector2 nextPosition = new Vector2(lastPosition.x + 1, lastPosition.y);

                slots.add(nextPosition);
                lastPosition = nextPosition;
            }
            if(slots.size() < countColumns * countRows) {
                lastPosition.y += 1;
            }
            lastPosition.x = firstPosition.x;
        }
    }

    public void fillSlots() {
        for(int i = 0; i < slots.size() - 1; i++) {
            final Random random = new Random();
            final int gemColor = random.nextInt(7);
            final Gem gem = new Gem(gemTextures[gemColor], gemColor, gems, slots, i, rows, columns);

            gem.setPosition(slots.get(i).x, slots.get(i).y);

            gems.add((gem));
            stage.addActor(gem);
        }
    }

    @Override
    public void show() {

        FitViewport fitViewport = new FitViewport(13, 13);

        stage = new Stage(fitViewport);

        Texture gemSpriteSheet = new Texture(Gdx.files.internal("gem_set.png"));
        gemTextures = new TextureRegion[]{
                new TextureRegion(gemSpriteSheet, 160, 0, 32, 32), // GREEN 0
                new TextureRegion(gemSpriteSheet, 128, 64, 32, 32), // PURPLE 1
                new TextureRegion(gemSpriteSheet, 32, 128, 32, 32), // RED 2
                new TextureRegion(gemSpriteSheet, 0, 192, 32, 32), // ORANGE 3
                new TextureRegion(gemSpriteSheet, 0, 288, 32, 32), // YELLOW 4
                new TextureRegion(gemSpriteSheet, 160, 320, 32, 32), // BLUE 5
                new TextureRegion(gemSpriteSheet, 160, 384, 32, 32) // CLEAR 6
        };

        slots = new ArrayList<Vector2>();
        gems = new ArrayList<Gem>();

        createSlots(12, 12);

        fillSlots();

        Gdx.input.setInputProcessor(stage);

        game.camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        game.mapRenderer.setView(game.camera);
        game.mapRenderer.render();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.getCamera().update();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
