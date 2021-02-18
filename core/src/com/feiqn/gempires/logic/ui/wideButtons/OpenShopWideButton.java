package com.feiqn.gempires.logic.ui.wideButtons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.feiqn.gempires.logic.ui.PopupMenu;

public class OpenShopWideButton extends WideButton {

    public OpenShopWideButton(final PopupMenu parentMenu) {
        super(parentMenu.getParentStructure().getParentScreen().blueButtonTexture, parentMenu);

        updateLabelText("Create Items");
        updateLabelFontScale(1.75f);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                final PopupMenu shopMenu = new PopupMenu(parentMenu.getParentStructure(), PopupMenu.MenuType.SHOP_ALCHEMY);
                parentMenu.getParentStructure().getParentScreen().uiGroup.addActor(shopMenu);
            }
        });
    }
}
