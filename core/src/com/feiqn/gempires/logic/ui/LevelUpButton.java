package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class LevelUpButton extends Image {

    private final Label label;

    public LevelUpButton(final PopupMenu parentMenu) {
        super(parentMenu.getParentStructure().getParentScreen().yellowButtonTexture);
        final float w = Gdx.graphics.getWidth() * .9f;
        this.setSize(w, w * .3f);

        label = new Label("Level up!", parentMenu.getParentStructure().getParentScreen().structureLabelStyle);
        label.setFontScale(2.5f);

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
                // TODO: if(have enough resources) { remove resources, level up }
                parentMenu.getParentStructure().levelUp();
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
