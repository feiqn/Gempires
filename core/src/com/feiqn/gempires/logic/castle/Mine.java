package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Mine extends Structure {

    public Mine(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        structureType = StructureType.MINE;

        // itemNotifierBubble = new ItemNotifierBubble(parent.oreIcon, this);
    }
}
