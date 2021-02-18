package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Alchemist extends Structure {

    public Alchemist(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        structureType = StructureType.ALCHEMIST;

        this.setSize(1.5f, 1.5f);
    }
}
