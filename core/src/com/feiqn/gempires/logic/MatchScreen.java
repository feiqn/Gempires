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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.models.CampaignLevelID;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MatchScreen extends ScreenAdapter {
    // Tables are kind of made for this sort of thing.
    // For no particular reason whatsoever, fuck tables.

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer orthoMapRenderer;
    private TiledMap matchMap;

    public MapProperties mapProperties;

    public final float gemSwapTime = 0.2f;
    float timeToCompleteSwaps = 0f;

    GempiresGame game;
    private Stage stage;

    public Group gemGroup;

    public boolean matchFound,
                   classicMode,
                   allowUserInput;

    public int rows,
               columns,
               horizontalMatchLength,
               verticalMatchLength;

    private TextureRegion[] gemTextures;

    public CampaignLevelID campaignLevelID;

    public ArrayList<Gem> matchesForThisGem,
                          sendToDestroy,

                          leftMatches,
                          rightMatches,
                          upMatches,
                          downMatches;


    // TODO: Current implementation of Gems<> is poor. Switch to POOLING will be mandatory soon.
    public DelayedRemovalArray<Gem> gems;

    public ArrayList<Vector2> slots;

    public MatchScreen(GempiresGame game, Boolean classic, CampaignLevelID levelID){
        this.game = game;
        this.classicMode = classic;
        this.campaignLevelID = levelID;
    }
    public MatchScreen(GempiresGame game) {
        // new MatchScreen(game, true, CampaignLevelID.DEFAULT);
        this.game = game;
        this.classicMode = true;
        this.campaignLevelID = CampaignLevelID.DEFAULT;
    }

    private void initAdventureMode() {

    }

    private void createAndFillSlots(final int countRows, final int countColumns) {

        int revolution = 0;

        final Vector2 firstPosition;

        if(classicMode) {
            firstPosition = new Vector2(.5f, .5f);
        } else {
            firstPosition = new Vector2(.5f, 3.1f);
            initAdventureMode();
        }

        Vector2 previousPosition = firstPosition;

        for(int i = 0; i < countRows; i++) { // height
            for (int x = 0; x < countColumns; x++) { // width

                final Vector2 nextPosition;

                if(x != 0) {
                    nextPosition = new Vector2(previousPosition.x + 1, previousPosition.y);
                } else {
                    nextPosition = previousPosition;
                }

                slots.add(new Vector2(nextPosition.x, nextPosition.y));

                final Random random = new Random();
                final int gemColor = random.nextInt(7);
                final Gem gem = new Gem(gemTextures[gemColor], gemColor, revolution, this);

                gem.positionInRow = x;
                gem.positionInColumn = i;

                gem.setXY(nextPosition.x, nextPosition.y);

                gems.add(gem);
                stage.addActor(gem);

                previousPosition = nextPosition;

                revolution++;
            }

            previousPosition.x = firstPosition.x;

            if (slots.size() < countColumns * countRows) {
                previousPosition.y += 1;
            }
        }
    }

    public void swapGems(Gem origin, Gem destination) {

        final Vector2 originSlot = slots.get(origin.GemIndex);
        final Vector2 destinationSlot;

        if(destination.GemIndex > (rows * columns)) {
            // out of slots<> bounds
            destinationSlot = new Vector2(destination.getX(), destination.getY());
        } else {
            destinationSlot = slots.get(destination.GemIndex);
        }

        gems.swap(gems.indexOf(origin, true), gems.indexOf(destination, true));

        origin.GemIndex = gems.indexOf(origin, true);
        destination.GemIndex = gems.indexOf(destination, true);

        final int originPositionInRow = origin.positionInRow;
        final int originPositionInColumn = origin.positionInColumn;

        origin.positionInRow = destination.positionInRow;
        origin.positionInColumn = destination.positionInColumn;

        destination.positionInRow = originPositionInRow;
        destination.positionInColumn = originPositionInColumn;

        destination.addAction(Actions.moveTo(originSlot.x, originSlot.y, gemSwapTime));
        origin.addAction(Actions.moveTo(destinationSlot.x, destinationSlot.y, gemSwapTime));

//        if (slots.size > rows * columns) {
//            slots.begin();
//            slots.removeIndex(slots.size - 1);
//            slots.end();
//        }

    }

    public void checkBoundsThenSwap(final float mouseUpAtX, final float mouseUpAtY, final int index) {

        // TODO: refactor

        if     (mouseUpAtX > 1.1f
                && mouseUpAtY < 1
                && mouseUpAtY > -1
                && index != gems.size - 1
                && index % columns != columns - 1) {
            // MOVE RIGHT ->
            swapGems(gems.get(index), gems.get(index + 1));
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    matchFound = checkWholeBoardForMatches();
                    if(!matchFound) {
                        swapGems(gems.get(index), gems.get(index + 1));
                    }
                }
            }, gemSwapTime);

        } else if (mouseUpAtX < -0.1f
                && mouseUpAtY < 1
                && mouseUpAtY > -1
                && index % columns != 0) {
            // MOVE LEFT <-
            swapGems(gems.get(index), gems.get(index - 1));
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    matchFound = checkWholeBoardForMatches();
                    if(!matchFound) {
                        swapGems(gems.get(index), gems.get(index - 1));
                    }
                }
            }, gemSwapTime);

        } else if (mouseUpAtX < 1
                && mouseUpAtX > -1
                && mouseUpAtY < -0.3f
                && index - columns > 0) {
            // MOVE DOWN v
            swapGems(gems.get(index), gems.get(index - columns));
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    matchFound = checkWholeBoardForMatches();
                    if(!matchFound) {
                        swapGems(gems.get(index), gems.get(index - columns));
                    }
                }
            }, gemSwapTime);

        } else if (mouseUpAtX < 1
                && mouseUpAtX > -1
                && mouseUpAtY > 0.5f
                && index + columns < gems.size -1) {
            // MOVE UP ^
            swapGems(gems.get(index), gems.get(index + columns));
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    matchFound = checkWholeBoardForMatches();
                    if(!matchFound) {
                        swapGems(gems.get(index), gems.get(index + columns));
                    }
                }
            }, gemSwapTime);
        }
    }

    public void destroy(ArrayList<Gem> gemsToDestroy) {

        for(Gem gem : gemsToDestroy) {

            gem.setToBlank();

        }

        checkIfGemsShouldBeDropped();

        int swapTimer = (int) Math.ceil(timeToCompleteSwaps);

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                Gdx.app.log("timer", "fired");
                checkWholeBoardForMatches();
                timeToCompleteSwaps = 0f;
            }
        }, swapTimer);
    }

    public void checkIfGemsShouldBeDropped() {

        for(final Gem gem : gems) {
            if(gem.GemColor == 7) {

                if(classicMode) {
                    // standard bejewelled style, gems fall from the top down

                } else {
                    // adventure mode, gems come from the bottom up
                    if(gem.positionInColumn == 0) {
                        // need to spawn a new gem,
                        // TODO: newGem pooling

                        final Random random = new Random();
                        final int gemColor = random.nextInt(7);
                        final int newIndex = gems.size + 1;
                        final Gem newGem = new Gem(gemTextures[gemColor], gemColor, newIndex, this);

                        newGem.positionInColumn = gem.positionInColumn;
                        newGem.positionInRow = gem.positionInRow;

                        newGem.setXY(gem.getX(), gem.getY() - 2f);

                        gems.add(newGem);
                        stage.addActor(newGem);

                        // swap() the blank gem with the new gem below it,
                        swapGems(gem, newGem);
                        timeToCompleteSwaps += gemSwapTime;

                        // and safely remove() the blank gem to end the loop!

                        gems.begin();
                        gems.removeValue(gem, true);
                        gems.end();

                        gem.remove();

                    } else {
                        // just swap the blank gem with the gem below it and repeat loop
                        swapGems(gem, gems.get(gem.GemIndex - columns));
                        timeToCompleteSwaps += gemSwapTime;

                         Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                checkIfGemsShouldBeDropped();
                            }
                        }, 0.06f);

                        break;
                    }
                }
            }
        }

