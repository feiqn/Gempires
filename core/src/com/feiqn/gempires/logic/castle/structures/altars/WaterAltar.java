package com.feiqn.gempires.logic.castle.structures.altars;

import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.structures.Structure;
import com.feiqn.gempires.models.ElementalType;

public class WaterAltar extends Structure {

    final ElementalType elementalType;

    public WaterAltar(GempiresGame game) {
        super(game.gempiresAssetHandler.altarWaterTexture, game);

        this.elementalType = ElementalType.WATER;

        structureType = StructureType.ALTAR_WATER;

        this.setSize(1.5f, 1.5f);
    }
}
