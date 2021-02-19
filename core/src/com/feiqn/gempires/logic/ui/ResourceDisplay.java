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
    private Image img;

    public DisplayType dType;

    public enum DisplayType {
        FOOD,
        ORE,
        ARCANA,
        THYME,
        PURE_GEM
    }

    public ResourceDisplay(CastleScreen parent, DisplayType type) {
        super(parent.game.gempiresAssetHandler.menuTexture);
        this.parentScreen = parent;
        dType = type;
        style = parent.structureLabelStyle;
        text = "";
        label = new Label(text, style);

        switch(dType) {
            case FOOD:
                img = new Image(parent.game.gempiresAssetHandler.foodTexture);
                break;
            case ARCANA:
                img = new Image(parent.game.gempiresAssetHandler.arcanaTexture);
                break;
            case THYME:
                img = new Image(parent.game.gempiresAssetHandler.thymeTexture);
                break;
            case ORE:
                img = new Image(parent.game.gempiresAssetHandler.oreTexture);
                break;
            case PURE_GEM:
                img = new Image(parent.game.gempiresAssetHandler.pureGemTexture);
                break;
        }
        img.setSize(parent.game.gempiresAssetHandler.foodTexture.getRegionWidth() * .9f, parent.game.gempiresAssetHandler.foodTexture.getRegionHeight() * .9f);

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
        img.draw(batch, parentAlpha * .5f);
        label.draw(batch, parentAlpha);
    }

    public void move(float x, float y) {
        this.setPosition(x,y);
        img.setPosition(x,y + 3);
        label.setPosition(x + this.getWidth() * .2f,y + this.getHeight() * .5f);
    }

}
