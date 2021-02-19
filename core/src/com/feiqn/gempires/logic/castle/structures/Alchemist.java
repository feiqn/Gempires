package com.feiqn.gempires.logic.castle.structures;

import com.feiqn.gempires.GempiresGame;

public class Alchemist extends Structure {

    public Alchemist(GempiresGame game) {
        super(game.gempiresAssetHandler.alchemistTexture, game);

        structureType = StructureType.ALCHEMIST;

        this.setSize(1.5f, 1.5f);
    }
}
