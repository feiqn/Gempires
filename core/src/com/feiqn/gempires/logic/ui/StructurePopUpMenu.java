package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.castle.Structure;

public class StructurePopUpMenu extends Image {
    // FUCK. I know this should just be a Table; but for no reason whatsoever, I lividly, avidly, consistently despise using Tables!!!

    public Group group;

    private Structure parentStructure;

    public StructurePopUpMenu(Structure sender) {
        super(sender.getParentScreen().menuSprite);
        this.parentStructure = sender;

        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * 0.66f);
    }
}
