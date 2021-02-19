package com.feiqn.gempires.logic.castle.structures;

import com.feiqn.gempires.GempiresGame;

public class Archive extends Structure {

    public Archive(GempiresGame game) {
        super(game.gempiresAssetHandler.archivesTexture, game);

        structureType = StructureType.ARCHIVE;

        increaseResourceCapacity(getResourceCapacity() * 5f); // 500
        this.setSize(1.5f, 1.5f);
    }
}
