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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.stats.CastleStats;
import com.feiqn.gempires.models.stats.HeroRoster;
import com.feiqn.gempires.models.stats.PlayerInventory;
import com.feiqn.gempires.logic.ui.T3AButton;

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

    private Group rootGroup,
                  uiGroup;

    public Label.LabelStyle structureLabelStyle;

    final private CastleScreen self = this;

    public PlayerInventory playerInventory;
    public CastleStats castleStats;
    public HeroRoster heroRoster;

    private Array<Plot> plots;

    public Array<Structure> structures;

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
    public Library library;
    public Silo silo;
    public Warehouse warehouse;

    public T3AButton t3ATestButton;

    public BitmapFont structureFont;

    // TODO: read from and write to: CastleStats, HeroRoster, and PlayerInventory

    public CastleScreen(GempiresGame game) { this.game = game; }

    public void initialiseTextures() {

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

    // TODO: initialiseUI() , set a ui group to be added to stage

    public void initialiseStructures() {

        t3ATestButton = new T3AButton(T3AIcon);
        // t3ATestButton.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiGroup.addActor((t3ATestButton));

        barracks = new Barracks(barracksTexture, self);
        barracks.setSize(1.5f, 1.5f);
        barracks.setPosition(59.5f,64.5f);
        rootGroup.addActor(barracks);

        goddessStatue = new GoddessStatue(goddessStatueTexture, self);
        goddessStatue.setSize(1, 2);
        goddessStatue.setPosition(55.5f, 64.5f);
        rootGroup.addActor(goddessStatue);

        mine = new Mine(mineTexture, self);
        mine.setSize(1.5f,1.5f);
        mine.setPosition(51.5f, 64f);
        rootGroup.addActor(mine);

        farm = new Farm(farmTexture, self);
        farm.setSize(1.5f,1.5f);
        farm.setPosition(52.5f, 63.5f);
        rootGroup.addActor(farm);

        CampaignSelector debugSelector = new CampaignSelector(campaignSelectorFire, CampaignLevelID.FIRE_1);
        debugSelector.setSize(1.5f,1.5f);
        debugSelector.setPosition(60, 60);
        rootGroup.addActor(debugSelector);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        rootGroup = new Group();
        uiGroup = new Group();

        castleMap = new TmxMapLoader().load("castleMap.tmx");
        isoMapRenderer = new IsometricTiledMapRenderer(castleMap, 1/32f);

        playerInventory = new PlayerInventory(this);
        castleStats = new CastleStats(this);
        heroRoster = new HeroRoster();

        final float worldWidth = Gdx.graphics.getWidth() / 32f; // TODO: should this be viewport instead?
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

        stage.addActor(uiGroup);
        stage.addActor(rootGroup);

        // TODO: move all this stuff to helper methods

        initialiseTextures();

        structureFont = new BitmapFont();
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("COPPERPLATE.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter structureFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        structureFontParameter.color = Color.WHITE;
        structureFontParameter.borderWidth = 2;
        structureFontParameter.borderColor = Color.BLACK;
        structureFont = fontGenerator.generateFont(structureFontParameter);
        fontGenerator.dispose();

        structureLabelStyle = new Label.LabelStyle();
        structureLabelStyle.font = structureFont;

        // TODO: debug campaign selectors

        initialiseStructures();

        camera.position.set(100, 0, 0);

//        Vector2 vector = new Vector2(-30, -30);
//        vector = stage.screenToStageCoordinates(vector);
//        vector = rootGroup.stageToLocalCoordinates(vector);
//        barracks.setPosition(vector.x, vector.y);
//        camera.position.set(vector.x, vector.y, 0);

        final float camViewportHalfX = camera.viewportWidth * .5f;
        final float camViewportHalfY = camera.viewportHeight * .5f;
        // final float mapWidth = 100;

       // camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth - camViewportHalfX);
        // camera.position.y = ... // TODO: finish this

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

        // TODO: add all structures to an Array<Structure> and iterate through them
        // farm.updateResource(Gdx.graphics.getDeltaTime());

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
