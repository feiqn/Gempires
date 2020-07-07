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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.models.stats.CastleStats;
import com.feiqn.gempires.models.stats.HeroRoster;
import com.feiqn.gempires.models.stats.PlayerInventory;

public class CastleScreen extends ScreenAdapter {

    public OrthographicCamera camera;
    public TiledMap castleMap;
    public IsometricTiledMapRenderer isoMapRenderer;

    GempiresGame game;

    private Stage stage;

    final public CastleScreen self = this;

    public PlayerInventory playerInventory;
    public CastleStats castleStats;
    public HeroRoster heroRoster;

    public int goddessStatueLevel, goddessStatueExp;

    private Array<Plot> plots;
    public Array<Structure> structures;

    public Texture barracksIcon;

    public TextureRegion foodIcon,
                         menuSprite,
                         subMenuSprite,
                         heroRosterMenuSprite,
                         natureCardRegion,
                         natureCardThumbnail;

    Barracks barracks;

    Farm farm;

    BitmapFont structureFont;

    // TODO: read from and write to: CastleStats, HeroRoster, and PlayerInventory

    public CastleScreen(GempiresGame game) { this.game = game; }

    public void initialiseTextures() {

    }

    @Override
    public void show() {
        camera = new OrthographicCamera();

        castleMap = new TmxMapLoader().load("castleMap.tmx");
        isoMapRenderer = new IsometricTiledMapRenderer(castleMap, 1/32f);

        camera.setToOrtho(false, 12, 12);

        camera.position.set(100, 0, 0);

        camera.update();

        playerInventory = new PlayerInventory();
        castleStats = new CastleStats();
        heroRoster = new HeroRoster();

        stage = new Stage();

        // TODO: move all this stuff to helper methods

        barracksIcon = new Texture(Gdx.files.internal("structures/barracks.png"));

        final Texture menuSpriteSheet = new Texture(Gdx.files.internal("ui/menu.png"));
        menuSprite = new TextureRegion(menuSpriteSheet, 96, 0 , 96, 96);
        subMenuSprite = new TextureRegion(menuSpriteSheet, 96, 96, 96, 96);
        heroRosterMenuSprite = new TextureRegion(menuSpriteSheet, 0, 192, 96, 96);

        final Texture cardSpriteSheet = new Texture(Gdx.files.internal("ui/heroCards.png"));
        natureCardRegion = new TextureRegion(cardSpriteSheet, 0, 0, 96, 128);
        natureCardThumbnail = new TextureRegion(cardSpriteSheet, 96, 0, 64, 96);

        final Texture iconSpriteSheet = new Texture(Gdx.files.internal("icon-pack.png"));
        foodIcon = new TextureRegion(iconSpriteSheet, 256, 0, 32, 32);

        final Texture buildingSpriteSheet = new Texture(Gdx.files.internal("structures/buildingSpriteSheet.png"));
        final TextureRegion farmTexture = new TextureRegion(buildingSpriteSheet, 0, 128, 32, 32);

        structureFont = new BitmapFont();
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("COPPERPLATE.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter structureFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        structureFontParameter.size = 30;
        structureFontParameter.color = Color.WHITE;
        structureFontParameter.borderWidth = 2;
        structureFontParameter.borderColor = Color.BLACK;
        structureFont = fontGenerator.generateFont(structureFontParameter);
        fontGenerator.dispose();

        Label.LabelStyle structureLabelStyle = new Label.LabelStyle();
        structureLabelStyle.font = structureFont;

//        farm = new Farm(farmTexture, self);
//        farm.setXY(Gdx.graphics.getWidth() * .5f, Gdx.graphics.getHeight() * .5f);
//        stage.addActor(farm);

        barracks = new Barracks(barracksIcon, self);
        barracks.setSize(32, 32);
        barracks.setXY(camera.viewportWidth * .5f, camera.viewportHeight * .5f);
        stage.addActor(barracks);

        final float camViewportHalfX = camera.viewportWidth * .5f;
        final float camViewportHalfY = camera.viewportHeight * .5f;
        final float mapWidth = 100; // TODO: finish this

        camera.position.x = MathUtils.clamp(camera.position.x, camViewportHalfX, mapWidth - camViewportHalfX);

        stage.addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float screenX, float screenY, int pointer) {
                final float x = Gdx.input.getDeltaX();
                final float y = Gdx.input.getDeltaY();

                Gdx.app.log("drag", "x: " + x);
                Gdx.app.log("drag", "y: " + y);
                Gdx.app.log("drag", "screenX: " + screenX);
                Gdx.app.log("drag", "screenY: " + screenY);

                camera.translate(-x,y);
                camera.update();
            }
        });

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

    // GETTERS
    public Stage getStage() { return stage; }
}
