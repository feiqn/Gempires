package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.models.ElementalType;

public class Altar extends Structure {

    final ElementalType elementalType;

    public Altar(TextureRegion region, CastleScreen parent, ElementalType elementalType) {
        super(region, parent);

        this.elementalType = elementalType;

        structureType = StructureType.ALTAR;

        this.setSize(1.5f, 1.5f);
    }
}
