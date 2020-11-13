package com.feiqn.gempires.logic.t3a;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.feiqn.gempires.GempiresGame;

public class T3AScreen extends ScreenAdapter {

    GempiresGame game;

    private Stage stage;

    public BitmapFont readoutFont;

    public T3AScreen(GempiresGame game) { this.game = game; }

    @Override
    public void show() {
        stage = new Stage();

        readoutFont = new BitmapFont();
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("COPPERPLATE.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter readoutFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        readoutFontParameter.size = 12;
        readoutFontParameter.color = Color.WHITE;
        readoutFontParameter.borderWidth = 2;
        readoutFontParameter.borderColor = Color.BLUE;
        readoutFont = fontGenerator.generateFont(readoutFontParameter);
        fontGenerator.dispose();

        Label.LabelStyle readoutLabelStyle = new Label.LabelStyle();
        readoutLabelStyle.font = readoutFont;

        // TODO: this will later be where players come to level up their personal player character
    }

}
