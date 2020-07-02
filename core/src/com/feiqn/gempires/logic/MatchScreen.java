package com.feiqn.gempires.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.Gem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MatchScreen extends ScreenAdapter {
    // Tables are kind of made for this sort of thing. Fuck tables.

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer orthoMapRenderer;
    private TiledMap matchMap;
    public MapProperties mapProperties;

    GempiresGame game;

    private Stage stage;
    public int rows, columns;
    int horizontalMatchLength, verticalMatchLength;

    private TextureRegion[] gemTextures;

    // TODO: switch to Pooling for gems, and libGDX's Array<> data type
    public ArrayList<Gem> gems;
    public ArrayList<Vector2> slots;
    public ArrayList<Gem> sendToDestroy;

    public MatchScreen(GempiresGame game) { this.game = game; }

    // TODO: later, this function can probably folded in to the gems array, and slots<> can be removed
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
            final com.feiqn.gempires.logic.Gem gem = new com.feiqn.gempires.logic.Gem(gemTextures[gemColor], gemColor, i, this);

            gem.setPosition(slots.get(i).x, slots.get(i).y);

            gems.add((gem));
            stage.addActor(gem);
        }
    }

    public void swapGems(com.feiqn.gempires.logic.Gem origin, com.feiqn.gempires.logic.Gem destination) {
        final Vector2 originSlot = new Vector2(origin.getX(), origin.getY());

        origin.setXY(destination.getX(), destination.getY());
        destination.setXY(originSlot.x, originSlot.y);
        Collections.swap(gems, gems.indexOf(origin), gems.indexOf(destination));
        origin.GemIndex = gems.indexOf(origin);
        destination.GemIndex = gems.indexOf(destination);
    }

    public void checkBoundsForSwap(final float mouseUpAtX, final float mouseUpAtY, int index) {

        // TODO: animate :)
        if (mouseUpAtX > 1.1f && mouseUpAtY < 1 && mouseUpAtY > -1 && index != gems.size() - 1 && index % columns != columns - 1) { // MOVE RIGHT ->
            swapGems(gems.get(index), gems.get(index + 1));
            if(!checkWholeBoardForMatches()) {
                swapGems(gems.get(index), gems.get(index + 1));
            }
        } else if (mouseUpAtX < -0.1f && mouseUpAtY < 1 && mouseUpAtY > -1 && index % columns != 0) { // MOVE LEFT <-
            swapGems(gems.get(index), gems.get(index - 1));
            if(!checkWholeBoardForMatches()) {
                swapGems(gems.get(index), gems.get(index - 1));
            }
        } else if (mouseUpAtX < 1 && mouseUpAtX > -1 && mouseUpAtY < -0.3f && index - columns > 0) { // MOVE DOWN v
            swapGems(gems.get(index), gems.get(index - columns));
            if(!checkWholeBoardForMatches()) {
                swapGems(gems.get(index), gems.get(index - columns));
            }
        } else if (mouseUpAtX < 1 && mouseUpAtX > -1 && mouseUpAtY > 0.5f && index + columns < gems.size() -1) { // MOVE UP ^
            swapGems(gems.get(index), gems.get(index + columns));
            if(!checkWholeBoardForMatches()) {
                swapGems(gems.get(index), gems.get(index + columns));
            }
        }
    }

    public void destroy(ArrayList<com.feiqn.gempires.logic.Gem> gemsToDestroy) {
        for (com.feiqn.gempires.logic.Gem gem : gemsToDestroy) {
            final int gemToDestroy = gem.GemIndex;
            gems.get(gemToDestroy).remove();
            gem.remove();
            // TODO: some logic for holding empty spaces in the index, e.g., blank gems that are immediately removed; or maybe switch to a standard static Gem[] of size rows * columns
        }
        drop();
    }

    public void drop() {

    }

    public String gemColorAsString(int color) {
        String colour = "";
        switch(color) {
            case 0:
                colour = "Green";
                break;
            case 1:
                colour = "Purple";
                break;
            case 2:
                colour = "Red";
                break;
            case 3:
                colour = "Orange";
                break;
            case 4:
                colour = "Yellow";
                break;
            case 5:
                colour = "Blue";
                break;
            case 6:
                colour = "Clear";
                break;
        }
        return colour;
    }

    public void look(com.feiqn.gempires.logic.Gem gem) {
        horizontalMatchLength = 0;
        verticalMatchLength = 0;
        final String color = gemColorAsString(gem.GemColor);

        Gdx.app.log("reader", "I'm looking for " + color + " gems now.");
        Gdx.app.log("reader", "From index " + gem.GemIndex + ", I see: ");

        // TODO: something wonky in each of these loops, don't work quite right
        lookLeft(gem);
        lookRight(gem);
        lookDown(gem);
        lookUp(gem);
    }

    private void lookUp(com.feiqn.gempires.logic.Gem gem) {
        final int distanceToBottomBound = gem.GemIndex / columns;
        final int distanceToTopBound = rows - distanceToBottomBound - 1;

        // TODO: check for inverse problem as lookDown

        for(int u = 0; u < distanceToTopBound; u++) {
            try {
                Gdx.app.log("reader", "up: " + gems.get(gem.GemIndex+ + (u * columns)).GemIndex + ", and it's " + gemColorAsString(gems.get(gem.GemIndex+ + (u * columns)).GemColor));

                if (gems.get(gem.GemIndex+ + (u * columns)).GemColor == gem.GemColor) {
                    if (gems.get(gem.GemIndex+ + (u * columns)).GemIndex != gem.GemIndex) {
                        Gdx.app.log("reader", "I call that an up match!");
                        verticalMatchLength++;
                        sendToDestroy.add(gems.get(gem.GemIndex + +(u * columns)));
                    }
                } else {
                    Gdx.app.log("reader", "No match up.");
                    break;
                }
            } catch(Exception e) { /* uwu */ }
        }
    }

    private void lookDown(com.feiqn.gempires.logic.Gem gem) {
        final int distanceToBottomBound = gem.GemIndex / columns;

        // TODO: fix breaking early

        for(int b = 0; b < distanceToBottomBound; b++) {
            try {
                Gdx.app.log("reader", "down: " + gems.get(gem.GemIndex - (b * columns)).GemIndex + ", and it's " + gemColorAsString(gems.get(gem.GemIndex - (b * columns)).GemColor));
                if (gems.get(gem.GemIndex - (b * columns)).GemColor == gem.GemColor) {
                    if (gems.get(gem.GemIndex - (b * columns)).GemIndex != gem.GemIndex)
                    Gdx.app.log("reader", "I call that a down match!");
                    verticalMatchLength++;
                    sendToDestroy.add(gems.get(gem.GemIndex - (b * columns)));
                } else {
                    Gdx.app.log("reader", "No match down.");
                    break;
                }
            } catch(Exception e) { /* uwu */ }
        }
    }

    private void lookRight(com.feiqn.gempires.logic.Gem gem) {
        final int distanceToLeftBound = gem.GemIndex % rows;
        final int distanceToRightBound = columns - distanceToLeftBound - 1;

        // TODO: doesn't properly respect right bounds

        for(int r = 0; r < distanceToRightBound; r ++) {
            try {
                Gdx.app.log("reader", "right: " + gems.get(gem.GemIndex + r).GemIndex + ", and it's " + gemColorAsString(gems.get(gem.GemIndex + r).GemColor));
                if (gems.get(gem.GemIndex + r).GemColor == gem.GemColor) {
                    if (gems.get(gem.GemIndex + r).GemIndex != gem.GemIndex) {
                        Gdx.app.log("reader", "I call that a right match!");
                        horizontalMatchLength++;
                        sendToDestroy.add(gems.get(gem.GemIndex + r));
                    }
                } else {
                    Gdx.app.log("reader", "No match to the right.");
                    break;
                }
            } catch (Exception e) { /* uwu */ }
        }
    }

    private void lookLeft(com.feiqn.gempires.logic.Gem gem) {
        final int distanceToLeftBound = gem.GemIndex % rows;

        // TODO: something especially wonky here, breaking early

        for(int l = 0; l < distanceToLeftBound; l++) {
            try {
                Gdx.app.log("reader", "left: " + gem.GemIndex + ", and it's " + gemColorAsString(gem.GemColor));
                if (gems.get(gem.GemIndex - l).GemColor == gem.GemColor) {
                    if (gems.get(gem.GemIndex - l).GemIndex != gem.GemIndex) {
                        Gdx.app.log("reader", "I call that a left match!");
                        horizontalMatchLength++;
                        sendToDestroy.add(gems.get(gem.GemIndex - l));
                    }
                } else {
                    Gdx.app.log("reader", "No match to the left.");
                    break;
                }
            } catch(Exception e) { /* uwu */ }
        }
    }

    public boolean checkWholeBoardForMatches() {
        boolean matchFound = false;
        sendToDestroy = new ArrayList<>();

        for(int i = 0; i < gems.size() - 1; i++) { // gems.forEach((g) -> look(g)); would be better after updating gradle build to 1.8
            look(gems.get(i));
        }

        Gdx.app.log("reader", "HML: " + horizontalMatchLength + ", VML: " + verticalMatchLength);
        if(horizontalMatchLength > 3 || verticalMatchLength > 3) {
            matchFound = true;
            Gdx.app.log("matchFound", "true");
            destroy(sendToDestroy);
        } else {
            Gdx.app.log("reader", "No matches on the bored.");
        }
        return matchFound;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();

        matchMap = new TmxMapLoader().load("testMap.tmx"); // TODO: have background map selected and loaded in via MatchScreen constructor
        orthoMapRenderer = new OrthogonalTiledMapRenderer(matchMap, 1/32f);

        mapProperties = matchMap.getProperties();
        int mapWidth = mapProperties.get("width", Integer.class);
        int mapHeight = mapProperties.get("height", Integer.class);

        camera.setToOrtho(false, 8, 8);


        // camera.position.set(mapWidth * .5f, camera.viewportHeight * .5f, 0);
        camera.update();

        horizontalMatchLength = 0;
        verticalMatchLength = 0;

        FitViewport fitViewport = new FitViewport(8, 8);

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

        createSlots(5, 7);

        fillSlots();

        Gdx.input.setInputProcessor(stage);

        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        orthoMapRenderer.setView(camera);
        orthoMapRenderer.render();

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
