package com.feiqn.gempires.logic.ui.wideButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.feiqn.gempires.logic.ui.popupMenus.PopupMenu;

public class WideButton extends Image {

    private final Label label;

    public WideButton(final TextureRegion region, final PopupMenu parentMenu) {
        super(region);

        final float w = Gdx.graphics.getWidth() * .9f;
        this.setSize(w, w * .3f);

        label = new Label("", parentMenu.getParentStructure().getParentScreen().structureLabelStyle);
        label.setFontScale(2.5f);
    }

    public void updateLabelText(String text) {
        label.setText(text);
    }

    public void updateLabelFontScale(float scale) {
        label.setFontScale(scale);
    }

    @Override
    public void setPosition(float x, float y) {
        this.setX(x);
        this.setY(y);
        label.setPosition(x + this.getWidth() * .1f,y + this.getHeight() * .4f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        label.draw(batch, parentAlpha);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        super.setColor(r,g,b,a);
        label.setColor(r,g,b,a);
    }
}
