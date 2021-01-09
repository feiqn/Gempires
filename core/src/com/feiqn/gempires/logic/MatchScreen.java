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
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.enemies.Enemy;
import com.feiqn.gempires.logic.characters.enemies.water.WaterWizard;
import com.feiqn.gempires.logic.items.Tornado;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.Element;
import com.feiqn.gempires.models.Formation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
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

    public Group gemGroup,
                 topLayer; // TODO: set top layer for attack tokens to display on

    public boolean matchFound,
                   classicMode,
                   allowUserInput;

    public int rows,
               columns,
               horizontalMatchLength,
               verticalMatchLength;

    private int naturePower,
                voidPower,
                firePower,
                waterPower,
                stonePower,
                electricPower,
                enemyDifficulty,
                wavesCleared,
                enemiesOnScreen;

    private TextureRegion[] gemTextures,
                            attackTokenTextures;

    public CampaignLevelID campaignLevelID;

    public ArrayList<Gem> matchesForThisGem,
                          sendToDestroy,

                          leftMatches,
                          rightMatches,
                          upMatches,
                          downMatches;


    // TODO: Current implementation of Gems<> is poor. Switch to POOLING will be mandatory soon.
    public DelayedRemovalArray<Gem> gems;

    private DelayedRemovalArray<Enemy> enemies;

    private HashMap<Bestiary, Boolean> enemyIsInitialized;
    private HashMap<Integer, Formation> waveFormations;

    private ArrayList<Integer> waves;

    public ArrayList<Vector2> slots;

    private TextureRegion waterWizardTextureRegion;

    public MatchScreen(GempiresGame game, Boolean classic, CampaignLevelID levelID){
        this.game = game;
        this.classicMode = classic;
        this.campaignLevelID = levelID;
    }
    public MatchScreen(GempiresGame game) {
        // new MatchScreen(game, true, CampaignLevelID.DEFAULT);
        this.game = game;
        this.classicMode = true;
        this.campaignLevelID = CampaignLevelID.DEBUG;
    }

    private void initAdventureMode(ArrayList<Bestiary> passToInitEnemies) {
        // Called by loadMap()

        waterPower = 0;
        firePower = 0;
        stonePower = 0;
        electricPower = 0;
        voidPower = 0;
        naturePower = 0;

        enemyDifficulty = 0;
        enemiesOnScreen = 0;
        wavesCleared = 0;
        waves = new ArrayList<>();
        waveFormations = new HashMap<>();
        enemies = new DelayedRemovalArray<>();
        enemyIsInitialized = new HashMap<Bestiary, Boolean>(Bestiary.values().length);

        for(int i = 0; i < Bestiary.values().length; i++) {
            enemyIsInitialized.put(Bestiary.values()[i], false);
        }

        initEnemies(passToInitEnemies);
    }

    private void initEnemies(ArrayList<Bestiary> beasts) {
        // Called by initAdventureMode()

        for(Bestiary beast : beasts) {
            switch(beast) {
                case WATER_WIZARD:
                    if(!enemyIsInitialized.get(Bestiary.WATER_WIZARD)) {
                        final Texture wizardSpriteSheet = new Texture(Gdx.files.internal("characters/enemies/wizard_spritesheet.png"));
                        waterWizardTextureRegion = new TextureRegion(wizardSpriteSheet, 64, 288, 64, 64);

                        enemyIsInitialized.put(Bestiary.WATER_WIZARD, true);
                    }

                    final WaterWizard waterWizard = new WaterWizard(waterWizardTextureRegion);
                    enemies.add(waterWizard);
                    break;

                case ICE_FIEND:
                    break;
            }
        }

        for(Enemy enemy : enemies) {
            enemy.scaleToLevel(enemyDifficulty);
        }

    }

    private void createAndFillSlots(final int countRows, final int countColumns) {
        // Called by show()

        int revolution = 0;

        final Vector2 firstPosition;

        if(classicMode) {
            firstPosition = new Vector2(.5f, .5f);
        } else {
            firstPosition = new Vector2(.5f, 3.05f);
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

    private Element translateGemColorToElement(Integer color) {
        // this class solely exists as a stop-gap until Gem class refactor is complete

        switch (color) {
            case 0:
                return Element.NATURE;
            case 1:
                return Element.VOID;
            case 2:
                return Element.FIRE;
            case 3:
                return Element.STONE;
            case 4:
                return Element.ELECTRIC;
            case 5:
                return Element.WATER;
            case 6:
                return Element.PURE;
            case 7:
                break;
        }

        return null;
    }

    public void destroy(ArrayList<Gem> gemsToDestroy) {

        for(Gem gem : gemsToDestroy) {
            final Element gemElement = translateGemColorToElement(gem.GemColor);
            if(gem.GemColor != 7) {
                final AttackToken token = new AttackToken(attackTokenTextures[gem.GemColor], gemElement);

//            switch (gem.GemColor) {
//                // TODO: create attack tokens and send them up at the enemies
//
//                case 0:
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    break;
//                case 3:
//                case 4:
//                case 5:
//                case 6:
//                    break;
//            }


                token.setX(gem.getX());
                token.setY(gem.getY());

                stage.addActor(token);

                token.addAction(Actions.moveTo(token.getX(), Gdx.graphics.getHeight(), gemSwapTime));

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        token.remove();
                    }
                }, gemSwapTime);
            }
            gem.setToBlank();
        }

        checkIfGemsShouldBeDropped();

        int swapTimer = (int) Math.ceil(timeToCompleteSwaps); // TODO: not perfect

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

        // TODO: check for No Possible Matches
        for(Gem gem : gems) { look(gem); }

        if(matchFound) {
            destroy(sendToDestroy);
        }

        return matchFound;
    }

    private void loadMap() {
        // Called by show()

        // Sizes default to 6 & 8 for adventure mode; but this can be changed for any given stage
        rows = 6;
        columns = 8;

        ArrayList<Bestiary> neededEnemies = new ArrayList<>();

        switch (campaignLevelID) {
            case WATER_1:
            case WATER_2:
                matchMap = new TmxMapLoader().load("maps/ice_debug.tmx");
                enemyDifficulty = 2;

                neededEnemies.add(Bestiary.WATER_WIZARD);
                initAdventureMode(neededEnemies);

                waves.add(1); // waves contains the NUMBER OF ENEMIES at each wave, with each index being 1 wave
                waveFormations.put(0, Formation.ONE_CENTER); // waveFormations contains the layout / arrangement of enemies at each wave, with each key relating to wave number, indexed from zero

                /*
                 * The math for adding waves of enemies here must always add up.
                 * For example, if 10 enemies are added, and 3 waves, the total
                 * number of enemies deployed over 3 waves must equal 10. For example:
                 * 3 + 3 + 4, 2 + 5 + 3, etc.
                */
                break;

            case WATER_3:
            case WATER_4:
            case WATER_5:
            case FIRE_1:
            case FIRE_2:
            case VOID_1:
            case VOID_2:
            case STONE_1:
            case STONE_2:
            case NATURE_1:
            case NATURE_2:
            case ELECTRIC_1:
            case ELECTRIC_2:
            case CLASSIC_1:
            case CLASSIC_2:

            case DEBUG:
                matchMap = new TmxMapLoader().load("testMap.tmx");
                rows = 5;
                columns = 7;
                break;
        }

    }

    private void destroyEnemy(Enemy enemy) {
        // TODO: destroy enemy

        enemies.removeValue(enemy, true);

        enemiesOnScreen--;
        if(enemiesOnScreen <= 0) {
            wavesCleared++;
            deployEnemyWave();
        }
    }

    private void deployEnemyWave() {
        // Called by show()
        // Called by destroyEnemy() when a wave is cleared


        if(waves.size() > 0) {

            final int enemiesLeftToDeploy = waves.get(0);
            final ArrayList<Enemy> thisWave = new ArrayList<>();
            final Formation thisFormation = waveFormations.get(wavesCleared);

            enemiesOnScreen = enemiesLeftToDeploy;

            for(int i = 0; i < waves.get(0); i++) {
                final Enemy enemyToDeploy = enemies.get(i);
                stage.addActor(enemyToDeploy);
            }

             switch (thisFormation) {
                 case ONE_CENTER:
                     enemies.get(0).setSize(6,6);
                     enemies.get(0).setXY(1.5f, 9.5f);
                     break;
                 case TWO_IN_BACK:
                 case TWO_IN_FRONT:
                 case TWO_STAGGERED_LEFT:
                 case TWO_STAGGERED_RIGHT:
                 case THREE_ASCENDING:
                 case THREE_DESCENDING:
                 case THREE_IN_FRONT:
                 case FOUR_IN_FRONT:
                 case FOUR_REVERSE_N_SHAPE:
                 case FOUR_WITH_BACK_SIDE_FLANKS:
                 case FOUR_WITH_FRONT_SIDE_FLANKS:
                 case FOUR_N_SHAPE:
                 case FIVE_M_SHAPE:
                 case FIVE_W_SHAPE:
                 case FIVE_PROTECTING_CENTER:
                 case FIVE_DESCENDING:
                 case FIVE_ASCENDING:
                     break;
             }

            waves.remove(0);
        }

    }

    private void calculateGemPower() {
        // TODO
    }

    private void layOutTeamCards() {
        // TODO
    }

    private void initGemTextures() {
        final Texture gemSpriteSheet = new Texture(Gdx.files.internal("gem_set.png"));
        gemTextures = new TextureRegion[]{
                new TextureRegion(gemSpriteSheet, 160, 0, 32, 32),  // GREEN 0
                new TextureRegion(gemSpriteSheet, 128, 64, 32, 32), // PURPLE 1
                new TextureRegion(gemSpriteSheet, 32, 128, 32, 32), // RED 2
                new TextureRegion(gemSpriteSheet, 0, 192, 32, 32),  // ORANGE 3
                new TextureRegion(gemSpriteSheet, 0, 288, 32, 32),  // YELLOW 4
                new TextureRegion(gemSpriteSheet, 160, 320, 32, 32),// BLUE 5
                new TextureRegion(gemSpriteSheet, 160, 384, 32, 32) // CLEAR 6, not to be confused with BLANK 7
        };

        if(!classicMode) {
            attackTokenTextures = new TextureRegion[] {
                new TextureRegion(gemSpriteSheet, 128, 0, 32, 32),
                new TextureRegion(gemSpriteSheet, 160, 64, 32, 32),
                new TextureRegion(gemSpriteSheet, 0, 128, 32, 32),
                new TextureRegion(gemSpriteSheet, 32, 192, 32, 32),
                new TextureRegion(gemSpriteSheet, 0, 256, 32, 32),
                new TextureRegion(gemSpriteSheet, 128, 320, 32,32),
                new TextureRegion(gemSpriteSheet, 128, 384, 32, 32)
            };
        }
    }

    @Override
    public void show() {
        horizontalMatchLength = 0;
        verticalMatchLength = 0;

        camera = new OrthographicCamera();
        slots = new ArrayList<Vector2>();
        gems = new DelayedRemovalArray<Gem>();
        sendToDestroy = new ArrayList<Gem>();

        //
        classicMode=false; // debug
        campaignLevelID = CampaignLevelID.WATER_1; // debug
        //

        initGemTextures();

        loadMap();

        orthoMapRenderer = new OrthogonalTiledMapRenderer(matchMap, 1/32f);

        final int worldWidth = columns + 1;
        final double worldHeight = Math.floor(worldWidth * 1.77777778f); // approx 9:16

        FitViewport fitViewport = new FitViewport(worldWidth, (int) worldHeight);
        camera.setToOrtho(false, worldWidth, (int) worldHeight);

        stage = new Stage(fitViewport);

        createAndFillSlots(rows, columns);

        if(!classicMode) {
            deployEnemyWave();
            calculateGemPower();
        }

        //
        final Tornado debugShuffler = new Tornado(gemTextures[1], this); // debug
        debugShuffler.setY(12); //
        debugShuffler.setX(1); //
        stage.addActor(debugShuffler); //
        //


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
