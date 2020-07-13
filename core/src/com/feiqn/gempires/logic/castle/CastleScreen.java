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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.ui.HeroRosterPopup;
import com.feiqn.gempires.logic.ui.StructurePopupMenu;
import com.feiqn.gempires.models.stats.CastleStats;
import com.feiqn.gempires.models.stats.HeroRoster;
import com.feiqn.gempires.models.stats.PlayerInventory;

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

    private Group rootGroup;

    public Label.LabelStyle structureLabelStyle;

    final public CastleScreen self = this;

    public PlayerInventory playerInventory;
    public CastleStats castleStats;
    public HeroRoster heroRoster;

    private Array<Plot> plots;

    public Array<Structure> structures;

    public Texture barracksIcon;

    public TextureRegion foodIcon,
                         oreIcon,
                         arcanaIcon,

                         farmTexture,
                         mineTexture,
                         libraryTexture,
                         siloTexture,
                         warehouseTexture,
                         archivesTexture,

                         menuSprite,
                         subMenuSprite,
                         heroRosterMenuSprite,
                         natureCardRegion,
                         natureCardThumbnail,
                         backButtonTexture;

    public Barracks barracks;

    public BitmapFont structureFont;

    // TODO: read from and write to: CastleStats, HeroRoster, and PlayerInventory

    public CastleScreen(GempiresGame game) { this.game = game; }

    public void initialiseTextures() {

        barracksIcon = new Texture(Gdx.files.internal("structures/barracks.png"));

        final Texture menuSpriteSheet = new Texture(Gdx.files.internal("ui/menu.png"));
        menuSprite = new TextureRegion(menuSpriteSheet, 96, 0 , 96, 96);
        subMenuSprite = new TextureRegion(menuSpriteSheet, 96, 96, 96, 96);
        heroRosterMenuSprite = new TextureRegion(menuSpriteSheet, 0, 192, 96, 96);
        backButtonTexture = new TextureRegion(menuSpriteSheet, 192, 0 , 32, 32);

        final Texture cardSpriteSheet = new Texture(Gdx.files.internal("ui/heroCards.png"));
        natureCardRegion = new TextureRegion(cardSpriteSheet, 0, 0, 96, 128);
        natureCardThumbnail = new TextureRegion(cardSpriteSheet, 96, 0, 64, 96);

        final Texture iconSpriteSheet = new Texture(Gdx.files.internal("icon-pack.png"));
        foodIcon = new TextureRegion(iconSpriteSheet, 256, 0, 32, 32);

        final Texture buildingSpriteSheet = new Texture(Gdx.files.internal("structures/buildingSpriteSheet.png"));
        farmTexture = new TextureRegion(buildingSpriteSheet, 0, 128, 32, 32);

    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        rootGroup = new Group();

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

        final MapProperties mapProperties = castleMap.getProperties();
        final int mapWidth = mapProperties.get("width", Integer.class);
        final int mapHeight = mapProperties.get("height", Integer.class);

        rootGroup.setSize(mapWidth, mapHeight);

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

        Label debugLabel = new Label("Adventure onwards" , structureLabelStyle);

        debugLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                // later we should select stage, etc

            }
        });

         // rootGroup.addActor(debugLabel);

//        farm = new Farm(farmTexture, self);
//        farm.setXY(Gdx.graphics.getWidth() * .5f, Gdx.graphics.getHeight() * .5f);
//        stage.addActor(farm);

        barracks = new Barracks(barracksIcon, self);

        rootGroup.addActor(barracks);

        barracks.setSize(2, 2);

        camera.position.set(100, 0, 0);
        barracks.setPosition(0,0);

//
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
