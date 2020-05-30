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
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class MatchScreen extends ScreenAdapter {

    GempiresGame game;

    private Stage stage;
    public int rows, columns;

    // private Gem gems;

    private Texture gemSpriteSheet;
    private TextureRegion greenGemTexture;

    // public Image greenGem;

    public ArrayList<Image> gems;
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
            // Gdx.app.log("rowLoop", "row loop " + i + " of " + countRows);
            for(int x = 0; x < countColumns; x++) {
                // Gdx.app.log("colLoop", "column loop " + x + " of " + countColumns + " in row " + i);
                Vector2 nextPosition = new Vector2(lastPosition.x + 1, lastPosition.y);

                slots.add(nextPosition);
                lastPosition = nextPosition;
                // Gdx.app.log("slotReadout", "Wrote to slot: " + (x + i) + " with value: " + slots.get(x + i));
            }
            if(slots.size() < countColumns * countRows) {
                lastPosition.y += 1;
            }
            lastPosition.x = firstPosition.x;
        }
    }

    public void fillSlots() {
        // Gdx.app.log("fillSlots", "MatchScreen.fillSlots");
        for(int i = 0; i < slots.size(); i++) {
            // TODO: pick a random gem
            Image greenGem = new Image(greenGemTexture);
            greenGem.setSize(1,1);
            greenGem.setPosition(slots.get(i).x, slots.get(i).y);
            stage.addActor(greenGem);
        }
    }

    @Override
    public void show() {
        // Gdx.app.log("MatchShow", "MatchScreen.show");

        FitViewport fitViewport = new FitViewport(13, 13);

        stage = new Stage(fitViewport);

        gemSpriteSheet = new Texture(Gdx.files.internal("gem_set.png"));
        greenGemTexture = new TextureRegion(gemSpriteSheet, 160, 0, 32, 32);

        slots = new ArrayList<Vector2>();
        gems = new ArrayList<Image>();

        createSlots(12, 12);
        Gdx.app.log("slotsCreated", "Finished creating slots");

        fillSlots();
        Gdx.app.log("slotsFilled",  "Finished filling slots");

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
