package com.feiqn.gempires.logic.castle.structures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.structures.Structure;

public class Silo extends Structure {

    public Silo(GempiresGame game) {
        super(game.gempiresAssetHandler.siloTexture, game);

        structureType = StructureType.SILO;

        increaseResourceCapacity(getResourceCapacity() * 5f); // 500
        this.setSize(1.5f, 1.5f);
    }

}
