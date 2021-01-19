package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class LevelUpButton extends Image {
    public LevelUpButton(final PopupMenu parentMenu) {
        super(parentMenu.getParentStructure().getParentScreen().T3AIcon); // debug texture
        this.setSize(3, 1);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                // TODO: if(have enough resources) { remove resources, level up }
                parentMenu.getParentStructure().levelUp();
            }
        });
    }
}
