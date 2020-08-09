package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.Texture;

public class Barracks extends Structure {

    public Barracks(Texture region, CastleScreen parent) {
        super(region, parent);

        structureType = StructureType.BARRACKS;

        this.setSize(1,1);
    }
}
