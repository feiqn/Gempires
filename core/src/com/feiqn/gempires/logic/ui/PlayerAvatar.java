package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class PlayerAvatar extends Image {

    public PlayerAvatar(Texture texture) {
        super(texture);
        this.setSize(512, 288);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                // TODO: open menu to select avatar
            }
        });
    }
}
