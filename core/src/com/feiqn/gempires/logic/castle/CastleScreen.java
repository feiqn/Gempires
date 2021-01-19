package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.ui.ResourceDisplay;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.stats.CastleStats;
import com.feiqn.gempires.models.stats.HeroRoster;
import com.feiqn.gempires.models.stats.PlayerInventory;

public class CastleScreen extends ScreenAdapter {

    /*
     * This is the base class for all of Adventure Mode.
     * Everything refers back here in one way or another.
    */

    public OrthographicCamera gameCamera,
                              uiCamera;
    public TiledMap castleMap;
    public IsometricTiledMapRenderer isoMapRenderer;

    final private GempiresGame game;

    // TODO: use an AssetManager

    private Stage gameStage,
                  uiStage;

    public Group rootGroup,
                 uiGroup;

    public Label.LabelStyle structureLabelStyle;

    final private CastleScreen self = this;

    public PlayerInventory playerInventory;
    public CastleStats castleStats;
    public HeroRoster heroRoster;

    private DelayedRemovalArray<Plot> plots;

    public Texture barracksTexture;

    private ResourceDisplay foodDisplay;

    public TextureRegion T3AIcon,
                         foodIcon,
                         oreIcon,
                         arcanaIcon,

                         farmTexture,
                         mineTexture,
                         libraryTexture,
                         siloTexture,
                         warehouseTexture,
                         archivesTexture,
                         goddessStatueTexture,

                         campaignSelectorVoid,
                         campaignSelectorIce,
                         campaignSelectorFire,
                         campaignSelectorElectric,
                         campaignSelectorNature,
                         campaignSelectorEarth,

                         menuTexture,
                         natureCardRegion,
                         natureCardThumbnail,
                         backButtonTexture;

    public Barracks barracks;

//    public T3AButton t3ATestButton;

    public BitmapFont structureFont;

    public CastleScreen(GempiresGame game) { this.game = game; }

    private void initialiseTextures() {

        final Texture goddessSpriteSheet = new Texture(Gdx.files.internal("structures/goddessSpriteSheet.png"));
        goddessStatueTexture = new TextureRegion(goddessSpriteSheet,672, 64, 32, 64);

        barracksTexture = new Texture(Gdx.files.internal("structures/barracks.png"));

        final Texture menuSpriteSheet = new Texture(Gdx.files.internal("ui/menu.png"));
        menuTexture =  new TextureRegion(menuSpriteSheet, 0,  192,96, 96);
        backButtonTexture =     new TextureRegion(menuSpriteSheet, 192,0 , 32, 32);

        final Texture cardSpriteSheet = new Texture(Gdx.files.internal("ui/heroCards.png"));
        natureCardRegion =    new TextureRegion(cardSpriteSheet, 0, 0, 96, 128);
        natureCardThumbnail = new TextureRegion(cardSpriteSheet, 96,0, 64, 96);

        final Texture iconSpriteSheet = new Texture(Gdx.files.internal("icon-pack.png"));
        foodIcon = new TextureRegion(iconSpriteSheet, 256, 0, 32, 32);

        final Texture buildingSpriteSheet = new Texture(Gdx.files.internal("structures/buildingSpriteSheet.png"));
        farmTexture = new TextureRegion(buildingSpriteSheet, 0, 128, 32, 32);
        mineTexture = new TextureRegion(buildingSpriteSheet, 160, 96, 32, 32);

        final Texture itemSpriteSheet = new Texture(Gdx.files.internal("ui/RPG_Item_Pack.png"));
        campaignSelectorVoid      = new TextureRegion(itemSpriteSheet,64, 0,  16,16);
        campaignSelectorIce       = new TextureRegion(itemSpriteSheet,64, 16, 16,16);
        campaignSelectorElectric  = new TextureRegion(itemSpriteSheet,64, 32, 16,16);
        campaignSelectorFire      = new TextureRegion(itemSpriteSheet,64, 48, 16,16);
        campaignSelectorNature    = new TextureRegion(itemSpriteSheet,64, 64, 16,16);
        campaignSelectorEarth     = new TextureRegion(itemSpriteSheet,64, 70, 16,16);
        T3AIcon                   = new TextureRegion(itemSpriteSheet,128,368,16,16);

    }

    private void initialiseUI() {
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        final FitViewport fitViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        uiStage = new Stage(fitViewport);

        uiCamera.position.set(0,0,0);
        uiGroup.setPosition(0,0);
        uiStage.addActor(uiGroup);

    }

    private void layoutUI() {
        foodDisplay = new ResourceDisplay(this);
        foodDisplay.move(32, 32); // todo
        uiGroup.addActor(foodDisplay);
    }

    private void initialiseStructures() {

//        t3ATestButton = new T3AButton(T3AIcon);
//        t3ATestButton.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        uiGroup.addActor((t3ATestButton));

        CampaignSelector debugSelector = new CampaignSelector(campaignSelectorFire, CampaignLevelID.FIRE_1);
        debugSelector.setSize(1,1);
        debugSelector.setPosition(60, 60);
        rootGroup.addActor(debugSelector);


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

        initialiseTextures();

        castleStats = new CastleStats(this);

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
        // TODO: inputMultiplexer
        Gdx.input.setInputProcessor(gameStage);
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

        foodDisplay.updateText();

        gameStage.act();
        gameStage.draw();
        uiStage.act();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        gameStage.getCamera().update();
    }

    // GETTERS
    public Stage getGameStage() { return gameStage; }
}
