package com.feiqn.gempires.logic.ui.wideButtons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.ui.PopupMenu;

public class BeginStageWideButton extends WideButton {

    public BeginStageWideButton(final PopupMenu parentMenu) {
        super(parentMenu.getParentStructure().getParentScreen().blueButtonTexture, parentMenu);

        updateLabelText("Venture On");
        updateLabelFontScale(2f);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                parentMenu.getParentStructure().selectLevel();
            }
        });
    }
}
