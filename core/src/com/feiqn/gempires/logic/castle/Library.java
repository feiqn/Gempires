package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Library extends Structure {

    public Library(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        structureType = StructureType.LIBRARY;

        // itemNotifierBubble = new ItemNotifierBubble(parent.arcanaIcon, this);
        this.setSize(1.5f, 1.5f);
    }

}
