package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GoddessStatue extends Structure {

    public GoddessStatue(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        this.structureType = StructureType.GODDESS_STATUE;
        this.setSize(1,2);
    }
}
