package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.logic.ui.ItemNotifierBubble;

public class Mine extends Structure {

    public Mine(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        structureType = StructureType.MINE;

        itemNotifierBubble = new ItemNotifierBubble(parent.oreTexture, this);
        this.setSize(1.5f, 1.5f);
    }
}
