package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Silo extends Structure {

    public Silo(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        structureType = Type.SILO;

        increaseResourceCapacity(getResourceCapacity() * 5f); // 500
    }

}