//        timeToCompleteSwaps *= gemSwapTime;
//
//        // checkWholeBoardForMatches();
//        Timer.schedule(new Timer.Task(){
//            @Override
//            public void run() {
//                Gdx.app.log("timer", "fired");
//                checkWholeBoardForMatches();
//            }
//        }, 5);

    }

    public String gemColorAsString(int color) { // primarily for human-readable debugging
        String colorAsString = "";
        switch(color) {
            case 0:
                colorAsString = "Green";
                break;
            case 1:
                colorAsString = "Purple";
                break;
            case 2:
                colorAsString = "Red";
                break;
            case 3:
                colorAsString = "Orange";
                break;
            case 4:
                colorAsString = "Yellow";
                break;
            case 5:
                colorAsString = "Blue";
                break;
            case 6:
                colorAsString = "Clear";
                break;
        }
        return colorAsString;
    }

    public void look(@NotNull Gem gem) {
        horizontalMatchLength = 0;
        verticalMatchLength = 0;
        matchesForThisGem = new ArrayList<>();

//        final String color = gemColorAsString(gem.GemColor);

//        Gdx.app.log("reader", "I'm looking for " + color + " gems now.");
//        Gdx.app.log("reader", "From index " + gem.GemIndex + ", I see: ");

        lookLeft(gem);
        lookRight(gem);
        lookDown(gem);
        lookUp(gem);

        horizontalMatchLength = leftMatches.size() + rightMatches.size();
        verticalMatchLength = upMatches.size() + downMatches.size();

        // Gdx.app.log("reader", "HML: " + horizontalMatchLength + ", VML: " + verticalMatchLength);

        if(horizontalMatchLength >= 2) {
            matchFound = true;
//            Gdx.app.log("matchFound", "Good horizontal matches.");
            sendToDestroy.addAll(leftMatches);
            sendToDestroy.addAll(rightMatches);
        }

        if(verticalMatchLength >= 2) {
            matchFound = true;
//            Gdx.app.log("matchFound", "Good vertical matches.");
            sendToDestroy.addAll(upMatches);
            sendToDestroy.addAll(downMatches);
        }
    }

    private void lookUp(@NotNull Gem gem) {
        final int distanceToBottomBound = gem.positionInColumn;
        final int distanceToTopBound = rows - distanceToBottomBound - 1;
        upMatches = new ArrayList<>();

        // Gdx.app.log("lookUp", "There are " + distanceToTopBound + " gems above me.");

        for(int u = 0; u <= distanceToTopBound; u++) {

            try {
                final Gem target = gems.get(gem.GemIndex + (u * columns));

                if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "up: " + target.GemIndex + ", and it's " + gemColorAsString(target.GemColor));
                }
                if(target.GemColor == gem.GemColor && target.GemIndex != gem.GemIndex) {
//                        Gdx.app.log("reader", "I call that an up match!");
                        verticalMatchLength++;
                        upMatches.add(target);
                } else if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "No match up.");
                    break;
                }
            } catch(Exception e) { /* uwu */ }
        }
    }

    private void lookDown(@NotNull Gem gem) {
        final int distanceToBottomBound = gem.positionInColumn;
        downMatches = new ArrayList<>();

       // Gdx.app.log("lookDown", "There are " + distanceToBottomBound + " gems below me.");

        for(int b = 0; b <= distanceToBottomBound; b++) {

            try {
                final Gem target = gems.get(gem.GemIndex - (b * columns));

                if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "down: " + target.GemIndex + ", and it's " + gemColorAsString(target.GemColor));
                }
                if(target.GemColor == gem.GemColor && target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "I call that a down match!");
                    verticalMatchLength++;
                    downMatches.add(target);
                } else if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "No match down.");
                    break;
                }
            } catch(Exception e) {
//                Gdx.app.log("reader", "broke down :(");
            }
        }
    }

    private void lookRight(@NotNull Gem gem) {
        final int distanceToLeftBound = gem.positionInRow;
        final int distanceToRightBound = columns - distanceToLeftBound - 1;
        rightMatches = new ArrayList<>();

//        Gdx.app.log("lookRight", "There are " + distanceToRightBound + " gems to my right.");

        for(int r = 0; r <= distanceToRightBound; r ++) {

            try {
                final Gem target = gems.get(gem.GemIndex + r);

                if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "right: " + target.GemIndex + ", and it's " + gemColorAsString(target.GemColor));
                }
                if (target.GemColor == gem.GemColor && target.GemIndex != gem.GemIndex) {
//                        Gdx.app.log("reader", "I call that a right-match!");
                        horizontalMatchLength++;
                        rightMatches.add(target);
                } else if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "No match to the right.");
                    break;
                }
            } catch (Exception e) { /* uwu */ }
        }
    }

    private void lookLeft(@NotNull Gem gem) {
        final int distanceToLeftBound = gem.positionInRow;
        leftMatches = new ArrayList<>();

        // Gdx.app.log("lookLeft", "There are " + distanceToLeftBound + " gems to my left.");

        for(int l = 0; l <= distanceToLeftBound; l++) {

            try {
                final Gem target = gems.get(gem.GemIndex - l);

                if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "left: " + target.GemIndex + ", and it's " + gemColorAsString(target.GemColor));
                }
                if (target.GemColor == gem.GemColor && target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "I call that a left match!");
                    horizontalMatchLength++;
                    leftMatches.add(target);
                } else if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "No match to the left.");
                    break;
                }
            } catch(Exception e) {
//                Gdx.app.log("break", "loop broken on revolution: " + l );
            }
        }
    }

    public boolean checkWholeBoardForMatches() {
        matchFound = false;
        sendToDestroy = new ArrayList<>();

        for(Gem gem : gems) { look(gem); }

        if(matchFound) {
            destroy(sendToDestroy);
           // checkWholeBoardForMatches();
        }

        return matchFound;
    }

    private void loadMap() {
        // Sizes default to 6 & 8 in adventure mode; but this can be changed for any given stage
        rows = 6;
        columns = 8;

        switch (campaignLevelID) {
            case ICE_1:
            case ICE_2:
                matchMap = new TmxMapLoader().load("maps/ice_debug.tmx");
                break;
            case FIRE_1:
            case FIRE_2:
            case VOID_1:
            case VOID_2:
            case STONE_1:
            case STONE_2:
            case NATURE_1:
            case NATURE_2:
            case CLASSIC_1:
            case CLASSIC_2:
            case ELECTRIC_1:
            case ELECTRIC_2:

            case DEFAULT:
                matchMap = new TmxMapLoader().load("testMap.tmx");
                rows = 5;
                columns = 7;
                break;
        }

    }

    private void initTextures() {
        Texture gemSpriteSheet = new Texture(Gdx.files.internal("gem_set.png"));
        gemTextures = new TextureRegion[]{
                new TextureRegion(gemSpriteSheet, 160, 0, 32, 32),  // GREEN 0
                new TextureRegion(gemSpriteSheet, 128, 64, 32, 32), // PURPLE 1
                new TextureRegion(gemSpriteSheet, 32, 128, 32, 32), // RED 2
                new TextureRegion(gemSpriteSheet, 0, 192, 32, 32),  // ORANGE 3
                new TextureRegion(gemSpriteSheet, 0, 288, 32, 32),  // YELLOW 4
                new TextureRegion(gemSpriteSheet, 160, 320, 32, 32),// BLUE 5
                new TextureRegion(gemSpriteSheet, 160, 384, 32, 32) // CLEAR 6, not to be confused with BLANK 7
        };
    }

    @Override
    public void show() {
        horizontalMatchLength = 0;
        verticalMatchLength = 0;

        camera = new OrthographicCamera();
        slots = new ArrayList<Vector2>();
        gems = new DelayedRemovalArray<Gem>();
        sendToDestroy = new ArrayList<Gem>();

        classicMode=false; // debug
        campaignLevelID = CampaignLevelID.ICE_1; // debug

        initTextures();

        loadMap();

        orthoMapRenderer = new OrthogonalTiledMapRenderer(matchMap, 1/32f);

        final int worldWidth = columns + 1;
        final double worldHeight = Math.floor(worldWidth * 1.77777778f); // approx 9:16

        FitViewport fitViewport = new FitViewport(worldWidth, (int) worldHeight);
        camera.setToOrtho(false, worldWidth, (int) worldHeight);

        stage = new Stage(fitViewport);

        createAndFillSlots(rows, columns);

        Gdx.input.setInputProcessor(stage);

        camera.update();

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                checkWholeBoardForMatches();
            }
        }, 1);
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
