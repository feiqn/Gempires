package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SummonButton extends Image {
    // TODO: This and LevelUpButton share a great deal of code. Create a super class.

    private final Label label;

    public SummonButton(final PopupMenu parentMenu) {
        super(parentMenu.getParentStructure().getParentScreen().purpleButtonTexture);

        final float w = Gdx.graphics.getWidth() * .9f;
        this.setSize(w, w * .3f);

        label = new Label("Search the Fog", parentMenu.getParentStructure().getParentScreen().structureLabelStyle);
        label.setFontScale(1.5f);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                label.setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                label.setColor(1.5f, 1.5f, 1.5f, 1);
                // TODO: if(have enough resources) { remove resources }
                parentMenu.getParentStructure().getParentScreen().heroRoster.goddessSummon();
            }
        });
    }

    public void move(float x, float y) {
        this.setPosition(x,y);
        label.setPosition(x + this.getWidth() * .1f,y + this.getHeight() * .4f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        label.draw(batch, parentAlpha);
    }
}
