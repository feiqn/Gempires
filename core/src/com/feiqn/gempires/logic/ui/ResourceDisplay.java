package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.feiqn.gempires.logic.castle.CastleScreen;

public class ResourceDisplay extends Actor {
    private String text;
    private CastleScreen parentScreen;
    private Label.LabelStyle style;
    private Label label;

    public ResourceDisplay(CastleScreen parent) {
        super();
        this.parentScreen = parent;
        style = parent.structureLabelStyle;
        text = "";
        label = new Label(text, style);
        updateText();
    }

    public void updateText() {
        final int i = (int) parentScreen.playerInventory.getFoodCount();
        this.text = "Food: " + i;
        label.setText(text);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        label.draw(batch, 1);
    }

    public void move(float x, float y) {
        label.setPosition(x,y);
    }

}
