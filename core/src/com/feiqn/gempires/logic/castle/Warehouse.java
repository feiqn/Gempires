package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Warehouse extends Structure{

    public Warehouse(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        structureType = StructureType.WAREHOUSE;

        increaseResourceCapacity(getResourceCapacity() * 5f); // 500
        this.setSize(1.5f, 1.5f);
    }
}
