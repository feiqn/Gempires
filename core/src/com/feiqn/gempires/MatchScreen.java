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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Random;

public class MatchScreen extends ScreenAdapter {

    public class Gem extends Image {

        TextureRegion image;
        Rectangle bounds;
        float x, y;
        public Gem(TextureRegion region) {
            x = this.getX();
            y = this.getY();
            image = new TextureRegion(region);
            bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        }

        public Rectangle getBounds() {
            return bounds;
        }

        private void setXY(float pX,float pY) {
            setPosition(pX, pY);
            bounds.setX((int)pX);
            bounds.setY((int)pY);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
        }
    }

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
            // final Image newGem = new Image(gemTextures[gemColor]);
            final Gem gem = new Gem(gemTextures[gemColor]);
            final int finalI = i;

            gem.setPosition(slots.get(i).x, slots.get(i).y);

            gem.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    gem.setColor(.5f, .5f, .5f, 1);
                    Gdx.app.log("gemTouched", "Gem touched at location: " + slots.get(finalI).x + ", " + slots.get(finalI).y + " of color: " + gemColor);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int point, int button) {
                    gem.setColor(1.5f, 1.5f, 1.5f, 1);

                    if(x > 0.5f && y < 1 && y > -1) {
                        Gdx.app.log("moveRight", "Move right");
                    } else if(x < -0.5f && y < 1 && y > -1) {
                        Gdx.app.log("moveLeft", "Move left");
                    } else if(x < 1 && x > -1 && y < -0.5f) {
                        Gdx.app.log("moveDown", "Move down");
                    } else if(x < 1 && x > -1 && y > 0.5f) {
                        Gdx.app.log("moveUp", "Move up");
                    }
                }
            });
            //gems.add((gem));
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
