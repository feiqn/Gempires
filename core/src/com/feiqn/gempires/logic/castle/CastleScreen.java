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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.stats.CastleStats;
import com.feiqn.gempires.models.stats.HeroRoster;
import com.feiqn.gempires.models.stats.PlayerInventory;
import com.feiqn.gempires.logic.ui.T3AButton;

import java.util.ArrayList;

public class CastleScreen extends ScreenAdapter {

    /*
     * This is the base class for all of Adventure Mode.
     * Everything refers back here in one way or another.
    */

    public OrthographicCamera camera;
    public TiledMap castleMap;
    public IsometricTiledMapRenderer isoMapRenderer;

    final private GempiresGame game;

    private Stage stage;

    public Group rootGroup,
                 uiGroup;

    public Label.LabelStyle structureLabelStyle;

    final private CastleScreen self = this;

    public PlayerInventory playerInventory;
    public CastleStats castleStats;
    public HeroRoster heroRoster;

    private DelayedRemovalArray<Plot> plots;

    public Texture barracksTexture;

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
    public GoddessStatue goddessStatue;
    public Farm farm;
    public Mine mine;

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
        Label foodLabel = new Label( "Food: " /* + playerInventory.getFoodCount() */ , structureLabelStyle );
//        foodLabel.setFontScale(0.08f);
        uiGroup.addActor(foodLabel);
    }

    private void initialiseStructures() {

//        t3ATestButton = new T3AButton(T3AIcon);
//        t3ATestButton.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        uiGroup.addActor((t3ATestButton));

        // TODO: move to CastleStates and delete local pref files

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

        camera.setToOrtho(false, worldWidth, worldHeight);

        final FitViewport fitViewport = new FitViewport(worldWidth, worldHeight);

        stage = new Stage(fitViewport);

        camera.position.set(0,0,0);

        final MapProperties mapProperties = castleMap.getProperties();
        final int mapWidth = mapProperties.get("width", Integer.class);
        final int mapHeight = mapProperties.get("height", Integer.class);

        rootGroup.setSize(mapWidth, mapHeight);
        // uiGroup.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        rootGroup.setPosition(0,0,0);
        uiGroup.setPosition(0,0);

        stage.addActor(rootGroup);
        stage.addActor(uiGroup);

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
        structureFontParameter.size = 12;
        structureFontParameter.incremental = true;
//        structureFontParameter.characters = Hiero.EXTENDED_CHARS;
        structureFont = fontGenerator.generateFont(structureFontParameter);
        structureFont.getData().setScale(0.09f, 0.09f);// TODO: how do i make this super tiny?
        fontGenerator.dispose();

        structureLabelStyle = new Label.LabelStyle();
        structureLabelStyle.font = structureFont;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
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

        initialiseUI();

        camera.position.set(100, 0, 0);

        final float camViewportHalfX = camera.viewportWidth * .5f;
        final float camViewportHalfY = camera.viewportHeight * .5f;
        // final float mapWidth = 100;

       // camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth - camViewportHalfX);
        // camera.position.y = ... // TODO: finish this

        heroRoster = new HeroRoster(this);
        playerInventory = new PlayerInventory(this);


        stage.addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float screenX, float screenY, int pointer) {
                final float x = Gdx.input.getDeltaX() * .05f;
                final float y = Gdx.input.getDeltaY() * .05f;

                camera.translate(-x,y);
                camera.update();

                final float destinationX = rootGroup.getX() + x;
                final float destinationY = rootGroup.getY() - y;

                rootGroup.setPosition(destinationX, destinationY);
                stage.act();
                stage.draw();
            }
        });

        camera.update();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,1,1);


        for(Structure struct : castleStats.getStructures()) {
            struct.updateResource(Gdx.graphics.getDeltaTime());
        }

        isoMapRenderer.setView(camera);
        isoMapRenderer.render();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.getCamera().update();
    }

    // GETTERS
    public Stage getStage() { return stage; }
}
