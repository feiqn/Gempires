package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.structures.Structure;
import com.feiqn.gempires.logic.characters.heroes.HeroList;
import com.feiqn.gempires.logic.ui.popupMenus.PopupMenu;
import com.feiqn.gempires.logic.ui.ResourceDisplay;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.stats.CastleStats;
import com.feiqn.gempires.models.stats.HeroRoster;
import com.feiqn.gempires.models.stats.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;

public class CastleScreen extends ScreenAdapter {

    /*
     * This is the base class for all of Adventure Mode.
     * Everything refers back here in one way or another.
    */

    public OrthographicCamera gameCamera,
                              uiCamera;
    public TiledMap castleMap;
    public IsometricTiledMapRenderer isoMapRenderer;

    final public GempiresGame game;

    public PopupMenu activeMenu;

    private Stage gameStage,
                  hudStage;

    public Group rootGroup,
                 uiGroup;

    public Label.LabelStyle structureLabelStyle;

    final private CastleScreen self = this;

    // TODO: kinda feeling like these should have just all been one file
    public PlayerInventory playerInventory;
    public CastleStats castleStats;
    public HeroRoster heroRoster;

    private DelayedRemovalArray<Plot> plots;

    private ResourceDisplay foodDisplay,
                            oreDisplay,
                            pureGemsDisplay,
                            thymeDisplay,
                            arcanaDisplay;


//    public T3AButton t3ATestButton;

    public BitmapFont structureFont;

    public CastleScreen(GempiresGame game) { this.game = game; }

    private void initialiseUI() {
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        final FitViewport fitViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hudStage = new Stage(fitViewport);

        uiCamera.position.set(0,0,0);
        uiGroup.setPosition(0,0);

        hudStage.addActor(uiGroup);

    }

    private void layoutUI() {
        final Image avatar = new Image(game.gempiresAssetHandler.avatarTexture);
        // TODO: yikes
        final float nine = Gdx.graphics.getWidth() * .2f;
        final float sixteen = Gdx.graphics.getHeight() * .2f ;
        avatar.setSize(sixteen, nine);
        avatar.setScaling(Scaling.fit);
        avatar.setPosition(Gdx.graphics.getWidth() * .02f, Gdx.graphics.getHeight() * 0.87f);
        uiGroup.addActor(avatar);

        foodDisplay = new ResourceDisplay(this, ResourceDisplay.DisplayType.FOOD);
        foodDisplay.setSize(avatar.getWidth() * .5f, avatar.getHeight() * .4f);
        foodDisplay.move(avatar.getX() + avatar.getWidth() + 5, avatar.getY()); // todo
        uiGroup.addActor(foodDisplay);

        oreDisplay = new ResourceDisplay(this, ResourceDisplay.DisplayType.ORE);
        oreDisplay. setSize(foodDisplay.getWidth(), foodDisplay.getHeight());
        oreDisplay.move(foodDisplay.getX(), foodDisplay.getY() + oreDisplay.getHeight() + (oreDisplay.getHeight() * .5f));
        uiGroup.addActor(oreDisplay);

        arcanaDisplay = new ResourceDisplay(this, ResourceDisplay.DisplayType.ARCANA);
        arcanaDisplay.setSize(foodDisplay.getWidth() * .9f, foodDisplay.getHeight());
        arcanaDisplay.move(oreDisplay.getX() + oreDisplay.getWidth() + 5, oreDisplay.getY());
        uiGroup.addActor(arcanaDisplay);

        pureGemsDisplay = new ResourceDisplay(this, ResourceDisplay.DisplayType.PURE_GEM);
        pureGemsDisplay.setSize(foodDisplay.getWidth() * .8f, foodDisplay.getHeight());
        pureGemsDisplay.move(arcanaDisplay.getX() + arcanaDisplay.getWidth() + 5, arcanaDisplay.getY());
        uiGroup.addActor(pureGemsDisplay);

        thymeDisplay = new ResourceDisplay(this, ResourceDisplay.DisplayType.THYME);
        thymeDisplay.setSize(arcanaDisplay.getWidth() + pureGemsDisplay.getWidth() + 5, pureGemsDisplay.getHeight());
        thymeDisplay.move(arcanaDisplay.getX(), foodDisplay.getY());
        uiGroup.addActor(thymeDisplay);
    }

