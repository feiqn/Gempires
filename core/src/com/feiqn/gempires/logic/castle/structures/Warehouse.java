package com.feiqn.gempires.logic.castle.structures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.castle.structures.Structure;

public class Warehouse extends Structure {

    public Warehouse(GempiresGame game) {
        super(game.gempiresAssetHandler.warehouseTexture, game);

        structureType = StructureType.WAREHOUSE;

        increaseResourceCapacity(getResourceCapacity() * 5f); // 500
        this.setSize(1.5f, 1.5f);
    }
}
