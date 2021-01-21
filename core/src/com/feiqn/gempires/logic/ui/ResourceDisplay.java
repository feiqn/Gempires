package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.items.ItemList;

public class ResourceDisplay extends Image {
    private String text;
    private CastleScreen parentScreen;
    private Label.LabelStyle style;
    private Label label;
    public DisplayType dType;

    public enum DisplayType {
        FOOD,
        ORE,
        ARCANA,
        THYME,
        PURE_GEM
    }

    public ResourceDisplay(CastleScreen parent, DisplayType type) {
        super(parent.menuTexture);
        this.parentScreen = parent;
        dType = type;
        style = parent.structureLabelStyle;
        text = "";
        label = new Label(text, style);
        updateText();
    }

    public void updateText() {
        final int i;

        switch(dType) {
            case FOOD:
                i = (int) parentScreen.playerInventory.getFoodCount();
                break;
            case ORE:
                i = (int) parentScreen.playerInventory.getOreCount();
                break;
            case THYME:
                i = parentScreen.playerInventory.getItemCount(ItemList.THYME);
                break;
            case ARCANA:
                i = (int) parentScreen.playerInventory.getArcanaCount();
                break;
            case PURE_GEM:
                i = parentScreen.playerInventory.getItemCount(ItemList.PURE_GEM);
                break;
            default:
                i = 0;
        }

        this.text = "" + i;
        label.setText(text);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        label.draw(batch, 1);
    }

    public void move(float x, float y) {
        this.setPosition(x,y);
        label.setPosition(x + this.getWidth() * .2f,y + this.getHeight() * .5f);
    }

}
