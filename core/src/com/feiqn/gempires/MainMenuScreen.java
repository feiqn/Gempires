package com.feiqn.gempires;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.feiqn.gempires.logic.stages.match3.MatchScreen;
import com.feiqn.gempires.logic.castle.CastleScreen;

public class MainMenuScreen extends ScreenAdapter {

    GempiresGame game;

    private Stage stage;

    public BitmapFont mainMenuFont;

    public MainMenuScreen(GempiresGame game) { this.game = game; }

    @Override
    public void show() {
        Label ClassicModeLabel, AdventureModeLabel;

        stage = new Stage();

        mainMenuFont = new BitmapFont();
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("COPPERPLATE.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter mainMenuFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mainMenuFontParameter.size = 40;
        mainMenuFontParameter.color = Color.GOLDENROD;
        mainMenuFontParameter.borderWidth = 4;
        mainMenuFontParameter.borderColor = Color.GOLD;
        mainMenuFont = fontGenerator.generateFont(mainMenuFontParameter);
        fontGenerator.dispose();

        Label.LabelStyle mainMenuLabelStyle = new Label.LabelStyle();
        mainMenuLabelStyle.font = mainMenuFont;

        ClassicModeLabel = new Label("Classic Mode", mainMenuLabelStyle);
        AdventureModeLabel = new Label("Adventure Mode", mainMenuLabelStyle);

        ClassicModeLabel.setPosition(Gdx.graphics.getWidth() * .2f, Gdx.graphics.getHeight() * .4f);
        AdventureModeLabel.setPosition(Gdx.graphics.getWidth() * .2f, Gdx.graphics.getHeight() * .6f);

        ClassicModeLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                game.setScreen(new MatchScreen(game));
            }
        });

        AdventureModeLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                game.setScreen(game.castle);
            }
        });

        stage.addActor(ClassicModeLabel);
        stage.addActor(AdventureModeLabel);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if(game.gempiresAssetHandler.getManager().update()) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glClearColor(0,0,0,1);

            stage.act();
            stage.draw();
        } else {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glClearColor(0,1,0,1);
        }
    }

}