    private void initialiseStructures() {

//        t3ATestButton = new T3AButton(T3AIcon);
//        t3ATestButton.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        uiGroup.addActor((t3ATestButton));

        final CampaignSelector debugWaterSelector = new CampaignSelector(game.gempiresAssetHandler.campaignSelectorWaterTexture, game, CampaignLevelID.WATER_1);
        debugWaterSelector.setSize(1,1);
        debugWaterSelector.setPosition(60, 60);
        rootGroup.addActor(debugWaterSelector);

        final CampaignSelector debugVoidSelector = new CampaignSelector(game.gempiresAssetHandler.campaignSelectorVoidTexture, game, CampaignLevelID.VOID_1);
        debugVoidSelector.setSize(1,1);
        debugVoidSelector.setPosition(62,62);
        rootGroup.addActor(debugVoidSelector);

        for(Structure s : castleStats.getStructures()) {
            rootGroup.addActor(s);
        }
    }

    private void initialiseMap() {

        final float worldWidth = Gdx.graphics.getWidth() / 32f;
        final float worldHeight = Gdx.graphics.getHeight() / 32f;

        gameCamera.setToOrtho(false, worldWidth, worldHeight);

        final FitViewport fitViewport = new FitViewport(worldWidth, worldHeight);

        gameStage = new Stage(fitViewport);

        gameCamera.position.set(0,0,0);

        final MapProperties mapProperties = castleMap.getProperties();
        final int mapWidth = mapProperties.get("width", Integer.class);
        final int mapHeight = mapProperties.get("height", Integer.class);

        rootGroup.setSize(mapWidth, mapHeight);

        rootGroup.setPosition(0,0,0);

        gameStage.addActor(rootGroup);

    }

    private void initialiseFont() {
        // TODO: load via asset handler
        final Texture fontTexture = new Texture(Gdx.files.internal("ui/tinyFont.png"), true);
        fontTexture.setFilter(Texture.TextureFilter.MipMapNearestNearest, Texture.TextureFilter.Linear);

        structureFont = new BitmapFont(Gdx.files.internal("ui/tinyFont.fnt"), new TextureRegion(fontTexture), false);
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("COPPERPLATE.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter structureFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        structureFontParameter.color = Color.WHITE;
        structureFontParameter.borderWidth = 2;
        structureFontParameter.borderColor = Color.BLACK;
        structureFontParameter.size = 16;
        structureFontParameter.incremental = true;
        structureFont = fontGenerator.generateFont(structureFontParameter);
        fontGenerator.dispose();

        structureLabelStyle = new Label.LabelStyle();
        structureLabelStyle.font = structureFont;
    }

    @Override
    public void show() {
        gameCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        rootGroup = new Group();
        uiGroup = new Group();

        castleMap = new TmxMapLoader().load("castleMap.tmx");
        isoMapRenderer = new IsometricTiledMapRenderer(castleMap, 1/32f);

        game.gempiresAssetHandler.initialiseCastleTextures();

        final ArrayList<HeroList> h = new ArrayList<>(Arrays.asList(HeroList.values())); // debug
        game.gempiresAssetHandler.initialiseHeroTextures(h);

        castleStats = new CastleStats(game);

        initialiseMap();

        initialiseFont();

        // TODO: debug campaign selectors

        initialiseStructures();

        gameCamera.position.set(100, 0, 0);

        final float camViewportHalfX = gameCamera.viewportWidth * .5f;
        final float camViewportHalfY = gameCamera.viewportHeight * .5f;
        // final float mapWidth = 100;

       // camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth - camViewportHalfX);
        // camera.position.y = ... // TODO: finish this

        heroRoster = new HeroRoster(this);
        playerInventory = new PlayerInventory(this);

        initialiseUI();
        layoutUI();

        gameStage.addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float screenX, float screenY, int pointer) {
                final float x = Gdx.input.getDeltaX() * .05f;
                final float y = Gdx.input.getDeltaY() * .05f;

                gameCamera.translate(-x,y);
                gameCamera.update();

                final float destinationX = rootGroup.getX() + x;
                final float destinationY = rootGroup.getY() - y;

                rootGroup.setPosition(destinationX, destinationY);
                gameStage.act();
                gameStage.draw();
            }
        });

        gameCamera.update();

        // TODO: make game stage not clickable through hud stage
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hudStage);
        multiplexer.addProcessor(gameStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void updateHUD() {
        foodDisplay.updateText();
        oreDisplay.updateText();
        thymeDisplay.updateText();
        pureGemsDisplay.updateText();
        arcanaDisplay.updateText();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,1,1);


        for(Structure struct : castleStats.getStructures()) {
            struct.updateResource(Gdx.graphics.getDeltaTime());
        }

        isoMapRenderer.setView(gameCamera);
        isoMapRenderer.render();

        updateHUD();

        gameStage.act();
        gameStage.draw();

        hudStage.act();
        hudStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        gameStage.getCamera().update();
    }

    // GETTERS
    public Stage getGameStage() { return gameStage; }
    public Stage getHUDStage() { return hudStage; }
}
