package com.feiqn.gempires.logic.stages.match3;

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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.AttackToken;
import com.feiqn.gempires.logic.Gem;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.enemies.Enemy;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.characters.heroes.HeroList;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.logic.items.Tornado;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.ElementalType;
import com.feiqn.gempires.models.Formation;
import com.feiqn.gempires.models.stats.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MatchScreen extends ScreenAdapter {
    // I am a woman humbled.

    private Table rootTable,
                  gemTable,
                  enemyTable,
                  uiTable;

    public OrthographicCamera gameCamera;
    public OrthogonalTiledMapRenderer orthoMapRenderer;
    public TiledMap matchMap;

    public MapProperties mapProperties;

    public final float gemSwapTime = 0.2f;
    private float timeToCompleteSwaps = 0f;

    final public GempiresGame game;
    public Stage stage;

    public boolean matchFound,
                   possibleToMatch,
                   classicMode,
                   needToClearWave,
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
                purePower,
                enemyDifficulty,
                numberOfEnemiesOnScreen;

    public CampaignLevelID campaignLevelID;

    public ArrayList<ItemList> loot;
    public ArrayList<Vector2> slots;
    public ArrayList<Gem> matchesForThisGem,
                          sendToDestroy,

                          leftMatches,
                          rightMatches,
                          upMatches,
                          downMatches;

    // TODO: Current implementation of Gems<> is poor. Switch to POOLING will be mandatory soon.
    public  DelayedRemovalArray<Gem> gems;
    private DelayedRemovalArray<Enemy> enemiesOnScreen;
    private DelayedRemovalArray<AttackToken> attackTokensOnScreen;

    public ArrayList<Bestiary> neededEnemies;
    public Boolean firstWaveClear;
    public Boolean secondWaveClear;
    public Boolean thirdWaveClear;
    public ArrayList<HeroCard> team;

    public MatchScreen(GempiresGame game, CampaignLevelID levelID /* , Arraylist<HeroCard> team */){
        // Adventure Mode constructor
        this.game = game;
        this.classicMode = false;
        this.campaignLevelID = levelID;

        firstWaveClear = false;
        secondWaveClear = false;
        thirdWaveClear = false;
        neededEnemies = new ArrayList<>();
        this.team = game.castle.heroRoster.getTeam(game.castle.heroRoster.defaultTeam);
        final ArrayList<HeroList> neededHeroes = new ArrayList<>();
        for(HeroCard hero : team) {
            neededHeroes.add(hero.heroID);
        }
        game.gempiresAssetHandler.initialiseHeroTextures(neededHeroes);
    }
    public MatchScreen(GempiresGame game) {
        // Classic constructor
        this.game = game;
        this.classicMode = true;
        this.campaignLevelID = CampaignLevelID.CLASSIC_1;
    }

    public void initAdventureMode(ArrayList<Bestiary> passToInitEnemies) {
        // Called by child class

        waterPower = 1;
        firePower = 1;
        stonePower = 1;
        electricPower = 1;
        voidPower = 1;
        naturePower = 1;
        purePower = 1;

        enemyDifficulty = 0;
        numberOfEnemiesOnScreen = 0;
        loot = new ArrayList<>();
        enemiesOnScreen = new DelayedRemovalArray<>();
        attackTokensOnScreen = new DelayedRemovalArray<>();

        game.gempiresAssetHandler.initialiseEnemyTextures(passToInitEnemies);
    }

    public Gem generateRandomNewGem() {
        final Random random = new Random();
        final int gemColor = random.nextInt(7);
        return new Gem(this, gemColor);
    }

    private void createAndFillSlots(final int countRows, final int countColumns) {
        // Called by show()

        int revolution = 0;

        gemTable.defaults().width(1).height(1);


        for(int h = 0; h < rows; h++) { // height in rows
            for(int w = 0; w < columns; w++) { // width in columns
//                final Gem gem = generateRandomNewGem();
//                gemTable.add(gem);

                final Random random = new Random();
                final int gemColor = random.nextInt(7);
                final Gem gem = new Gem(game.gempiresAssetHandler.gemTextures[gemColor], gemColor, revolution, this);

                gem.positionInRow = h;
                gem.positionInColumn = w;

                gemTable.add(gem);
                revolution++;
            }
            gemTable.row();
        }

//        final Vector2 firstPosition;
//        if(classicMode) {
//            firstPosition = new Vector2(.5f, .5f);
//        } else {
//            firstPosition = new Vector2(.5f, 3.05f);
//        }
//        Vector2 previousPosition = firstPosition;
//
//        for(int i = 0; i < countRows; i++) { // height
//            for (int x = 0; x < countColumns; x++) { // width
//
//                final Vector2 nextPosition;
//
//                if(x != 0) {
//                    nextPosition = new Vector2(previousPosition.x + 1, previousPosition.y);
//                } else {
//                    nextPosition = previousPosition;
//                }
//
//                slots.add(new Vector2(nextPosition.x, nextPosition.y));
//
//                final Random random = new Random();
//                final int gemColor = random.nextInt(7);
//                final Gem gem = new Gem(game.gempiresAssetHandler.gemTextures[gemColor], gemColor, revolution, this);
//
//                gem.positionInRow = x;
//                gem.positionInColumn = i;
//
//                gem.setPosition(nextPosition.x, nextPosition.y);
//
//                gems.add(gem);
//                stage.addActor(gem);
//
//                previousPosition = nextPosition;
//
//                revolution++;
//            }
//
//            previousPosition.x = firstPosition.x;
//
//            if (slots.size() < countColumns * countRows) {
//                previousPosition.y += 1;
//            }
//        }
    }

    public void swapGems(Gem origin, Gem destination) {

//        allowUserInput = false;

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

//        Timer.schedule(new Timer.Task(){
//            @Override
//            public void run() {
//                allowUserInput = true;
//            }
//        }, gemSwapTime);

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
                        Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                allowUserInput = true;
                            }
                        }, gemSwapTime);
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
                        Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                allowUserInput = true;
                            }
                        }, gemSwapTime);
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
                        Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                allowUserInput = true;
                            }
                        }, gemSwapTime);
                    }
                }
            }, gemSwapTime);

        } else if (mouseUpAtX < 1
                && mouseUpAtX > -1
                && mouseUpAtY > 0.8f
                && index + columns < gems.size -1) {
            // MOVE UP ^
            swapGems(gems.get(index), gems.get(index + columns));
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    matchFound = checkWholeBoardForMatches();
                    if(!matchFound) {
                        swapGems(gems.get(index), gems.get(index + columns));
                        Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                allowUserInput = true;
                            }
                        }, gemSwapTime);
                    }
                }
            }, gemSwapTime);
        } else {
            allowUserInput = true;
        }
    }

    private ElementalType translateGemColorToElement(Integer color) {
        // this class solely exists as a stop-gap until Gem class refactor is complete

        switch (color) {
            case 0:
                return ElementalType.NATURE;
            case 1:
                return ElementalType.VOID;
            case 2:
                return ElementalType.FIRE;
            case 3:
                return ElementalType.STONE;
            case 4:
                return ElementalType.ELECTRIC;
            case 5:
                return ElementalType.WATER;
            case 6:
                return ElementalType.PURE;
            case 7:
                break;
        }

        return null;
    }

    public void destroyGems(ArrayList<Gem> gemsToDestroy) {

        for(Gem gem : gemsToDestroy) {
            if(!classicMode) {
                final ElementalType gemElementalType = translateGemColorToElement(gem.GemColor);
                if (gem.GemColor != 7) {
                    final AttackToken token = new AttackToken(game.gempiresAssetHandler.attackTokenTextures[gem.GemColor], gemElementalType);

                    token.setX(gem.getX());
                    token.setY(gem.getY());

                    stage.addActor(token);

                    attackTokensOnScreen.add(token);

                    token.addAction(Actions.moveTo(token.getX(), gameCamera.viewportHeight, 1));

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                token.remove();
                                attackTokensOnScreen.begin();
                                attackTokensOnScreen.removeValue(token, true);
                                attackTokensOnScreen.end();
                            } catch (Exception ignored) {

                            }
                        }
                    }, 2);
                }
            }
            gem.setToBlank();
        }

        checkIfGemsShouldBeDropped();

        if(timeToCompleteSwaps < 1) {
            timeToCompleteSwaps = (int) Math.ceil(timeToCompleteSwaps);
        }

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                Gdx.app.log("timer", "fired");
                matchFound = checkWholeBoardForMatches();
                if(!matchFound) {
                    allowUserInput = true;
                }
                timeToCompleteSwaps = 0f;
            }
        }, timeToCompleteSwaps);
    }

    public void checkIfGemsShouldBeDropped() {
        /* This checks for "blank" gems on the board, which have been destroyed.
        *  It then decides weather there is an existing gem above/below which should be
        *  "dropped" into this empty space. This repeats until all existing gems have been
        *  dropped into place, and then new gems are spawned to fill the empty space.
        */
        for(final int[] g = {0}; g[0] < gems.size; g[0]++) {
            final Gem gem = gems.get(g[0]);
            if(gem.GemColor == 7) {
                if(classicMode) {
                    // standard bejewelled style, gems fall from the top down
                    // TODO: broken
                    if(gem.positionInColumn == rows - 1) {
                        // need to spawn a new gem,
                        // TODO: newGem pooling
                        Gdx.app.log("", "spawn new gem");

                        final Random random = new Random();
                        final int gemColor = random.nextInt(7);
                        final int newIndex = gems.size + 1;
                        final Gem newGem = new Gem(game.gempiresAssetHandler.gemTextures[gemColor], gemColor, newIndex, this);

                        newGem.positionInColumn = gem.positionInColumn;
                        newGem.positionInRow = gem.positionInRow;

                        newGem.setPosition(gem.getX(), gem.getY() + 2);

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
                        // just swap the blank gem with the gem above it and repeat loop
                        swapGems(gem, gems.get(gem.GemIndex + columns));
                        timeToCompleteSwaps += gemSwapTime;

                        Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                checkIfGemsShouldBeDropped();
                            }
                        }, 0.06f);

                        break;
                    }

                } else {
                    // adventure mode, gems come from the bottom up
                    if(gem.positionInColumn == 0) {
                        // need to spawn a new gem,
                        // TODO: newGem pooling

                        final Random random = new Random();
                        final int gemColor = random.nextInt(7);
                        final int newIndex = gems.size + 1;
                        final Gem newGem = new Gem(game.gempiresAssetHandler.gemTextures[gemColor], gemColor, newIndex, this);

                        newGem.positionInColumn = gem.positionInColumn;
                        newGem.positionInRow = gem.positionInRow;

                        newGem.setPosition(gem.getX(), gem.getY() - 2f);

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
                        // just swap the blank gem with the gem below it and restart loop
                        swapGems(gem, gems.get(gem.GemIndex - columns));
                        timeToCompleteSwaps += gemSwapTime;

                         Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                g[0] = gems.size;
                                // Thank you IntelliJ for this big brain constant array trick.
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

                if(target.GemColor == gem.GemColor && target.GemIndex != gem.GemIndex) {
//                        Gdx.app.log("reader", "I call that an up match!");
                        verticalMatchLength++;
                        upMatches.add(target);
                } else if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "No match up.");

                    if(!possibleToMatch) {

                        boolean up1DidInitialize = false;
                        boolean up2DidInitialize = false;
                        boolean left1DidInitialize = false;
                        boolean right1DidInitialize = false;
                        boolean down1DidInitialize = false;

                        Gem down1;
                        Gem right1;
                        Gem left1;
                        Gem up2;
                        Gem up1;

                        try {
                            up1 = gems.get(gem.GemIndex + columns);
                            up1DidInitialize = true;
                        } catch(Exception e){
                            // this assignment is just to keep the variable Not Null, and in this case will never be called
                            up1 = gem;
                        }

                        try {
                            up2 = gems.get(gem.GemIndex + (columns*2));
                            up2DidInitialize = true;
                        } catch(Exception e){
                            up2 = gem;
                        }

                        try {
                            left1 = gems.get(gem.GemIndex - 1);
                            left1DidInitialize = true;
                        } catch(Exception e){
                            left1 = gem;
                        }

                        try {
                            right1 = gems.get(gem.GemIndex + 1);
                            right1DidInitialize = true;
                        } catch(Exception e){
                            right1 = gem;
                        }

                        try {
                            down1 = gems.get(gem.GemIndex -  columns);
                            down1DidInitialize = true;
                        } catch(Exception e){
                            down1 = gem;
                        }

                        boolean up2Matches = false;
                        boolean left1Matches = false;
                        boolean right1Matches = false;
                        boolean down1Matches = false;

                        if(up1DidInitialize && up2DidInitialize){
                            if(up2.GemColor == up1.GemColor) {
                                // confirmed set of 2, only need 1 more to match
                                up2Matches = true;
                            }
                        }

                        if(left1DidInitialize && up1DidInitialize) {
                            if(left1.GemColor == up1.GemColor) {
                                left1Matches = true;
                            }
                        }

                        if(up1DidInitialize && right1DidInitialize) {
                            if(right1.GemColor == up1.GemColor) {
                                right1Matches = true;
                            }
                        }

                        if(up1DidInitialize && down1DidInitialize) {
                            if(down1.GemColor == up1.GemColor) {
                                down1Matches = true;
                            }
                        }

                        if(up2Matches) {
                            // only need 1 more to match
                            if(down1Matches || left1Matches || right1Matches) {
                                possibleToMatch = true;
                            }
                        } else {
                            if(down1Matches && left1Matches
                            || down1Matches && right1Matches
                            || right1Matches && left1Matches) {
                                possibleToMatch = true;
                            }
                        }
                    }
                    break;
                }
            } catch(Exception ignored) {}
        }
    }

    private void lookDown(@NotNull Gem gem) {
        final int distanceToBottomBound = gem.positionInColumn;
        downMatches = new ArrayList<>();

       // Gdx.app.log("lookDown", "There are " + distanceToBottomBound + " gems below me.");

        for(int b = 0; b <= distanceToBottomBound; b++) {

            try {
                final Gem target = gems.get(gem.GemIndex - (b * columns));

                if(target.GemColor == gem.GemColor && target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "I call that a down match!");
                    verticalMatchLength++;
                    downMatches.add(target);
                } else if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "No match down.");

                    if(!possibleToMatch) {

                        boolean up1DidInitialize = false;
                        boolean down2DidInitialize = false;
                        boolean left1DidInitialize = false;
                        boolean right1DidInitialize = false;
                        boolean down1DidInitialize = false;

                        Gem down1;
                        Gem right1;
                        Gem left1;
                        Gem down2;
                        Gem up1;

                        try {
                            up1 = gems.get(gem.GemIndex + columns);
                            up1DidInitialize = true;
                        } catch(Exception e){
                            // this assignment is just to keep the variable Not Null, and in this case will never be called
                            up1 = gem;
                        }

                        try {
                            down2 = gems.get(gem.GemIndex - (columns*2));
                            down2DidInitialize = true;
                        } catch(Exception e){
                            down2 = gem;
                        }

                        try {
                            left1 = gems.get(gem.GemIndex - 1);
                            left1DidInitialize = true;
                        } catch(Exception e){
                            left1 = gem;
                        }

                        try {
                            right1 = gems.get(gem.GemIndex + 1);
                            right1DidInitialize = true;
                        } catch(Exception e){
                            right1 = gem;
                        }

                        try {
                            down1 = gems.get(gem.GemIndex -  columns);
                            down1DidInitialize = true;
                        } catch(Exception e){
                            down1 = gem;
                        }

                        boolean down2Matches = false;
                        boolean left1Matches = false;
                        boolean right1Matches = false;
                        boolean down1Matches = false;

                        if(up1DidInitialize && down2DidInitialize){
                            if(down2.GemColor == up1.GemColor) {
                                // confirmed set of 2, only need 1 more to match
                                down2Matches = true;
                            }
                        }

                        if(left1DidInitialize && up1DidInitialize) {
                            if(left1.GemColor == up1.GemColor) {
                                left1Matches = true;
                            }
                        }

                        if(up1DidInitialize && right1DidInitialize) {
                            if(right1.GemColor == up1.GemColor) {
                                right1Matches = true;
                            }
                        }

                        if(up1DidInitialize && down1DidInitialize) {
                            if(down1.GemColor == up1.GemColor) {
                                down1Matches = true;
                            }
                        }

                        if(down2Matches) {
                            // only need 1 more to match
                            if(down1Matches || left1Matches || right1Matches) {
                                possibleToMatch = true;
                            }
                        } else {
                            if(down1Matches && left1Matches
                            || down1Matches && right1Matches
                            || right1Matches && left1Matches) {
                                possibleToMatch = true;
                            }
                        }
                    }
                    break;
                }
            } catch(Exception ignored) {}
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

                if (target.GemColor == gem.GemColor && target.GemIndex != gem.GemIndex) {
//                        Gdx.app.log("reader", "I call that a right-match!");
                        horizontalMatchLength++;
                        rightMatches.add(target);
                } else if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "No match to the right.");

                    if(!possibleToMatch) {

                        boolean up1DidInitialize = false;
                        boolean right2DidInitialize = false;
                        boolean left1DidInitialize = false;
                        boolean right1DidInitialize = false;
                        boolean down1DidInitialize = false;

                        Gem down1;
                        Gem right1;
                        Gem left1;
                        Gem right2;
                        Gem up1;

                        try {
                            up1 = gems.get(gem.GemIndex + columns);
                            up1DidInitialize = true;
                        } catch(Exception e){
                            // this assignment is just to keep the variable Not Null, and in this case will never be called
                            up1 = gem;
                        }

                        try {
                            right2 = gems.get(gem.GemIndex + 2);
                            right2DidInitialize = true;
                        } catch(Exception e){
                            right2 = gem;
                        }

                        try {
                            left1 = gems.get(gem.GemIndex - 1);
                            left1DidInitialize = true;
                        } catch(Exception e){
                            left1 = gem;
                        }

                        try {
                            right1 = gems.get(gem.GemIndex + 1);
                            right1DidInitialize = true;
                        } catch(Exception e){
                            right1 = gem;
                        }

                        try {
                            down1 = gems.get(gem.GemIndex -  columns);
                            down1DidInitialize = true;
                        } catch(Exception e){
                            down1 = gem;
                        }

                        boolean right2Matches = false;
                        boolean left1Matches = false;
                        boolean right1Matches = false;
                        boolean down1Matches = false;

                        if(up1DidInitialize && right2DidInitialize){
                            if(right2.GemColor == up1.GemColor) {
                                // confirmed set of 2, only need 1 more to match
                                right2Matches = true;
                            }
                        }

                        if(left1DidInitialize && up1DidInitialize) {
                            if(left1.GemColor == up1.GemColor) {
                                left1Matches = true;
                            }
                        }

                        if(up1DidInitialize && right1DidInitialize) {
                            if(right1.GemColor == up1.GemColor) {
                                right1Matches = true;
                            }
                        }

                        if(up1DidInitialize && down1DidInitialize) {
                            if(down1.GemColor == up1.GemColor) {
                                down1Matches = true;
                            }
                        }

                        if(right2Matches) {
                            // only need 1 more to match
                            if(down1Matches || left1Matches || right1Matches) {
                                possibleToMatch = true;
                            }
                        } else {
                            if(down1Matches && left1Matches
                            || down1Matches && right1Matches
                            || right1Matches && left1Matches) {
                                possibleToMatch = true;
                            }
                        }
                    }
                    break;
                }
            } catch (Exception ignored) {}
        }
    }

    private void lookLeft(@NotNull Gem gem) {
        final int distanceToLeftBound = gem.positionInRow;
        leftMatches = new ArrayList<>();

        // Gdx.app.log("lookLeft", "There are " + distanceToLeftBound + " gems to my left.");

        for(int l = 0; l <= distanceToLeftBound; l++) {

            try {
                final Gem target = gems.get(gem.GemIndex - l);

                if (target.GemColor == gem.GemColor && target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "I call that a left match!");
                    horizontalMatchLength++;
                    leftMatches.add(target);
                } else if(target.GemIndex != gem.GemIndex) {
//                    Gdx.app.log("reader", "No match to the left.");

                    if(!possibleToMatch) {

                        boolean up1DidInitialize = false;
                        boolean left2DidInitialize = false;
                        boolean left1DidInitialize = false;
                        boolean right1DidInitialize = false;
                        boolean down1DidInitialize = false;

                        Gem down1;
                        Gem right1;
                        Gem left1;
                        Gem left2;
                        Gem up1;

                        try {
                            up1 = gems.get(gem.GemIndex + columns);
                            up1DidInitialize = true;
                        } catch(Exception e){
                            // this assignment is just to keep the variable Not Null, and in this case will never be called
                            up1 = gem;
                        }

                        try {
                            left2 = gems.get(gem.GemIndex - 2);
                            left2DidInitialize = true;
                        } catch(Exception e){
                            left2 = gem;
                        }

                        try {
                            left1 = gems.get(gem.GemIndex - 1);
                            left1DidInitialize = true;
                        } catch(Exception e){
                            left1 = gem;
                        }

                        try {
                            right1 = gems.get(gem.GemIndex + 1);
                            right1DidInitialize = true;
                        } catch(Exception e){
                            right1 = gem;
                        }

                        try {
                            down1 = gems.get(gem.GemIndex -  columns);
                            down1DidInitialize = true;
                        } catch(Exception e){
                            down1 = gem;
                        }

                        boolean left2Matches = false;
                        boolean left1Matches = false;
                        boolean right1Matches = false;
                        boolean down1Matches = false;

                        if(up1DidInitialize && left2DidInitialize){
                            if(left2.GemColor == up1.GemColor) {
                                // confirmed set of 2, only need 1 more to match
                                left2Matches = true;
                            }
                        }

                        if(left1DidInitialize && up1DidInitialize) {
                            if(left1.GemColor == up1.GemColor) {
                                left1Matches = true;
                            }
                        }

                        if(up1DidInitialize && right1DidInitialize) {
                            if(right1.GemColor == up1.GemColor) {
                                right1Matches = true;
                            }
                        }

                        if(up1DidInitialize && down1DidInitialize) {
                            if(down1.GemColor == up1.GemColor) {
                                down1Matches = true;
                            }
                        }

                        if(left2Matches) {
                            // only need 1 more to match
                            if(down1Matches || left1Matches || right1Matches) {
                                possibleToMatch = true;
                            }
                        } else {
                            if(down1Matches && left1Matches
                            || down1Matches && right1Matches
                            || right1Matches && left1Matches) {
                                possibleToMatch = true;
                            }
                        }
                    }
                    break;
                }
            } catch(Exception ignored) { }
        }
    }

    public boolean checkWholeBoardForMatches() {
        matchFound = false;
        sendToDestroy = new ArrayList<>();
        possibleToMatch = false;

        for(Gem gem : gems) { look(gem); }

        if(matchFound) {
            destroyGems(sendToDestroy);
        } else if(possibleToMatch = false) {

            Gdx.app.log("No Matches", "No matches");

            final Tornado debugShuffler = new Tornado(game.gempiresAssetHandler.gemTextures[1], this);
            debugShuffler.shuffleWholeBoard();

            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    checkWholeBoardForMatches();
                }
            }, 1);
        }

        return matchFound;
    }

    public void deployHeroes(ArrayList<HeroCard> team) {
        for(int i = 0; i < team.size(); i++) {
            final HeroCard hero = team.get(i);
            if(hero != null) {
               hero.battleCard.setSize(1.8f, 3);
               switch(i) {
                   case 0:
                       hero.battleCard.setPosition(3.6f, 0);
                       break;
                   case 1:
                       hero.battleCard.setPosition(1.8f, 0);
                       break;
                   case 2:
                       hero.battleCard.setPosition(5.4f, 0);
                       break;
                   case 3:
                       break;
                   case 4:
                       hero.battleCard.setPosition(7.2f, 0);
                       break;
               }
               stage.addActor(hero.battleCard);
            }
        }
    }

    private void loadMap() {
        // Called by show()

        rows = 6;
        columns = 8;

        switch (campaignLevelID) {
            case WATER_1:
            case WATER_2:
            case WATER_3:
            case WATER_4:
            case WATER_5:
                matchMap = new TmxMapLoader().load("maps/ice_debug.tmx");
                break;
            case FIRE_1:
            case FIRE_2:
                matchMap = new TmxMapLoader().load("maps/fire_debug.tmx");
                break;
            case VOID_1:
            case VOID_2:
                matchMap = new TmxMapLoader().load("maps/void_debug.tmx");
                break;
            case STONE_1:
            case STONE_2:
                matchMap = new TmxMapLoader().load("maps/stone_debug.tmx");
                break;
            case NATURE_1:
            case NATURE_2:
                matchMap = new TmxMapLoader().load("maps/nature_debug.tmx");
                break;
            case ELECTRIC_1:
            case ELECTRIC_2:
                matchMap = new TmxMapLoader().load("maps/electric_debug.tmx");
                break;
            case CLASSIC_1:
            case CLASSIC_2:
            default:
                matchMap = new TmxMapLoader().load("testMap.tmx");
                rows = 5;
                columns = 7;
                break;
        }

    }

    private void destroyEnemy(Enemy enemy) {

        enemiesOnScreen.begin();
        enemiesOnScreen.removeValue(enemy, true);
        enemiesOnScreen.end();

        loot.addAll(enemy.rollLoot());

        enemy.remove();

        numberOfEnemiesOnScreen--;
        if(numberOfEnemiesOnScreen <= 0) {
            if(allowUserInput)  {
                clearWave();
            } else {
                needToClearWave = true;
            }
        }
    }

    public void clearWave() {
        // child class only
        // Template:

//        if(firstWaveClear && secondWaveClear && thirdWaveClear) {
//            // stage clear
//
//        } else if(firstWaveClear && secondWaveClear) {
//            thirdWaveClear = true;
//            // fourth wave?
//
//        } else if(firstWaveClear && !thirdWaveClear) {
//            secondWaveClear = true;
//            // deploy wave 3
//
//        } else if(!firstWaveClear && !secondWaveClear && !thirdWaveClear) {
//            firstWaveClear = true;
//            // deploy wave2
//
//        }
    }

    public void deployWave(ArrayList<Enemy> wave, Formation formation) {

        for(Enemy e : wave) {
            stage.addActor(e);
            enemiesOnScreen.add(e);
            e.scaleToLevel(enemyDifficulty);
            e.initLootTable();
        }

        numberOfEnemiesOnScreen = wave.size();

        // TODO: check each of these and polish if needed, finish rest
        switch (formation) {
            case ONE_CENTER:
                wave.get(0).setSize(4,4);
                wave.get(0).setPosition(2.5f, 9.5f);
                break;
            case TWO_IN_BACK:
                wave.get(0).setSize(3,3);
                wave.get(0).setPosition(.5f, 11f);

                wave.get(1).setSize(3,3);
                wave.get(1).setPosition(5.5f, 11f);
                break;
            case TWO_IN_FRONT:
                wave.get(0).setSize(4,4);
                wave.get(0).setPosition(.5f, 9.5f);

                wave.get(1).setSize(4,4);
                wave.get(1).setPosition(4.5f, 9.5f);
                break;
            case TWO_STAGGERED_LEFT:
                wave.get(0).setSize(3,3);
                wave.get(0).setPosition(.5f, 11f);

                wave.get(1).setSize(4,4);
                wave.get(1).setPosition(1.5f, 9.5f);
                break;
            case TWO_STAGGERED_RIGHT:
                wave.get(0).setSize(3,3);
                wave.get(0).setPosition(5.5f, 11);

                wave.get(1).setSize(4,4);
                wave.get(1).setPosition(3.5f, 9.5f);
                break;
            case THREE_ASCENDING:
                wave.get(0).setSize(2,2);
                wave.get(0).setPosition(6f, 11.5f);

                wave.get(1).setSize(3,3);
                wave.get(1).setPosition(3.5f, 10.5f);

                wave.get(2).setSize(5,5);
                wave.get(2).setPosition(.5f, 9.5f);
                break;
            case THREE_DESCENDING:
                wave.get(0).setSize(2,2);
                wave.get(0).setPosition(.5f, 11.5f);

                wave.get(1).setSize(3,3);
                wave.get(1).setPosition(3.5f, 10.5f);

                wave.get(2).setSize(5,5);
                wave.get(2).setPosition(6, 9.5f);
                break;
            case THREE_IN_FRONT:
                wave.get(0).setSize(2,2);
                wave.get(0).setPosition(6.5f, 10f);

                wave.get(1).setSize(2,2);
                wave.get(1).setPosition(3.5f, 10f);

                wave.get(2).setSize(2,2);
                wave.get(2).setPosition(.5f, 10f);
                break;
            case FOUR_IN_FRONT:
                wave.get(0).setSize(2,2);
                wave.get(0).setPosition(4.5f, 10f);

                wave.get(1).setSize(2,2);
                wave.get(1).setPosition(2.5f, 10f);

                wave.get(2).setSize(2,2);
                wave.get(2).setPosition(.5f, 10f);

                wave.get(3).setSize(2,2);
                wave.get(3).setPosition(6.5f, 10);
                break;
            case FOUR_REVERSE_N_SHAPE:
            case FOUR_WITH_BACK_SIDE_FLANKS:
                wave.get(0).setSize(3,3);
                wave.get(0).setPosition(.5f, 11);

                wave.get(1).setSize(3,3);
                wave.get(1).setPosition(6.5f, 11);

                wave.get(2).setSize(3,3);
                wave.get(2).setPosition(1.5f, 9.5f);

                wave.get(3).setSize(3,3);
                wave.get(3).setPosition(4.5f, 9.5f);
                break;
            case FOUR_WITH_FRONT_SIDE_FLANKS:
                wave.get(0).setSize(3,3);
                wave.get(0).setPosition(1.5f, 11);

                wave.get(1).setSize(3,3);
                wave.get(1).setPosition(4.5f, 11);

                wave.get(2).setSize(3,3);
                wave.get(2).setPosition(.5f, 9.5f);

                wave.get(3).setSize(3,3);
                wave.get(3).setPosition(6.5f, 9.5f);
                break;
            case FOUR_N_SHAPE:
            case FIVE_M_SHAPE:
            case FIVE_W_SHAPE:
            case FIVE_PROTECTING_CENTER:
            case FIVE_DESCENDING:
            case FIVE_ASCENDING:
                wave.get(0).setSize(2,2);
                wave.get(0).setPosition(6.5f, 14);

                wave.get(1).setSize(3,3);
                wave.get(1).setPosition(5.5f, 13.5f);

                wave.get(2).setSize(4,4);
                wave.get(2).setPosition(4.25f, 12);

                wave.get(3).setSize(5,5);
                wave.get(3).setPosition(2.5f, 10.25f);

                wave.get(4).setSize(6,6);
                wave.get(4).setPosition(.5f, 9);
                break;
        }
    }

    public void calculateGemPower(ArrayList<HeroCard> team) {
        for(HeroCard hero : team) {
            switch(hero.elementalType) {
                case ELECTRIC:
                    electricPower += hero.getStrength();
                    break;
                case FIRE:
                    firePower += hero.getStrength();
                    break;
                case NATURE:
                    naturePower += hero.getStrength();
                     break;
                case VOID:
                    voidPower += hero.getStrength();
                    break;
                case WATER:
                    waterPower += hero.getStrength();
                    break;
                case STONE:
                    stonePower += hero.getStrength();
                    break;
                case PURE:
                    purePower += hero.getStrength();
                    break;
            }
        }
    }

    private void initializeVariables(int phase) {
        switch(phase) {
            case 1:
                horizontalMatchLength = 0;
                verticalMatchLength = 0;

                possibleToMatch = false;
                allowUserInput = false;
                gameCamera = new OrthographicCamera();
                slots = new ArrayList<Vector2>();
                gems = new DelayedRemovalArray<Gem>();
                sendToDestroy = new ArrayList<Gem>();
                break;
            case 2:
                orthoMapRenderer = new OrthogonalTiledMapRenderer(matchMap, 1/32f);

                final int worldWidth = columns + 1;
                final double worldHeight = Math.floor(worldWidth * 1.77777778f); // approx 9:16

                FitViewport fitViewport = new FitViewport(worldWidth, (int) worldHeight);
                gameCamera.setToOrtho(false, worldWidth, (int) worldHeight);

                stage = new Stage(fitViewport);

                rootTable = new Table();
                rootTable.setFillParent(true);

//                rootTable.row().fillX().top();
                rootTable.add().fillX().expandX().top().height(7); // enemy cell
                rootTable.row();

                gemTable = new Table();
                rootTable.add(gemTable).height(rows).width(columns);
                rootTable.row();

                // for each hero in team add cell(hero)

                rootTable.setDebug(true);
                stage.setDebugAll(true); // debug
                break;
            default:
                break;
        }
    }

    @Override
    public void show() {
        initializeVariables(1);
        game.gempiresAssetHandler.initialiseGemTextures(classicMode);
        loadMap();
        initializeVariables(2);
        createAndFillSlots(rows, columns);
        Gdx.input.setInputProcessor(stage);
        gameCamera.update();
        stage.addActor(rootTable);

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                checkWholeBoardForMatches();
            }
        }, 1);
    }

    private void calculateDamage(ElementalType damageType, Enemy enemy) {

        final float damage;

        // TODO: do these elements actually make sense?

        switch(damageType) {
            case ELECTRIC:
                switch(enemy.elementalType) {
                    case VOID:
                    case WATER:
                        damage = electricPower * 2;
                        break;
                    case NATURE:
                    case STONE:
                        damage = electricPower * .5f;
                        break;
                    default:
                        damage = electricPower;
                        break;
                }
                break;
            case FIRE:
                switch(enemy.elementalType) {
                    case NATURE:
                    case VOID:
                        damage = firePower * 2;
                        break;
                    case WATER:
                    case STONE:
                        damage = firePower * .5f;
                        break;
                    default:
                        damage = firePower;
                        break;
                }
                break;
            case STONE:
                switch(enemy.elementalType) {
                    case FIRE:
                    case ELECTRIC:
                        damage = stonePower * 2;
                        break;
                    case VOID:
                    case WATER:
                        damage = stonePower * .5f;
                        break;
                    default:
                        damage = stonePower;
                        break;
                }
                break;
            case WATER:
                switch(enemy.elementalType) {
                    case FIRE:
                    case STONE:
                        damage = waterPower * 2;
                        break;
                    case NATURE:
                    case ELECTRIC:
                        damage = waterPower * .5f;
                        break;
                    default:
                        damage = waterPower;
                        break;
                }
                break;
            case NATURE:
                switch(enemy.elementalType) {
                    case WATER:
                    case ELECTRIC:
                        damage = naturePower * 2;
                        break;
                    case FIRE:
                    case VOID:
                        damage = naturePower * .5f;
                        break;
                    default:
                        damage = naturePower;
                        break;
                }
                break;
            case VOID:
                switch(enemy.elementalType) {
                    case STONE:
                    case NATURE:
                        damage = voidPower * 2;
                        break;
                    case FIRE:
                    case ELECTRIC:
                        damage = voidPower * .5f;
                        break;
                    default:
                        damage = voidPower;
                        break;
                }
                break;
            case PURE:
                // TODO:
                damage = 1000000000;
                break;
            default:
                damage = 1;
                break;
        }

        enemy.applyDamage(damage);

        if(enemy.getCurrentHealth() <= 0) {
            destroyEnemy(enemy);
        }
    }

    private void checkCollision() {
        for (AttackToken t : attackTokensOnScreen) {
            t.updateBounds();
            for (final Enemy e : enemiesOnScreen) {
                if (Intersector.overlaps(t.bounds, e.bounds)) {
                    t.remove();
                    attackTokensOnScreen.begin();
                    attackTokensOnScreen.removeValue(t, true);
                    attackTokensOnScreen.end();

                    calculateDamage(t.elementalType, e);

                    e.setColor(.5f,.5f,.5f,1);

                    Timer.schedule(new Timer.Task(){
                        @Override
                        public void run() {
                            e.setColor(1f,1f,1f,1);
                        }
                    }, 0.05f);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        orthoMapRenderer.setView(gameCamera);
        orthoMapRenderer.render();

        if(!classicMode) {
            checkCollision();
            if(needToClearWave && allowUserInput) {
                needToClearWave = false;
                clearWave();
            }
        }

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

    public void setEnemyDifficulty(int difficulty) {
        enemyDifficulty = difficulty;
    }

}
